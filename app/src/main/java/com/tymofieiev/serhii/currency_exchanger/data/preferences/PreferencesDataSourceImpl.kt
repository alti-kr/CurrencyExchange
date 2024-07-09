package com.tymofieiev.serhii.currency_exchanger.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/*
* Created by Serhii Tymofieiev
*/
class PreferencesDataSourceImpl(private val dataStore: DataStore<Preferences>) :
    PreferencesDataSource {
    override fun getBaseCurrencySymbol(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(KEY_BASE_CURRENCY_SYMBOL)]
        }.distinctUntilChanged()
    }

    override suspend fun saveBaseCurrencySymbol(symbol: String) {
        dataStore.edit { edit ->
            edit[stringPreferencesKey(KEY_BASE_CURRENCY_SYMBOL)] = symbol
        }
    }

    companion object {
        val KEY_BASE_CURRENCY_SYMBOL = "key_base_currency_symbol"
    }
}