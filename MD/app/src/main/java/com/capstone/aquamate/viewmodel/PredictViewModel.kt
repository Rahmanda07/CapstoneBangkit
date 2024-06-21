package com.capstone.aquamate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.aquamate.api.PredictFishResponse
import com.capstone.aquamate.repository.PredictRepository
import okhttp3.MultipartBody

class PredictViewModel(private val repository: PredictRepository) : ViewModel() {
    fun uploadImage(image: MultipartBody.Part): LiveData<PredictFishResponse> {
        return repository.uploadImage(image)
    }
}