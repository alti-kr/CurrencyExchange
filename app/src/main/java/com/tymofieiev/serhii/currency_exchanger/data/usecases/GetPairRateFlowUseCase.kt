package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
class GetPairRateFlowUseCase(private val currencyRepository: CurrencyRepository) {
    operator fun invoke(sellSymbol: String, buySymbol: String): Flow<BigDecimal> =
        currencyRepository.getPairRate(sellSymbol, buySymbol)
}