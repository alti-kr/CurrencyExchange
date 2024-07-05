package com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.NetworkResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.FeeModel
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepositoryImpl.Companion.checkRatesInterval
import com.tymofieiev.serhii.currency_exchanger.data.usecases.BalancesListFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.CommissionPercentByCurrencyParameters
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetCommissionPercentByCurrencyFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetExcOperationListFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetPairRateFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetSupportedCurrencyListFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.SaveExcOperationUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.UpdateRatesUseCase
import com.tymofieiev.serhii.currency_exchanger.extention.isLesserOrEquals
import com.tymofieiev.serhii.currency_exchanger.extention.isZero
import com.tymofieiev.serhii.currency_exchanger.extention.safeToBigDecimal
import com.tymofieiev.serhii.currency_exchanger.extention.standardRounding
import com.tymofieiev.serhii.currency_exchanger.extention.tickerFlow
import com.tymofieiev.serhii.currency_exchanger.extention.toFormattedString
import com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger.dialog.OperationSuccessDialogData
import com.tymofieiev.serhii.currency_exchanger.ui.currency_picker.CurrencyPickerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
class ExchangeViewModel(
    private val updateRatesUseCase: UpdateRatesUseCase,
    balancesListFlowUseCase: BalancesListFlowUseCase,
    private val getPairRateFlowUseCase: GetPairRateFlowUseCase,
    getExcOperationListFlowUseCase: GetExcOperationListFlowUseCase,
    private val saveExcOperationUseCase: SaveExcOperationUseCase,
    private val getCommissionPercentByCurrencyFlowUseCase: GetCommissionPercentByCurrencyFlowUseCase,
    private val getSupportedCurrencyListFlowUseCase: GetSupportedCurrencyListFlowUseCase
) : ViewModel() {

    val balanceList = balancesListFlowUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    private val supportedSymbolsList = getSupportedCurrencyListFlowUseCase().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )
    val currencySellSymbol = MutableStateFlow("")
    val currencyBuySymbol = MutableStateFlow("")
    val currencyPickerData = MutableSharedFlow<CurrencyPickerParameters>()
    val sellValue = MutableStateFlow(BigDecimal.ZERO.toFormattedString(2))
    private val timeTick = MutableStateFlow(System.currentTimeMillis())

    init {
        balanceList.filter { it.isNotEmpty() }.onEach {
            if (currencySellSymbol.value.isEmpty()) {
                currencySellSymbol.emit(it[0].symbol)
            }
            if (it.size > 1 && currencyBuySymbol.value.isEmpty()) {
                currencyBuySymbol.emit(it[1].symbol)
            }
        }.launchIn(viewModelScope)
        initRefreshingTimer()
        initTimeTick()
    }

    private fun initRefreshingTimer() {
        tickerFlow(
            checkRatesInterval,
            checkRatesInterval
        ).map {
            timeTick.emit(System.currentTimeMillis())
        }.launchIn(viewModelScope)
    }


    private fun initTimeTick() {
        viewModelScope.launch {
            timeTick.collect {
                when (updateRatesUseCase(Unit)) {
                    is NetworkResponse.Success -> {} // for some kind of reaction in behavior
                    is NetworkResponse.Error -> {} //for some kind of reaction in behavior
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentRate = combine(
        currencyBuySymbol,
        currencySellSymbol, ::Pair
    ).flatMapLatest { (lCurrencyBuy, lCurrencySell) ->
        getPairRateFlowUseCase(lCurrencySell, lCurrencyBuy).map {
            it
        }
    }


    fun selectSellCurrency() {
        viewModelScope.launch {
            currencyPickerData.emit(CurrencyPickerParameters(false, balanceList.value.map { it.symbol }, null))
        }
    }

    fun selectBuyCurrency() {
        viewModelScope.launch {
            currencyPickerData.emit(
                CurrencyPickerParameters(
                    true,
                    supportedSymbolsList.value.filter { it != currencySellSymbol.value },
                    null
                )
            )
        }
    }

    fun onCurrencySelected(selectedCurrency: CurrencyPickerParameters) {
        if (selectedCurrency.selectedItem != null) {
            viewModelScope.launch {
                if (selectedCurrency.isBuy) {
                    currencyBuySymbol.emit(selectedCurrency.selectedItem)
                } else {
                    currencySellSymbol.emit(selectedCurrency.selectedItem)
                }
            }
        }
    }

    private val buyValue = combine(sellValue, currentRate) { lSellValue, lCurrentRate ->
        lSellValue.safeToBigDecimal().multiply(lCurrentRate).standardRounding()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BigDecimal.ZERO)

    val excOperationList = getExcOperationListFlowUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    val buyValueAsString = buyValue.map { it.toFormattedString(2) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "0.00")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val commissionPercent = combine(
        currencySellSymbol,
        sellValue,
        ::Pair
    ).flatMapLatest { (lCurrencySell, lSellValue) ->
        getCommissionPercentByCurrencyFlowUseCase(
            CommissionPercentByCurrencyParameters(
                lCurrencySell,
                lSellValue.safeToBigDecimal(BigDecimal.ZERO),
                getFeeModelBySymbol(lCurrencySell)
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BigDecimal.ZERO)

    private fun getFeeModelBySymbol(symbol: String): FeeModel {
        // TODO for implementing different models
        return FeeModel(5, BigDecimal.ZERO, 0, BigDecimal("0.07"))
    }

    private val commission = combine(
        sellValue,
        commissionPercent,
    ) { lSellValue, lCommissionPercent ->
        val sellSum = lSellValue.safeToBigDecimal(BigDecimal.ZERO)
        sellSum.multiply(lCommissionPercent).standardRounding()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BigDecimal.ZERO)

    val isOperationAvailable = combine(
        currencySellSymbol,
        currencyBuySymbol,
        sellValue,
        commission,
    ) { lCurrencySell, lCurrencyBuy, lSellValue, lCommission ->
        val sellSum = lSellValue.safeToBigDecimal(BigDecimal.ZERO)
        val operationSum =
            sellSum.plus(lCommission).standardRounding()
        operationSum.isLesserOrEquals(getBalanceBySymbol(lCurrencySell)) && !sellSum.isZero() && lCurrencySell != lCurrencyBuy
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private fun getBalanceBySymbol(symbol: String): BigDecimal {
        balanceList.value.forEach {
            if (it.symbol == symbol) {
                return it.balance
            }
        }
        return BigDecimal.ZERO
    }

    val successDialogData = MutableSharedFlow<OperationSuccessDialogData>()
    fun submit() {
        val sumSell = sellValue.value.safeToBigDecimal()
        val sumCommission = commission.value
        val totalSum = sumSell.add(sumCommission)
        val curSellSymbol = currencySellSymbol.value
        val balanceSell = getBalanceBySymbol(curSellSymbol).minus(totalSum)
        val curBuySymbol = currencyBuySymbol.value
        val balanceBuy = getBalanceBySymbol(curBuySymbol).plus(buyValue.value)
        val parameter = ExchangeOperationsModel(
            id = 0,
            currencySellSymbol = curSellSymbol,
            sumSell = sumSell,
            currencyBuySymbol = curBuySymbol,
            sumBuy = buyValue.value,
            sumFee = sumCommission,
            sumTotal = totalSum,
            timeStamp = System.currentTimeMillis(),
            balanceSell = balanceSell,
            balanceBuy = balanceBuy
        )
        viewModelScope.launch {
            when (val response = saveExcOperationUseCase(parameter)) {
                is NetworkResponse.Error -> {}// something wrong
                is NetworkResponse.Success -> {
                    successDialogData.emit(
                        OperationSuccessDialogData(
                            part0 = sellValue.value + " " + curSellSymbol,
                            part1 = buyValueAsString.value + " " + curBuySymbol,
                            part2 = if (commission.value.isZero()) "" else commission.value.toFormattedString(
                                2
                            )
                        )
                    )
                }
            }
        }
    }
}