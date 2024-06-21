package com.capstone.aquamate.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.aquamate.viewmodel.ForgetPasswordViewModel

class ForgetPasswordViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForgetPasswordViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
