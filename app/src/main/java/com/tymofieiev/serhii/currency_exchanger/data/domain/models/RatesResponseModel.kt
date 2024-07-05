package com.tymofieiev.serhii.currency_exchanger.data.domain.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
data class RatesResponseModel(
    @field:SerializedName("base")
    val baseCurrency: String,
    @field:SerializedName("date")
    val date: String,
    @field:SerializedName("rates")
    val ratesMap: Map<String, BigDecimal>
)