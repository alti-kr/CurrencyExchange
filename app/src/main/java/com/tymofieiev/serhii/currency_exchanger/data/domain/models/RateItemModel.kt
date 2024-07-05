package com.tymofieiev.serhii.currency_exchanger.data.domain.models

import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
data class RateItemModel(
    val pairSymbol: String,
    val rate: BigDecimal
)