package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.domain.api.NetworkResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuccessResponse
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher


/*
* Created by Serhii Tymofieiev
*/
class UpdateRatesUseCaseImpl(
    defaultDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository,
) : UpdateRatesUseCase(defaultDispatcher) {
    override suspend fun execute(parameter: Unit): NetworkResponse<SuccessResponse> {
        return currencyRepository.updateRates()
    }
}