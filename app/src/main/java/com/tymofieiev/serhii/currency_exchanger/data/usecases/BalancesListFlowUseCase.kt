package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow


/*
* Created by Serhii Tymofieiev
*/
class BalancesListFlowUseCase(private val currencyRepository: CurrencyRepository) {
    operator fun invoke(): Flow<List<BalanceListItemModel>> =
        currencyRepository.getBalancesList()
}