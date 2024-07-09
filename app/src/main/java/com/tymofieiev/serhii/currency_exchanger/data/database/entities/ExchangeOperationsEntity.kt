package com.tymofieiev.serhii.currency_exchanger.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
@Entity(tableName = "exchange_operations")
data class ExchangeOperationsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "currency_sell_symbol")
    val currencySellSymbol: String,
    @ColumnInfo(name = "sum_sell")
    val sumSell: BigDecimal,
    @ColumnInfo(name = "currency_buy_symbol")
    val currencyBuySymbol: String,
    @ColumnInfo(name = "sum_buy")
    val sumBuy: BigDecimal,
    @ColumnInfo(name = "sum_fee")
    val sumFee: BigDecimal,
    @ColumnInfo(name = "sum_total")
    val sumTotal: BigDecimal,
    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long
)