package com.tymofieiev.serhii.currency_exchanger.data.domain.models

import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
data class FeeModel(
    val firstFreeCount: Int = 0,
    val maxFreeSum: BigDecimal,
    val freeInterval: Int = 0,
    val feePercent: BigDecimal
)