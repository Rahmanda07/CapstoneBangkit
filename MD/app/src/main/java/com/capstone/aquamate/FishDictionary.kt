package com.capstone.aquamate

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aquamate.adapter.FishDictionaryAdapter
import com.capstone.aquamate.api.DataItemFishDictionary
import com.capstone.aquamate.databinding.ActivityFishDictionaryBinding
import com.capstone.aquamate.factory.DictionaryViewModelFactory
import com.capstone.aquamate.repository.DictionaryRepository
import com.capstone.aquamate.viewmodel.DictionaryViewModel

class FishDictionary : AppCompatActivity() {

    private lateinit var viewModel: DictionaryViewModel
    private lateinit var binding: ActivityFishDictionaryBinding
    private lateinit var adapter: FishDictionaryAdapter
    private var originalFishList = listOf<DataItemFishDictionary>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = DictionaryRepository()
        val factory = DictionaryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(DictionaryViewModel::class.java)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        binding.btnRefresh.setOnClickListener {
            Log.d("FishDictionary", "Refresh button clicked")
            Toast.makeText(this, "Refresh button clicked", Toast.LENGTH_SHORT).show()
            fetchDictionary()
        }

        fetchDictionary()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.searchbar_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterFishList(newText)
                return true
            }
        })
        return true
    }

    private fun setupRecyclerView() {
        adapter = FishDictionaryAdapter()
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@FishDictionary)
            addItemDecoration(DividerItemDecoration(this@FishDictionary, DividerItemDecoration.VERTICAL))
            adapter = this@FishDictionary.adapter
        }
    }

    private fun fetchDictionary() {
        Log.d("FishDictionary", "Fetching dictionary data")
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRefresh.visibility = View.GONE
        viewModel.getDictionaryFromApi()
    }

    private fun observeViewModel() {
        viewModel.dictionaryLiveData.observe(this) { fishList ->
            binding.progressBar.visibility = View.GONE
            if (fishList != null && fishList.isNotEmpty()) {
                Log.d("FishDictionary", "Data received: $fishList")
                originalFishList = fishList
                adapter.submitList(fishList)
                binding.btnRefresh.visibility = View.GONE
            } else {
                Log.e("FishDictionary", "No data received or empty list")
                binding.btnRefresh.visibility = View.VISIBLE
                Log.d("FishDictionary", "Refresh button visibility: VISIBLE")
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            binding.progressBar.visibility = View.GONE
            if (message != null) {
                Log.e("FishDictionary", "Error message: $message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                binding.btnRefresh.visibility = View.VISIBLE
                Log.d("FishDictionary", "Refresh button visibility: VISIBLE")
            }
        }
    }

    private fun filterFishList(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.submitList(originalFishList)
        } else {
            val filteredList = originalFishList.filter { fish ->
                fish.fishName?.contains(query, ignoreCase = true) ?: false
            }
            adapter.submitList(filteredList)
        }
    }
}

