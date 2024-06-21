package com.capstone.aquamate.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiConfig {
    private const val BASE_URL = "http://34.101.207.223:3000/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)


}

