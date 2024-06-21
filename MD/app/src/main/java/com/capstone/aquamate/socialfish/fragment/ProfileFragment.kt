package com.capstone.aquamate.socialfish.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.aquamate.R
import com.capstone.aquamate.databinding.FragmentProfileBinding
import com.capstone.aquamate.socialfish.models.UserModel
import com.capstone.aquamate.socialfish.utils.USER_COLLECTION
import com.example.aquamatesocialfish.adapter.ViewPagerAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.editProfileBtn.setOnClickListener {
            navigateToEditProfile()
        }

        binding.progressBar.visibility = View.VISIBLE

        // Set up ViewPager and TabLayout
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.AddFragment(MyPostFragment(), "My Posts")
        viewPagerAdapter.AddFragment(MyReelsFragment(), "My Reels")
        binding.viewPagerView.adapter = viewPagerAdapter
        binding.tablayoutView.setupWithViewPager(binding.viewPagerView)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    fun loadUserProfile() {
        Firebase.firestore.collection(USER_COLLECTION)
            .document(Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val userAquamate = documentSnapshot.toObject<UserModel>()
                if (userAquamate != null) {
                    binding.usernameTxt.text = userAquamate.name
                    binding.fullnameTxt.text = userAquamate.fullname
                    binding.BioTxt.text = userAquamate.bio

                    if (userAquamate.name.isNullOrEmpty() || userAquamate.fullname.isNullOrEmpty() || userAquamate.bio.isNullOrEmpty()) {
                        navigateToEditProfile()
                    } else {
                        if (!userAquamate.image.isNullOrEmpty()) {
                            Picasso.get().load(userAquamate.image).into(binding.ivProfile)
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun navigateToEditProfile() {
        val editProfileFragment = EditProfileFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragment_container, editProfileFragment)
        transaction.commit()
    }
}