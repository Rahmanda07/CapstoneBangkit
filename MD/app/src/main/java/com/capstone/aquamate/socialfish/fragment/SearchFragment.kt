package com.capstone.aquamate.socialfish.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aquamate.databinding.FragmentSearchBinding
import com.capstone.aquamate.socialfish.models.UserModel
import com.capstone.aquamate.socialfish.utils.USER_COLLECTION
import com.example.aquamatesocialfish.adapter.SearchUserAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {

    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchUserAdapter
    private var searchList = ArrayList<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        searchBinding.rvSearchUser.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter = SearchUserAdapter(requireContext(), searchList)
        searchBinding.rvSearchUser.adapter = searchAdapter

        loadAllUsers()

        searchBinding.searchViewUsername.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchUsersByName(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isEmpty()) {
                        loadAllUsers()
                    } else {
                        searchUsersByName(it)
                    }
                }
                return true
            }
        })

        return searchBinding.root
    }

    private fun loadAllUsers() {
        Firebase.firestore.collection(USER_COLLECTION)
            .limit(7)
            .get()
            .addOnSuccessListener { result ->
                searchList.clear()
                for (document in result.documents) {
                    val user = document.toObject<UserModel>()
                    if (user != null && document.id != Firebase.auth.currentUser?.uid) {
                        searchList.add(user)
                    }
                }
                searchAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("SearchFragment", "Error getting users: ", exception)
            }
    }

    private fun searchUsersByName(name: String) {
        Firebase.firestore.collection(USER_COLLECTION)
            .whereGreaterThanOrEqualTo("name", name)
            .whereLessThanOrEqualTo("name", name + '\uf8ff')
            .limit(7)
            .get()
            .addOnSuccessListener { result ->
                searchList.clear()
                for (document in result.documents) {
                    val user = document.toObject<UserModel>()
                    if (user != null && document.id != Firebase.auth.currentUser?.uid) {
                        searchList.add(user)
                    }
                }
                searchAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("SearchFragment", "Error searching users: ", exception)
            }
    }

    companion object {

    }
}