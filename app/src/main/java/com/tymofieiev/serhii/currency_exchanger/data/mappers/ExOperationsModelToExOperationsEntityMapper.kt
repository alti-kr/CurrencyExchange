package com.tymofieiev.serhii.currency_exchanger.data.mappers

import com.tymofieiev.serhii.currency_exchanger.data.database.entities.ExchangeOperationsEntity
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel


/*
* Created by Serhii Tymofieiev
*/
class ExOperationsModelToExOperationsEntityMapper :
    BaseMapper<ExchangeOperationsModel, ExchangeOperationsEntity> {
    override fun map(input: ExchangeOperationsModel): ExchangeOperationsEntity {
        return ExchangeOperationsEntity(
            currencySellSymbol = input.currencySellSymbol,
            sumSell = input.sumSell,
            currencyBuySymbol = input.currencyBuySymbol,
            sumBuy = input.sumBuy,
            sumFee = input.sumFee,
            sumTotal = input.sumTotal,
            timeStamp = input.timeStamp
        )
    }
}