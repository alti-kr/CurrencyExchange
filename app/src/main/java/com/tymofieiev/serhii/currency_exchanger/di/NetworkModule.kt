package com.tymofieiev.serhii.currency_exchanger.di

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tymofieiev.serhii.currency_exchanger.BuildConfig
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.ApiService
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.HttpAuthInterceptor
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.NetworkResponseAdapterFactory


import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.min

/*
* Created by Serhii Tymofieiev
*/
private const val CACHE_SIZE: Long = 10 * 1024 * 1024 // 10 MiB
private const val CONNECT_TIMEOUT: Long = 60
private const val READ_TIMEOUT: Long = 60
private const val WRITE_TIMEOUT: Long = 60
private val step = 400
val networkModules
    get() = listOf(networkHttpModule)
private val networkHttpModule = module {
    fun provideCache(application: Application): Cache {
        return Cache(application.cacheDir, CACHE_SIZE)
    }

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor {
            var endInd = 0
            // this needs for long response is showing in logcat
            do {
                Log.d("HttpLoggingInterceptor", it.substring(endInd, min(endInd + step, it.length)))
                endInd += step
            } while (endInd <= it.length)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    fun provideHttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: HttpAuthInterceptor,

        ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(authInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        }
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    fun provideHttpAuthInterceptor(): HttpAuthInterceptor {
        return HttpAuthInterceptor()
    }

    factory { provideCache(androidApplication()) }
    factory { provideHttpLoggingInterceptor() }
    factory { provideHttpAuthInterceptor() }
    factory { provideGson() }
    factory { provideHttpClient(get(), get(), get()) }
    single { provideRetrofit(get(), get()) }
    factory { provideApi(get()) }

}
