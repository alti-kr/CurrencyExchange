package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import okhttp3.Request
import okio.Timeout
import org.koin.core.component.KoinComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/*
* Created by Serhii Tymofieiev
*/
class NetworkResponseCall<S : Any>(
    private val delegate: Call<S>
) : Call<NetworkResponse<S>>, KoinComponent {
    override fun enqueue(callback: Callback<NetworkResponse<S>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(body))
                        )
                    }
                } else {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(
                            NetworkResponse.Error(
                                NetworkResponse.RetrofitException.httpError(
                                    response
                                )
                            )
                        )
                    )
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> NetworkResponse.Error(
                        NetworkResponse.RetrofitException.networkError(
                            throwable
                        )
                    )

                    else -> NetworkResponse.Error(
                        NetworkResponse.RetrofitException.unexpectedError(
                            throwable
                        )
                    )
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    // Some time will write self converter
    //override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)
    override fun clone() = NetworkResponseCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
