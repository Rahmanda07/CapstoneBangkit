package com.capstone.aquamate.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.aquamate.repository.PredictRepository
import com.capstone.aquamate.viewmodel.PredictViewModel

class PredictViewModelFactory(private val repository: PredictRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PredictViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PredictViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
