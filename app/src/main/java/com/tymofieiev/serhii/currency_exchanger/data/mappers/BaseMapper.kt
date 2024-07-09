package com.tymofieiev.serhii.currency_exchanger.data.mappers


/*
* Created by Serhii Tymofieiev
*/
interface BaseMapper<I, O> {
    fun map(input: I): O
}