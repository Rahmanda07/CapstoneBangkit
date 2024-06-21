package com.capstone.aquamate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.aquamate.adapter.RecomendationAdapter
import com.capstone.aquamate.api.recomendation
import com.capstone.aquamate.databinding.ActivityMainBinding
import com.capstone.aquamate.factory.MainViewModelFactory
import com.capstone.aquamate.socialfish.view.HomeActivity
import com.capstone.aquamate.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvRecomendation: RecyclerView
    private lateinit var recomendationAdapter: RecomendationAdapter
    private val list = ArrayList<recomendation>()
    private val mainViewModel: MainViewModel by viewModels { MainViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvRecomendation = binding.rvRecomndation
        rvRecomendation.setHasFixedSize(true)

        list.addAll(getListRecomendation())
        showRecyclerList()

        if (!mainViewModel.isLoggedIn()) {
            navigateToLogin()
        }

        binding.gambar.setImageBitmap(resizeBitmap(R.drawable.poster_aquamate, 800, 800))
        binding.fishPrediction.setImageBitmap(resizeBitmap(R.drawable.fish_scanning, 80, 80))
        binding.fishDictionary.setImageBitmap(resizeBitmap(R.drawable.fish_dictionary, 80, 80))
        binding.socialFish.setImageBitmap(resizeBitmap(R.drawable.social_fish, 80, 80))

        binding.fishPrediction.setOnClickListener {
            val intent = Intent(this, DeteksiPenyakitIkanActivity::class.java)
            startActivity(intent)
        }

        binding.fishDictionary.setOnClickListener {
            val intent = Intent(this, FishDictionary::class.java)
            startActivity(intent)
        }

        binding.keluar.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.FishMaps.setOnClickListener {
            openMaps()
        }

        binding.socialFish.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Logout")
        builder.setMessage("Apakah Anda yakin ingin logout?")
        builder.setPositiveButton("Ya") { dialog, _ ->
            mainViewModel.logout()
            navigateToLogin()
            Toast.makeText(this, "Anda telah logout", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        if (!mainViewModel.isLoggedIn()) {
            navigateToLogin()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Function to resize the bitmap
    private fun resizeBitmap(resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, resId, options)
    }

    // Function to calculate the inSampleSize value
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun getListRecomendation(): ArrayList<recomendation> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listRecomendation = ArrayList<recomendation>()
        for (i in dataName.indices) {
            val recomendation =
                recomendation(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listRecomendation.add(recomendation)
        }
        return listRecomendation
    }

    private fun showRecyclerList() {
        rvRecomendation.layoutManager = LinearLayoutManager(this)
        recomendationAdapter = RecomendationAdapter(list)
        rvRecomendation.adapter = recomendationAdapter
        recomendationAdapter.setOnItemClickCallback(object :
            RecomendationAdapter.OnItemClickCallback {
            override fun onItemClicked(data: recomendation) {
                showSelectedRecomendation(data)
            }
        })
    }

    private fun showSelectedRecomendation(recomendation: recomendation) {
        val intentDetail = Intent(this, DetailRecomendationActivity::class.java)
        intentDetail.putExtra("key_recomendation", recomendation)
        startActivity(intentDetail)
    }

    private fun openMaps() {
        val gmmIntentUri = Uri.parse("geo:0,0?q=penjualan ikan terdekat")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Log.e("MainActivity", "Tidak ada aplikasi yang dapat menangani intent ini.")
            Toast.makeText(this, "Tidak ada aplikasi yang dapat menangani intent ini.", Toast.LENGTH_LONG).show()
        }
    }
}
