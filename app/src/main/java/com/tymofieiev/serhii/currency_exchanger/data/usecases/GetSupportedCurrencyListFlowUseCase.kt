package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow


/*
* Created by Serhii Tymofieiev  on 08.07.2024.
*/
class GetSupportedCurrencyListFlowUseCase(private val currencyRepository: CurrencyRepository) {
    operator fun invoke(): Flow<List<String>> = currencyRepository.getSupportedCurrencySymbolList()
}