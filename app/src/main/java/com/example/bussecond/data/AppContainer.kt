package com.example.bussecond.data

import com.example.bussecond.network.EtaApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val etaRepository: EtaRepository
}

class DefaultAppContainer(): AppContainer{
    private val baseUrl = "https://data.etabus.gov.hk"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: EtaApiService by lazy {
        retrofit.create(EtaApiService::class.java)
    }

    override val etaRepository: EtaRepository = EtaRepository(retrofitService)
}