package com.tymofieiev.serhii.currency_exchanger.data.database.converters

import androidx.room.TypeConverter
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
class BigDecimalTypeConverter {

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String? {
        return input?.toPlainString()
    }

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        if (input.isNullOrBlank()) return BigDecimal.ZERO
        return input.toBigDecimalOrNull() ?: BigDecimal.ZERO
    }

}