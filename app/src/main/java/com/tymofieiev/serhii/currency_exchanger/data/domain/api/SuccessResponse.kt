package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import com.google.gson.annotations.SerializedName


/*
* Created by Serhii Tymofieiev
*/
data class SuccessResponse(
    @field:SerializedName("success")
    val success: Boolean,
)