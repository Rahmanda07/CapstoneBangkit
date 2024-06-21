package com.capstone.aquamate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.capstone.aquamate.databinding.ActivityForgotPasswordBinding
import com.capstone.aquamate.factory.ForgetPasswordViewModelFactory
import com.capstone.aquamate.viewmodel.ForgetPasswordViewModel

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels { ForgetPasswordViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.resetPasswordButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            if (!binding.emailInput.isValidEmail()) {
                binding.emailEditTextLayout.error = "Email tidak valid"
                return@setOnClickListener
            } else {
                binding.emailEditTextLayout.error = null
            }
            binding.progressBar.visibility = ProgressBar.VISIBLE

            forgetPasswordViewModel.resetPassword(email)
        }

        forgetPasswordViewModel.resetEmailSent.observe(this, Observer { resetEmailSent ->
            binding.progressBar.visibility = ProgressBar.GONE

            if (resetEmailSent) {
                Toast.makeText(this, "Reset email sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to send reset email", Toast.LENGTH_SHORT).show()
            }
        })
    }
}