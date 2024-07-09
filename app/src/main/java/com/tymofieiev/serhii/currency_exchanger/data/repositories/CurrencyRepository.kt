package com.tymofieiev.serhii.currency_exchanger.data.repositories

import com.tymofieiev.serhii.currency_exchanger.data.domain.api.NetworkResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuccessResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import com.tymofieiev.serhii.currency_exchanger.data.usecases.CommissionPercentByCurrencyParameters
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
interface CurrencyRepository {
    suspend fun updateRates(): NetworkResponse<SuccessResponse>
    fun getBalancesList(): Flow<List<BalanceListItemModel>>
    fun getPairRate(sellSymbol: String, buySymbol: String): Flow<BigDecimal>
    fun getExchangeOperationList(): Flow<List<ExchangeOperationsModel>>
    suspend fun saveExchangeOperation(parameter: ExchangeOperationsModel): NetworkResponse<SuccessResponse>
    fun getComPercentBySymbol(parameter: CommissionPercentByCurrencyParameters): Flow<BigDecimal>
    fun getSupportedCurrencySymbolList(): Flow<List<String>>
}