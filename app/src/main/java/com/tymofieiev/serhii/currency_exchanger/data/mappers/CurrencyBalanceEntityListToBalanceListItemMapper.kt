package com.tymofieiev.serhii.currency_exchanger.data.mappers

import com.tymofieiev.serhii.currency_exchanger.data.database.entities.CurrencyBalanceEntity
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel


/*
* Created by Serhii Tymofieiev
*/
class CurrencyBalanceEntityListToBalanceListItemMapper :
    BaseMapper<List<CurrencyBalanceEntity>, List<BalanceListItemModel>> {
    override fun map(input: List<CurrencyBalanceEntity>): List<BalanceListItemModel> {
        return input.map {
            BalanceListItemModel(it.currencySymbol, it.order, it.balance)
        }.sortedBy { it.order }
    }
}