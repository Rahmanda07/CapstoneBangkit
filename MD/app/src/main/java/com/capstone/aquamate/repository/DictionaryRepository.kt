package com.capstone.aquamate.repository

import android.util.Log
import com.capstone.aquamate.api.ApiConfig
import com.capstone.aquamate.api.DictionaryFishResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DictionaryRepository {

    suspend fun getDictionaryFish(): DictionaryFishResponse {
        try {
            return withContext(Dispatchers.IO) {
                ApiConfig.apiService.getDictionaryFish()
            }
        } catch (t: Throwable) {
            Log.e("DictionaryRepository", "Request failed: ${t.message}", t)
            return DictionaryFishResponse(false, emptyList())
        }
    }
}

