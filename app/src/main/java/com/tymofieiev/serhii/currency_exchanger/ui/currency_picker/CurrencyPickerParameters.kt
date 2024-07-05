package com.tymofieiev.serhii.currency_exchanger.ui.currency_picker

import android.os.Parcelable
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel
import kotlinx.parcelize.Parcelize


/*
* Created by Serhii Tymofieiev
*/@Parcelize
data class CurrencyPickerParameters(
    val isBuy: Boolean,
    val items: List<String>,
    val selectedItem: String?
) :
    Parcelable