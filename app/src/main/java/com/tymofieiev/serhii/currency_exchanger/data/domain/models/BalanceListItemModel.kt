package com.tymofieiev.serhii.currency_exchanger.data.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
@Parcelize
data class BalanceListItemModel(
    val symbol: String,
    val order: Int,
    val balance: BigDecimal
) : Parcelable