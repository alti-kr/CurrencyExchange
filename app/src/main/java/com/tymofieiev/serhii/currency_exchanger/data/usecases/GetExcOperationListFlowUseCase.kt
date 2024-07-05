package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow


/*
* Created by Serhii Tymofieiev
*/
class GetExcOperationListFlowUseCase(private val currencyRepository: CurrencyRepository) {
    operator fun invoke(): Flow<List<ExchangeOperationsModel>> =
        currencyRepository.getExchangeOperationList()
}