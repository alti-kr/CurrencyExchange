package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import com.google.gson.Gson


/*
* Created by Serhii Tymofieiev
*/
fun <E> convertStringToModel(source: String?, classOfE: Class<E>): E? {
    return try {
        val gson = Gson()
        gson.fromJson(source, classOfE)
    } catch (e: Exception) {
        null
    }
}