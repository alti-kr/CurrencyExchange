package com.tymofieiev.serhii.currency_exchanger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tymofieiev.serhii.currency_exchanger.data.database.converters.BigDecimalTypeConverter
import com.tymofieiev.serhii.currency_exchanger.data.database.dao.CurrencyDao
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.CurrencyBalanceEntity
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.ExchangeOperationsEntity
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.RateEntity


/*
* Created by Serhii Tymofieiev
*/
@Database(
    entities = [
        CurrencyBalanceEntity::class,
        RateEntity::class,
        ExchangeOperationsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class ExchangeDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}