package com.capstone.aquamate

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.aquamate.api.ApiConfig
import com.capstone.aquamate.databinding.ActivityDetailFishDictionaryBinding
import com.capstone.aquamate.factory.DetailDictionaryFactory
import com.capstone.aquamate.viewmodel.DetailDictionaryViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class DetailFishDictionary : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFishDictionaryBinding
    private lateinit var viewModel: DetailDictionaryViewModel

    companion object {
        private const val DETAIL_DICTIONARY_TAG = "DetailDictionary"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFishDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get dictionaryId from intent
        val dictionaryId = intent.getStringExtra("dictionaryId")
        Log.d(DETAIL_DICTIONARY_TAG, "Dictionary ID: $dictionaryId")

        if (dictionaryId != null) {
            // Initialize ViewModel with custom factory
            viewModel = ViewModelProvider(
                this,
                DetailDictionaryFactory(ApiConfig.apiService)
            ).get(DetailDictionaryViewModel::class.java)

            // Fetch dictionary detail
            viewModel.getDictionaryDetail(dictionaryId)

            // Observe ViewModel data
            observeViewModel()
        } else {
            showToast("Invalid dictionary ID")
            Log.e(DETAIL_DICTIONARY_TAG, "Invalid dictionary ID")
            finish()
        }
    }

    private fun observeViewModel() {
        // Observe dictionary details
        viewModel.dictionaryDetail.observe(this, Observer { response ->
            if (response.status == true && response.data != null) {
                val data = response.data
                binding.fishName.text = data.fishName
                binding.fishLatinName.text = data.fishLatinName
                binding.deskripsiDetail.text = data.fishDesc
                loadImage(data.fishImage)
            } else {
                showToast("No data available")
            }
        })

        // Observe error messages
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            showToast(errorMessage)
            Log.e(DETAIL_DICTIONARY_TAG, "Error: $errorMessage")
        })
    }

    private fun loadImage(photoUrl: String?) {
        if (!photoUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(photoUrl)
                .into(binding.imgPhotos, object : Callback {
                    override fun onSuccess() {
                        Log.d(DETAIL_DICTIONARY_TAG, "Image loaded successfully")
                    }

                    override fun onError(e: Exception?) {
                        e?.printStackTrace()
                        Log.e(DETAIL_DICTIONARY_TAG, "Error loading image: ${e?.message}")
                    }
                })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
