package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.component.KoinComponent

/*
* Created by Serhii Tymofieiev
*/
class HttpAuthInterceptor : Interceptor,
    KoinComponent {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = newRequestWithAccessTokenAndNodeKey(chain.request())
        return chain.proceed(request)
    }

    private fun newRequestWithAccessTokenAndNodeKey(
        request: Request,
    ): Request {
        val builder = request.newBuilder()
        builder.header("Content-Type", "application/json")
        return builder.build()
    }
}