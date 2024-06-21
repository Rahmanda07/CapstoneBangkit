package com.capstone.aquamate.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    fun logout() {
        auth.signOut()
        setLoginStatus(false)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun setLoginStatus(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }
}
