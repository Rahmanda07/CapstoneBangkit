package com.capstone.aquamate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _resetEmailSent = MutableLiveData<Boolean>()
    val resetEmailSent: LiveData<Boolean> get() = _resetEmailSent

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resetEmailSent.value = true
                } else {
                    _resetEmailSent.value = false
                }
            }
    }
}
