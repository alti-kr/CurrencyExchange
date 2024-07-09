package com.tymofieiev.serhii.currency_exchanger.di

import org.koin.dsl.module
import com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger.ExchangeFragment
import com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger.ExchangeViewModel
import org.koin.androidx.fragment.dsl.fragmentOf
import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.tymofieiev.serhii.currency_exchanger.ui.currency_picker.CurrencyPickerBottomSheet
import com.tymofieiev.serhii.currency_exchanger.ui.currency_picker.CurrencyPickerViewModel

/*
* Created by Serhii Tymofieiev
*/
val presentationModules
    get() = listOf(exchangeModule, currencyPickerModule)

private val exchangeModule = module {
    viewModelOf(::ExchangeViewModel)
    fragmentOf(::ExchangeFragment)
}

private val currencyPickerModule = module {
    viewModelOf(::CurrencyPickerViewModel)
    fragmentOf(::CurrencyPickerBottomSheet)
}