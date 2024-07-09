package com.tymofieiev.serhii.currency_exchanger.extention

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale


/*
* Created by Serhii Tymofieiev
*/
val localeFormat = Locale.US
fun BigDecimal.toFormattedString(
    fractionDigits: Int = 2,
    displayDashesIfZero: Boolean = false,
): String {
    return NumberFormat.getInstance(localeFormat).apply {
        this.minimumFractionDigits = fractionDigits
        this.maximumFractionDigits = fractionDigits
        this.isGroupingUsed = false
    }.format(this)

}

fun BigDecimal.divideSafeCommon(divideValue: BigDecimal?): BigDecimal {
    return if (divideValue == null || divideValue.isZero()) {
        BigDecimal.ZERO
    } else {
        this.divide(divideValue, MathContext.DECIMAL64)
    }
}

fun BigDecimal.isZero() = this.isEquals(BigDecimal.ZERO)
fun BigDecimal.isEquals(equalsValue: BigDecimal?): Boolean {
    return if (equalsValue == null) {
        false
    } else {
        compareTo(equalsValue) == 0
    }
}

fun BigDecimal.standardRounding() = this.standardRounding(2)
fun BigDecimal.standardRounding(fractionDigits: Int) =
    this.setScale(fractionDigits, RoundingMode.HALF_UP)

fun BigDecimal.isLesserOrEquals(equalsValue: BigDecimal?): Boolean {
    return if (equalsValue == null) {
        false
    } else {
        compareTo(equalsValue) == -1 || compareTo(equalsValue) == 0
    }
}