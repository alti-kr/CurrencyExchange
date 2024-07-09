package com.tymofieiev.serhii.currency_exchanger.di

import org.koin.core.module.Module
import java.util.LinkedList


/*
* Created by Serhii Tymofieiev
*/
object Modules {
    fun getListOfModules() = LinkedList<Module>().apply {
        addAll(presentationModules)
        addAll(dataModule)
        addAll(networkModules)
    }
}