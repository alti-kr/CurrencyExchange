package com.tymofieiev.serhii.currency_exchanger.data.usecases

import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuccessResponse
import com.tymofieiev.serhii.currency_exchanger.data.domain.api.SuspendNetworkUseCase
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import kotlinx.coroutines.CoroutineDispatcher


/*
* Created by Serhii Tymofieiev
*/
abstract class SaveExcOperationUseCase(
    defaultDispatcher: CoroutineDispatcher
) : SuspendNetworkUseCase<ExchangeOperationsModel, SuccessResponse>(defaultDispatcher)
