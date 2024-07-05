package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/*
* Created by Serhii Tymofieiev
*/
@Parcelize
data class ErrorResponseDetailsModel(
    @field:SerializedName("code")
    val code: Int,
    @field:SerializedName("description")
    val description: String?,
    @field:SerializedName("references")
    val references: List<String>?
) : Parcelable