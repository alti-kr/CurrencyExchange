package com.tymofieiev.serhii.currency_exchanger.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
@Entity(tableName = "rates")
data class RateEntity(
    @PrimaryKey
    @ColumnInfo(name = "pair_symbol")
    val pairSymbol: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "rate")
    val rate: BigDecimal,
)