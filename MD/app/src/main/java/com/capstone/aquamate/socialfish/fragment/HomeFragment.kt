package com.capstone.aquamate.socialfish.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.aquamate.databinding.FragmentHomeBinding
import com.capstone.aquamate.socialfish.adapter.AllPostAdapter
import com.capstone.aquamate.socialfish.models.UserModel
import com.capstone.aquamate.socialfish.utils.FOLLOW_USER
import com.capstone.aquamate.socialfish.utils.POST
import com.example.aquamatesocialfish.models.PostUserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var homebinding: FragmentHomeBinding
    private var allPostList = ArrayList<PostUserModel>()
    private lateinit var postAdapter: AllPostAdapter
    private var followStoryList = ArrayList<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homebinding = FragmentHomeBinding.inflate(inflater, container, false)
        val swipeRefreshLayout = homebinding.swipeRefreshLayout

        postAdapter = AllPostAdapter(requireContext(), allPostList)
        homebinding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        homebinding.rvHome.adapter = postAdapter

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(homebinding.materialToolbarHome)

        fetchFollowStories()

        fetchAllPosts()


        swipeRefreshLayout.setOnRefreshListener {
            fetchAllPosts()
            fetchFollowStories()
            swipeRefreshLayout.isRefreshing = false  // Hide refreshing animation
        }

        return homebinding.root
    }

    private fun fetchFollowStories() {
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW_USER).get()
            .addOnSuccessListener { querySnapshot ->
                val storyTempList = ArrayList<UserModel>()
                followStoryList.clear()
                for (document in querySnapshot.documents) {
                    val storyUser = document.toObject<UserModel>()
                    Log.d("HomeFragment", "Fetched story user: ${storyUser.toString()}")
                    storyUser?.let { storyTempList.add(it) }
                }
                followStoryList.addAll(storyTempList)
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error fetching follow stories", exception)
            }
    }

    private fun fetchAllPosts() {
        Firebase.firestore.collection(POST).get()
            .addOnSuccessListener { querySnapshot ->
                val postTempList = ArrayList<PostUserModel>()
                allPostList.clear()
                for (document in querySnapshot.documents) {
                    val post = document.toObject<PostUserModel>()
                    Log.d("HomeFragment", "Fetched post: ${post.toString()}")
                    post?.let { postTempList.add(it) }
                }
                allPostList.addAll(postTempList)
                postAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error fetching posts", exception)
            }
    }
}