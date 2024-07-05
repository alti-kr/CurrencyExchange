package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuccessResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuspendNetworkUseCase
import kotlinx.coroutines.CoroutineDispatcher


/*
* Created by Serhii Tymofieiev
*/
abstract class UpdateRatesUseCase(
    defaultDispatcher: CoroutineDispatcher
) : SuspendNetworkUseCase<Unit, SuccessResponse>(defaultDispatcher)