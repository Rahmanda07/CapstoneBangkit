package com.capstone.aquamate.api

import retrofit2.Call
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {
    @Multipart
    @POST("aquamate/predict")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<PredictFishResponse>

    @GET("aquamate/dictionary")
    suspend fun getDictionaryFish(): DictionaryFishResponse

    @GET("aquamate/dictionary/{id}")
    suspend fun getDictionaryDetail(
        @Path("id") dictionaryId: String
    ): DetailDictionaryResponseApi
}