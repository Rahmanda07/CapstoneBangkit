package com.capstone.aquamate.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.aquamate.api.ApiConfig
import com.capstone.aquamate.api.PredictFishResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictRepository {
    fun uploadImage(image: MultipartBody.Part): LiveData<PredictFishResponse> {
        val responseLiveData = MutableLiveData<PredictFishResponse>()

        ApiConfig.apiService.uploadImage(image).enqueue(object : Callback<PredictFishResponse> {
            override fun onResponse(call: Call<PredictFishResponse>, response: Response<PredictFishResponse>) {
                if (response.isSuccessful) {
                    responseLiveData.value = response.body()
                    Log.d("PredictRepository", "Request success: ${responseLiveData.value}")

                } else {
                    val errorResponse = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("PredictRepository", "Request failed: $errorResponse")
                    responseLiveData.value = null
                }
            }

            override fun onFailure(call: Call<PredictFishResponse>, t: Throwable) {
                Log.e("PredictRepository", "Request failed: ${t.message}")
                responseLiveData.value = null
            }
        })

        return responseLiveData
    }
}
