package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/*
* Created by Serhii Tymofieiev
*/
@Parcelize
data
class ErrorResponseModel(
    @field:SerializedName("success")
    val success: Boolean,
    @field:SerializedName("request_id")
    val requestId: String?,
    @field:SerializedName("errors")
    val errorsDetails: List<ErrorResponseDetailsModel>?
) : Parcelable