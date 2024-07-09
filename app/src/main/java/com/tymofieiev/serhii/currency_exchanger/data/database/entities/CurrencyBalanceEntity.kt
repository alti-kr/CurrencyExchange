package com.tymofieiev.serhii.currency_exchanger.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
@Entity(tableName = "currency_balance")
data class CurrencyBalanceEntity(
    @PrimaryKey
    @ColumnInfo(name = "currency_symbol")
    val currencySymbol: String,
    @ColumnInfo(name = "order")
    val order: Int,
    @ColumnInfo(name = "balance")
    val balance: BigDecimal
)