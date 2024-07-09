package com.tymofieiev.serhii.currency_exchanger.extention

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
class InputFilterFractionDigestLength(private val digitLength: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val input = makeNewInputString(dstart, dest, source).safeToBigDecimal()
        if (digitLength <= 0) {
            if (source?.equals(",") == true || source?.equals(".") == true) {
                return ""
            }
            if (input.scale() > digitLength) {
                return ""
            }
        } else {

            if (input.scale() > digitLength) {
                return ""
            }
        }
        return null
    }

}

private fun makeNewInputString(dstart: Int, dest: Spanned?, source: CharSequence?): String {
    return try {
        val destAsString = dest.toString()
        (destAsString.substring(0, dstart) + source.toString() + destAsString.substring(
            dstart + (if (source.toString().isEmpty()) 1 else 0),
            destAsString.length
        ))
    } catch (e: Exception) {
        ""
    }
}

fun String.safeToBigDecimal(defValue: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return if (this.isEmpty()) {
        defValue
    } else {
        try {
            BigDecimal(this)
        } catch (e: Exception) {
            defValue
        }
    }
}
open class SimpleTextWatcher(
    private val onChanged: (String) -> Unit
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = s?.toString() ?: return
        onChanged.invoke(text)
    }
}