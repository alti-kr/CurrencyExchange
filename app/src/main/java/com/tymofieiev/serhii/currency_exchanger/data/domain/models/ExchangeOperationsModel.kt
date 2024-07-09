package com.tymofieiev.serhii.currency_exchanger.data.domain.models

import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
data class ExchangeOperationsModel(
    val id: Int = 0,
    val currencySellSymbol: String,
    val sumSell: BigDecimal,
    val currencyBuySymbol: String,
    val sumBuy: BigDecimal,
    val sumFee: BigDecimal,
    val sumTotal: BigDecimal,
    val timeStamp: Long,
    val balanceSell: BigDecimal,
    val balanceBuy: BigDecimal
)