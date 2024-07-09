package com.tymofieiev.serhii.currency_exchanger.data.repositories


import com.tymofieiev.serhii.currency_exchanger.data.database.dao.CurrencyDao
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.CurrencyBalanceEntity
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.ApiService
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.NetworkResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuccessResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import com.tymofieiev.serhii.currency_exchanger.data.mappers.CurrencyBalanceEntityListToBalanceListItemMapper
import com.tymofieiev.serhii.currency_exchanger.data.mappers.ExOperationsEntityToExOperationsModelListMapper
import com.tymofieiev.serhii.currency_exchanger.data.mappers.ExOperationsModelToExOperationsEntityMapper
import com.tymofieiev.serhii.currency_exchanger.data.mappers.RatesResponseToRateEntityListMapper
import com.tymofieiev.serhii.currency_exchanger.data.preferences.PreferencesDataSource
import com.tymofieiev.serhii.currency_exchanger.data.usecases.CommissionPercentByCurrencyParameters
import com.tymofieiev.serhii.currency_exchanger.extention.divideSafeCommon
import com.tymofieiev.serhii.currency_exchanger.extention.isLesserOrEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import kotlin.time.Duration.Companion.seconds


/*
* Created by Serhii Tymofieiev
*/
class CurrencyRepositoryImpl(
    private val apiService: ApiService,
    private val currencyDao: CurrencyDao,
    private val ratesResponseToRateEntityListMapper: RatesResponseToRateEntityListMapper,
    private val currencyBalanceEntityListToBalanceListItemMapper: CurrencyBalanceEntityListToBalanceListItemMapper,
    private val preferencesDataSource: PreferencesDataSource,
    private val exOperationsModelToExOperationsEntityMapper: ExOperationsModelToExOperationsEntityMapper,
    private val exOperationsEntityToExOperationsModelListMapper: ExOperationsEntityToExOperationsModelListMapper,
) : CurrencyRepository {
    override suspend fun updateRates(): NetworkResponse<SuccessResponse> {
        return when (val response = apiService.fetchRates()) {
            is NetworkResponse.Error -> response
            is NetworkResponse.Success -> {
                currencyDao.updateRates(ratesResponseToRateEntityListMapper.map(response.content))
                preferencesDataSource.saveBaseCurrencySymbol(response.content.baseCurrency)
                NetworkResponse.Success(SuccessResponse(true))
            }
        }
    }

    override fun getBalancesList(): Flow<List<BalanceListItemModel>> {
        return currencyDao.getBalanceList().map {
            currencyBalanceEntityListToBalanceListItemMapper.map(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPairRate(sellSymbol: String, buySymbol: String): Flow<BigDecimal> {
        return preferencesDataSource.getBaseCurrencySymbol().filterNotNull()
            .flatMapLatest { baseSymbol ->
                val rateSell = currencyDao.getRateByPairSymbol(baseSymbol + sellSymbol)
                val rateBuy = currencyDao.getRateByPairSymbol(baseSymbol + buySymbol)
                combine(
                    rateSell,
                    rateBuy
                ) { sellRateModel, buyRateModel ->
                    if(sellRateModel == null || buyRateModel == null){
                        BigDecimal.ZERO
                    }else {
                        buyRateModel.rate.divideSafeCommon(sellRateModel.rate)
                    }
                }
            }
    }

    override fun getExchangeOperationList(): Flow<List<ExchangeOperationsModel>> {
        return currencyDao.getExcOperationList().map {
            exOperationsEntityToExOperationsModelListMapper.map(it)
                .sortedByDescending { it.timeStamp }
        }
    }

    override suspend fun saveExchangeOperation(parameter: ExchangeOperationsModel): NetworkResponse<SuccessResponse> {
        currencyDao.updateBalance(parameter.currencySellSymbol, parameter.balanceSell)
        if(currencyDao.isBalanceExist(parameter.currencyBuySymbol) == 0){
            currencyDao.insertCurrency(CurrencyBalanceEntity(currencySymbol = parameter.currencyBuySymbol, 2, parameter.balanceBuy))
        }else {
            currencyDao.updateBalance(parameter.currencyBuySymbol, parameter.balanceBuy)
        }
        currencyDao.insertExcOperation(exOperationsModelToExOperationsEntityMapper.map(parameter))
        return NetworkResponse.Success(SuccessResponse(true))
    }

    override fun getComPercentBySymbol(parameter: CommissionPercentByCurrencyParameters): Flow<BigDecimal> {
        //a mechanism for determining the commission percentage is implemented here
        return currencyDao.operationCountBySymbol(parameter.symbol).map { operationCountBySymbol ->
            if (operationCountBySymbol < parameter.fee.firstFreeCount) {
                BigDecimal.ZERO
            } else if (parameter.fee.freeInterval != 0 && operationCountBySymbol % parameter.fee.freeInterval == 0) {
                BigDecimal.ZERO
            } else if (parameter.sum.isLesserOrEquals(parameter.fee.maxFreeSum)) {
                BigDecimal.ZERO
            } else {
                parameter.fee.feePercent
            }
        }
    }

    override fun getSupportedCurrencySymbolList(): Flow<List<String>> {
        return currencyDao.getAllCurrencySymbolList()
    }

    companion object {

        val checkRatesInterval = 5.seconds
    }
}