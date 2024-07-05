package com.tymofieiev.serhii.currency_exchanger.data.mappers

import com.tymofieiev.serhii.currency_exchanger.data.database.entities.ExchangeOperationsEntity
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
class ExOperationsEntityToExOperationsModelListMapper :
    BaseMapper<List<ExchangeOperationsEntity>, List<ExchangeOperationsModel>> {
    override fun map(input: List<ExchangeOperationsEntity>): List<ExchangeOperationsModel> {
        return input.map {
            ExchangeOperationsModel(
                id = it.id,
                currencySellSymbol = it.currencySellSymbol,
                sumSell = it.sumSell,
                currencyBuySymbol = it.currencyBuySymbol,
                sumBuy = it.sumBuy,
                sumFee = it.sumFee,
                sumTotal = it.sumTotal,
                timeStamp = it.timeStamp,
                balanceSell = BigDecimal.ZERO,
                balanceBuy = BigDecimal.ZERO
            )
        }
    }
}