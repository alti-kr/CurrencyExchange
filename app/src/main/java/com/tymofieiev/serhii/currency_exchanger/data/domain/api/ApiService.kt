package com.tymofieiev.serhii.currency_exchanger.data.domain.api

import com.tymofieiev.serhii.currency_exchanger.data.domain.models.RatesResponseModel
import retrofit2.http.GET


/*
* Created by Serhii Tymofieiev
*/
interface ApiService {
    @GET("tasks/api/currency-exchange-rates")
    suspend fun fetchRates(): NetworkResponse<RatesResponseModel>
}