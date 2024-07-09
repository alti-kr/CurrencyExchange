package com.tymofieiev.serhii.currency_exchanger.data.mappers

import com.tymofieiev.serhii.currency_exchanger.data.database.entities.RateEntity
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.RatesResponseModel


/*
* Created by Serhii Tymofieiev
*/
class RatesResponseToRateEntityListMapper : BaseMapper<RatesResponseModel, List<RateEntity>> {
    override fun map(input: RatesResponseModel): List<RateEntity> {
        val baseCurrencySymbol = input.baseCurrency
        return input.ratesMap.map { (k, v) ->
            RateEntity(pairSymbol = baseCurrencySymbol + k, k,rate = v);
        }
    }
}