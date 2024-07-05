package com.tymofieiev.serhii.currency_exchanger

import android.app.Application
import com.tymofieiev.serhii.currency_exchanger.di.Modules.getListOfModules
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger


/*
* Created by Serhii Tymofieiev
*/
class AppMain : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppMain)
            logger(EmptyLogger())
            fragmentFactory()
            modules(getListOfModules())
        }
    }
}