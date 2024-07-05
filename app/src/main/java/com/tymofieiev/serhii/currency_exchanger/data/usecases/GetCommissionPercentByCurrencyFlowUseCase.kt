package com.tymofieiev.serhii.currency_exchanger.data.usecases


import com.tymofieiev.serhii.currency_exchanger.data.domain.models.FeeModel
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
class GetCommissionPercentByCurrencyFlowUseCase(private val currencyRepository: CurrencyRepository) {
    operator fun invoke(parameter: CommissionPercentByCurrencyParameters): Flow<BigDecimal> =
        currencyRepository.getComPercentBySymbol(parameter)
}

data class CommissionPercentByCurrencyParameters(
    val symbol: String,
    val sum: BigDecimal,
    val fee: FeeModel
)