package com.tymofieiev.serhii.currency_exchanger.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tymofieiev.serhii.currency_exchanger.R
import com.tymofieiev.serhii.currency_exchanger.data.database.ExchangeDatabase
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.CurrencyBalanceEntity
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.bind
import com.tymofieiev.serhii.currency_exchanger.data.repositories.CurrencyRepositoryImpl
import com.tymofieiev.serhii.currency_exchanger.data.usecases.UpdateRatesUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.UpdateRatesUseCaseImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import  com.tymofieiev.serhii.currency_exchanger.data.mappers.RatesResponseToRateEntityListMapper
import  com.tymofieiev.serhii.currency_exchanger.data.mappers.CurrencyBalanceEntityListToBalanceListItemMapper
import com.tymofieiev.serhii.currency_exchanger.data.preferences.PreferencesDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import com.tymofieiev.serhii.currency_exchanger.data.usecases.BalancesListFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetPairRateFlowUseCase
import org.koin.core.module.dsl.factoryOf
import com.tymofieiev.serhii.currency_exchanger.data.preferences.PreferencesDataSourceImpl
import com.tymofieiev.serhii.currency_exchanger.data.mappers.ExOperationsModelToExOperationsEntityMapper
import com.tymofieiev.serhii.currency_exchanger.data.mappers.ExOperationsEntityToExOperationsModelListMapper
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetExcOperationListFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.SaveExcOperationUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.SaveExcOperationUseCaseImpl
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetCommissionPercentByCurrencyFlowUseCase
import com.tymofieiev.serhii.currency_exchanger.data.usecases.GetSupportedCurrencyListFlowUseCase
import java.math.BigDecimal

/*
* Created by Serhii Tymofieiev
*/
val dataModule
    get() = listOf(
        repositoryModule,
        useCasesModule,
        dbDataModule,
        mappersModule,
        preferencesModule
    )
private val repositoryModule = module {
    singleOf(::CurrencyRepositoryImpl) { bind<CurrencyRepository>() }
}

private val useCasesModule = module {
    fun provideUpdateRatesUseCase(
        defaultDispatcher: CoroutineDispatcher,
        currencyRepository: CurrencyRepository
    ): UpdateRatesUseCase {
        return UpdateRatesUseCaseImpl(defaultDispatcher, currencyRepository)
    }
    single { provideUpdateRatesUseCase(Dispatchers.IO, get()) }
    singleOf(::BalancesListFlowUseCase)
    factoryOf(::GetPairRateFlowUseCase)
    fun provideSaveExcOperationUseCase(
        defaultDispatcher: CoroutineDispatcher,
        currencyRepository: CurrencyRepository
    ): SaveExcOperationUseCase {
        return SaveExcOperationUseCaseImpl(defaultDispatcher, currencyRepository)
    }
    single { provideSaveExcOperationUseCase(Dispatchers.IO, get()) }
    singleOf(::GetExcOperationListFlowUseCase)
    singleOf(::GetCommissionPercentByCurrencyFlowUseCase)
    singleOf(::GetSupportedCurrencyListFlowUseCase)
}
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val preferencesModule = module {
    single { androidApplication().dataStore }
    factoryOf(::PreferencesDataSourceImpl) { bind<PreferencesDataSource>() }
}

private val dbDataModule = module {
    lateinit var database: ExchangeDatabase
    fun provideDatabase(application: Application): ExchangeDatabase {
        database = Room.databaseBuilder(
            application,
            ExchangeDatabase::class.java,
            "exchange_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    val currencyDao = database.currencyDao()
                    val balanceList: JSONArray =
                        application.resources.openRawResource(R.raw.prepopulate_db).bufferedReader()
                            .use {
                                JSONArray(it.readText())
                            }
                    balanceList.takeIf { it.length() > 0 }?.let { list ->
                        for (index in 0 until list.length()) {
                            val currencyObject = list.getJSONObject(index)
                            currencyDao.insertCurrency(
                                CurrencyBalanceEntity(
                                    currencySymbol = currencyObject.getString("currencySymbol"),
                                    order = currencyObject.getInt("order"),
                                    balance = BigDecimal(currencyObject.getInt("balance"))
                                )
                            )
                        }
                    }
                }
            }
        })
            .build()
        return database
    }
    single { provideDatabase(androidApplication()) }
    single { get<ExchangeDatabase>().currencyDao() }
}

private val mappersModule = module {
    singleOf(::RatesResponseToRateEntityListMapper)
    singleOf(::CurrencyBalanceEntityListToBalanceListItemMapper)
    singleOf(::ExOperationsEntityToExOperationsModelListMapper)
    singleOf(::ExOperationsModelToExOperationsEntityMapper)
}