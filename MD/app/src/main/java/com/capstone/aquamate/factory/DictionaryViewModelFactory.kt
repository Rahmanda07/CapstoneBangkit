package com.capstone.aquamate.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.aquamate.repository.DictionaryRepository
import com.capstone.aquamate.viewmodel.DictionaryViewModel

class DictionaryViewModelFactory(private val repository: DictionaryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
            return DictionaryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
