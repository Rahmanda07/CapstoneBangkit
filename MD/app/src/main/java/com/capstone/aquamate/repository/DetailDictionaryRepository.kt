package com.capstone.aquamate.repository

import android.util.Log
import com.capstone.aquamate.api.ApiService
import com.capstone.aquamate.api.DetailDictionaryResponseApi
import retrofit2.HttpException
import java.io.IOException

class DetailDictionaryRepository(private val apiService: ApiService) {

    suspend fun getDictionaryDetail(dictionaryId: String): DetailDictionaryResponseApi {
        try {
            val response = apiService.getDictionaryDetail(dictionaryId)
            Log.d("DetailDictionaryRepo", "Response: $response")
            return response
        } catch (e: HttpException) {
            throw Exception("HTTP error occurred: ${e.message()}")
        } catch (e: IOException) {
            throw Exception("Network error occurred: ${e.message}")
        }
    }
}
