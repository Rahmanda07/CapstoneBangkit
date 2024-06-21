package com.capstone.aquamate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.capstone.aquamate.databinding.ActivityDeteksiPenyakitIkanBinding
import com.capstone.aquamate.factory.PredictViewModelFactory
import com.capstone.aquamate.progressrequest.ProgressRequestBody
import com.capstone.aquamate.repository.PredictRepository
import com.capstone.aquamate.viewmodel.PredictViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class DeteksiPenyakitIkanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeteksiPenyakitIkanBinding
    private var currentImageUri: Uri? = null
    private lateinit var progressIndicator: LinearProgressIndicator

    private val repository = PredictRepository()
    private val predictViewModel: PredictViewModel by viewModels {
        PredictViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeteksiPenyakitIkanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressIndicator = binding.progressIndicator

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener {
            currentImageUri?.let {
                progressIndicator.visibility = View.VISIBLE
                uploadImage()
            } ?: showToast(getString(R.string.image_classifier_failed))
        }
        binding.cameraButton.setOnClickListener { startCamera() }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                currentImageUri = uri
                showImage()
            } ?: showToast("Failed to get image URI")
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let { uri ->
                currentImageUri = uri
                showImage()
            } ?: showToast("Failed to get image URI")
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            Log.d(TAG, "Displaying image: $uri")
            binding.previewImageView.setImageURI(uri)
        } ?: Log.d(TAG, "No image to display")
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage(this)

            Log.d(TAG, "Reduced Image File Size: ${imageFile.length()} bytes")
            Log.d(TAG, "Image File Path: ${imageFile.absolutePath}")

            val requestFile = ProgressRequestBody(imageFile) { progress ->
                lifecycleScope.launch {
                    progressIndicator.progress = progress
                }
            }

            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            Log.d(TAG, "Request File: ${requestFile.contentType()}")
            Log.d(TAG, "Multipart Body: ${body.body.contentLength()} bytes")

            progressIndicator.visibility = LinearProgressIndicator.VISIBLE

            lifecycleScope.launch {
                predictViewModel.uploadImage(body).observe(this@DeteksiPenyakitIkanActivity, Observer { response ->
                    progressIndicator.visibility = LinearProgressIndicator.GONE
                    response?.let {
                        Log.d(TAG, "Response: ${it.result}")
                        val resultText = "Result: ${it.result}\nExplanation: ${it.explanation}"
                        binding.resultDeteksiPenyakitTextView.text = resultText
                        navigateToResultActivity(it.result, it.explanation, uri.toString())                    } ?: run {
                        showToast("Failed to get prediction result")
                    }
                })
            }
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun navigateToResultActivity(result: String?, explanation: String?, imageUri: String) {
        val intent = Intent(this, ResultDeteksiPenyakitActivity::class.java).apply {
            putExtra(ResultDeteksiPenyakitActivity.IMAGE_URI, imageUri)
            putExtra(ResultDeteksiPenyakitActivity.RESULT_TEXT, "$result\n$explanation")
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "ImagePicker"
    }
}
