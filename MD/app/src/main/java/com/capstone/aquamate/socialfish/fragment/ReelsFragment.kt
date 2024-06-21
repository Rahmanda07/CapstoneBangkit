package com.capstone.aquamate.socialfish.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.aquamate.databinding.FragmentReelsBinding
import com.capstone.aquamate.socialfish.adapter.AllReelViewAdapter
import com.capstone.aquamate.socialfish.utils.VIDIO_REEL
import com.example.aquamatesocialfish.models.ReelsUserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class ReelsFragment : Fragment() {

    private lateinit var reelsBinding: FragmentReelsBinding
    private var allReelList = ArrayList<ReelsUserModel>()
    private lateinit var reelAdapter: AllReelViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reelsBinding = FragmentReelsBinding.inflate(inflater, container, false)

        reelAdapter = AllReelViewAdapter(requireContext(), allReelList)
        reelsBinding.reelsViewPager.adapter = reelAdapter

        fetchReelsData()

        return reelsBinding.root
    }

    private fun fetchReelsData() {
        Firebase.firestore.collection(VIDIO_REEL).get()
            .addOnSuccessListener { querySnapshot ->
                val tempReelList = ArrayList<ReelsUserModel>()
                allReelList.clear()
                for (document in querySnapshot.documents) {
                    val reel = document.toObject<ReelsUserModel>()
                    reel?.let { tempReelList.add(it) }
                }

                tempReelList.reverse()
                allReelList.addAll(tempReelList)
                reelAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->

            }
    }
}