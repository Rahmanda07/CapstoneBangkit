package com.capstone.aquamate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aquamate.api.DataItemFishDictionary
import com.capstone.aquamate.repository.DictionaryRepository
import kotlinx.coroutines.launch

class DictionaryViewModel(private val repository: DictionaryRepository) : ViewModel() {

    private val _dictionaryLiveData = MutableLiveData<List<DataItemFishDictionary>>()
    val dictionaryLiveData: LiveData<List<DataItemFishDictionary>> = _dictionaryLiveData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getDictionaryFromApi() {
        viewModelScope.launch {
            try {
                val response = repository.getDictionaryFish()
                if (response.status) {
                    _dictionaryLiveData.value = response.data
                    Log.d("DictionaryViewModel", "Successfully fetched dictionary: ${response.data}")
                } else {
                    _errorMessage.value = "Request failed"
                    _dictionaryLiveData.value = emptyList() // Ensure the LiveData is updated to an empty list on failure
                    Log.e("DictionaryViewModel", "Failed to fetch dictionary: Request failed")
                }
            } catch (t: Throwable) {
                _errorMessage.value = "Request failed: ${t.message}"
                _dictionaryLiveData.value = emptyList()
                Log.e("DictionaryViewModel", "Failed to fetch dictionary", t)
            }
        }
    }
}