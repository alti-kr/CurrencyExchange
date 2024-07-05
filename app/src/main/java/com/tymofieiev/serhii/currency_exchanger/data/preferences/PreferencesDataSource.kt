package com.tymofieiev.serhii.currency_exchanger.data.preferences

import kotlinx.coroutines.flow.Flow


/*
* Created by Serhii Tymofieiev
*/
interface PreferencesDataSource {
    fun getBaseCurrencySymbol(): Flow<String?>
    suspend fun saveBaseCurrencySymbol(symbol: String)
}