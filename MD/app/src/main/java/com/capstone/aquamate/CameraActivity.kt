package com.capstone.aquamate

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.capstone.aquamate.databinding.ActivityCameraBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageUri: Uri? = null
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCamera()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.resolveActivity(packageManager)?.also {
            photoFile = try {
                createImageFile()
            } catch (ex: IOException) {
                showToast("Error occurred while creating the File")
                null
            }
            photoFile?.also {
                imageUri = FileProvider.getUriForFile(
                    this,
                    "com.capstone.aquamate.fileprovider",
                    it
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                takePictureLauncher.launch(cameraIntent)
            }
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            takePhoto()
        } else {
            showToast("Failed to capture image")
            finish()
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            imageUri = Uri.fromFile(this)
        }
    }

    private fun takePhoto() {
        imageUri?.let { uri ->
            val resultIntent = Intent().apply {
                data = uri
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        } ?: showToast("Failed to capture image")
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
