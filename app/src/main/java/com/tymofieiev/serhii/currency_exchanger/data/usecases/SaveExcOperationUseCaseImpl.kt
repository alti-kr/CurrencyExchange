package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.domain.api.NetworkResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuccessResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher


/*
* Created by Serhii Tymofieiev
*/
class SaveExcOperationUseCaseImpl(
    defaultDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository,
) : SaveExcOperationUseCase(defaultDispatcher) {
    override suspend fun execute(parameter: ExchangeOperationsModel): NetworkResponse<SuccessResponse> {
        return currencyRepository.saveExchangeOperation(parameter)
    }
}