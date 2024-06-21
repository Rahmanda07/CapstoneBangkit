package com.capstone.aquamate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aquamate.api.DetailDictionaryResponseApi
import com.capstone.aquamate.repository.DetailDictionaryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetailDictionaryViewModel(private val repository: DetailDictionaryRepository) : ViewModel() {

    private val _dictionaryDetail = MutableLiveData<DetailDictionaryResponseApi>()
    val dictionaryDetail: LiveData<DetailDictionaryResponseApi> get() = _dictionaryDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getDictionaryDetail(dictionaryId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDictionaryDetail(dictionaryId)
                _dictionaryDetail.value = response
                Log.d("DetailDictionaryVM", "Dictionary detail fetched successfully: $response")
            } catch (e: HttpException) {
                _errorMessage.value = "HTTP error occurred: ${e.message()}"
                Log.e("DetailDictionaryVM", "HTTP error occurred: ${e.message()}", e)
            } catch (e: IOException) {
                _errorMessage.value = "Network error occurred: ${e.message}"
                Log.e("DetailDictionaryVM", "Network error occurred: ${e.message}", e)
            } catch (e: Exception) {
                _errorMessage.value = "Error occurred: ${e.message}"
                Log.e("DetailDictionaryVM", "Error occurred: ${e.message}", e)
            }
        }
    }
}
