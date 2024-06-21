package com.capstone.aquamate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.aquamate.databinding.ActivitySignupBinding
import com.capstone.aquamate.socialfish.models.UserModel
import com.capstone.aquamate.socialfish.utils.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }

        binding.daftarDisiniTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val name = binding.usernameInput.editText?.text.toString()

            if (!binding.emailEditText.isValidEmail()) {
                binding.emailInput.error = "Email tidak valid"
                return@setOnClickListener
            }
            if (!binding.passwordEditText.isValidPassword()) {
                binding.passwordInput.error = "Password harus memiliki setidaknya 8 karakter"
                return@setOnClickListener
            } else {
                binding.passwordInput.error = null
            }

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                createUser(name, email, password)
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = ProgressBar.GONE
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    if (user != null) {
                        saveUserToFirestore(user.uid, name, email, password)
                    } else {
                        Toast.makeText(this, "Registration failed: user not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFirestore(uid: String, name: String, email: String, password: String) {
        val user = UserModel(name = name, email = email, password = password)
        Firebase.firestore.collection(USER_COLLECTION)
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                updateUI(auth.currentUser)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val TAG = "SIGNUP"
    }
}