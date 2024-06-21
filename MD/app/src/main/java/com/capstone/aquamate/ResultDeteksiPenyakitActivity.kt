package com.capstone.aquamate

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge

class ResultDeteksiPenyakitActivity : AppCompatActivity() {
    companion object {
        const val IMAGE_URI = "IMAGE_URI"
        const val RESULT_TEXT = "RESULT_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_deteksi_penyakit)
        enableEdgeToEdge()

        val imageUri = intent.getStringExtra(IMAGE_URI)
        val resultText = intent.getStringExtra(RESULT_TEXT)

        setImageFromUri(imageUri)

        resultText?.let {
            val splitText = it.split("\n")
            findViewById<TextView>(R.id.result_text).text = splitText.getOrNull(0) ?: ""
            findViewById<TextView>(R.id.explanation_text).text = splitText.getOrNull(1) ?: ""
        }
    }
    private fun setImageFromUri(uriString: String?) {
        uriString?.let { uriStr ->
            val imageUri = Uri.parse(uriStr)
            try {
                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                findViewById<ImageView>(R.id.result_image).setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("ResultDeteksiPenyakit", "Error loading image: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
