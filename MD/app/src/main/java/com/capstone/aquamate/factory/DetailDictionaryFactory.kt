package com.capstone.aquamate.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.aquamate.api.ApiService
import com.capstone.aquamate.repository.DetailDictionaryRepository
import com.capstone.aquamate.viewmodel.DetailDictionaryViewModel

class DetailDictionaryFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailDictionaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailDictionaryViewModel(DetailDictionaryRepository(apiService)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
