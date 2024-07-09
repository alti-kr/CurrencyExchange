package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/*
* Created by Serhii Tymofieiev
*/
abstract class SuspendNetworkUseCase<in P, out R : Any>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameters: P): NetworkResponse<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters)
            }
        } catch (e: Exception) {
            NetworkResponse.Error(NetworkResponse.RetrofitException.unexpectedError(e))
        }
    }

    protected abstract suspend fun execute(parameter: P): NetworkResponse<R>
}