package com.capstone.aquamate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.capstone.aquamate.api.recomendation
import com.capstone.aquamate.databinding.ActivityDetailRecomendationBinding

class DetailRecomendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecomendationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailRecomendationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        try {
            val recomendation: recomendation? = intent.getParcelableExtra("key_recomendation")
            if (recomendation != null) {
                binding.tvDetailNama.text = recomendation.name
                binding.TvDetailDescription.text = recomendation.description
                binding.tvDetailFoto.setImageResource(recomendation.photo)
            } else {
                Log.e("DetailActivity", "Error l")
            }
        } catch (e: Exception) {
            Log.e("DetailActivity", "Error: ${e.message}")
            e.printStackTrace()
        }

    }
}