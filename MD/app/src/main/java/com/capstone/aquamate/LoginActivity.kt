package com.capstone.aquamate


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.capstone.aquamate.databinding.ActivityLoginBinding
import com.capstone.aquamate.factory.LoginViewModelFactory
import com.capstone.aquamate.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (loginViewModel.isLoggedIn()) {
            navigateToMain()
        }

        binding.daftarDisiniTextView.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Check email
            if (!binding.emailEditText.isValidEmail()) {
                binding.emailEditTextLayout.error = "Email tidak valid"
                return@setOnClickListener
            } else {
                binding.emailEditTextLayout.error = null
            }

            // Check password
            if (!binding.passwordEditText.isValidPassword()) {
                binding.passwordEditTextLayout.error = "Password harus memiliki setidaknya 8 karakter"
                return@setOnClickListener
            } else {
                binding.passwordEditTextLayout.error = null
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                loginViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.user.observe(this, Observer { user ->
            binding.progressBar.visibility = ProgressBar.GONE
            if (user != null) {
                navigateToMain()
            } else {
                Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}