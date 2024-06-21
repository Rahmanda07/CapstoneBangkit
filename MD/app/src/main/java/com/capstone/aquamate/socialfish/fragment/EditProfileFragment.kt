package com.capstone.aquamate.socialfish.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.capstone.aquamate.databinding.FragmentEditProfileBinding
import com.capstone.aquamate.socialfish.models.UserModel
import com.capstone.aquamate.socialfish.utils.USER_COLLECTION
import com.capstone.aquamate.socialfish.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private var selectedImageUri: Uri? = null
    private var savedFullname: String? = null
    private var savedBio: String? = null

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 101
        private const val STATE_FULLNAME = "savedFullname"
        private const val STATE_BIO = "savedBio"
        private const val STATE_IMAGE_URI = "selectedImageUri"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.btnChooseImage.setOnClickListener {
            saveCurrentData()
            selectImageFromGallery()
        }

        binding.btnSave.setOnClickListener {
            saveProfile()
        }

        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState)
            restoreImageUri(savedInstanceState)
        } else {
            loadUserProfile()
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_FULLNAME, savedFullname)
        outState.putString(STATE_BIO, savedBio)
        selectedImageUri?.let { uri ->
            outState.putString(STATE_IMAGE_URI, uri.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            savedFullname = it.getString(STATE_FULLNAME)
            savedBio = it.getString(STATE_BIO)
            binding.etFullname.setText(savedFullname)
            binding.etbio.setText(savedBio)

            val uriString = it.getString(STATE_IMAGE_URI)
            selectedImageUri = uriString?.let { Uri.parse(it) }
            selectedImageUri?.let { uri ->
                Picasso.get().load(uri).into(binding.ivProfile)
            }
        }
    }

    private fun saveCurrentData() {
        savedFullname = binding.etFullname.text.toString()
        savedBio = binding.etbio.text.toString()
    }

    private fun restoreSavedData(savedInstanceState: Bundle) {
        savedFullname = savedInstanceState.getString(STATE_FULLNAME)
        savedBio = savedInstanceState.getString(STATE_BIO)
    }

    private fun restoreImageUri(savedInstanceState: Bundle) {
        val uriString = savedInstanceState.getString(STATE_IMAGE_URI)
        selectedImageUri = uriString?.let { Uri.parse(it) }
    }

    private fun loadUserProfile() {
        val userId = Firebase.auth.currentUser?.uid ?: return

        binding.progressBar.visibility = View.VISIBLE

        Firebase.firestore.collection(USER_COLLECTION).document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(UserModel::class.java)
                user?.let {
                    binding.etUsername.setText(it.name)
                    if (savedFullname.isNullOrEmpty()) {
                        binding.etFullname.setText(it.fullname)
                    } else {
                        binding.etFullname.setText(savedFullname)
                    }
                    if (savedBio.isNullOrEmpty()) {
                        binding.etbio.setText(it.bio)
                    } else {
                        binding.etbio.setText(savedBio)
                    }
                    if (!it.image.isNullOrEmpty()) {
                        Picasso.get().load(it.image).into(binding.ivProfile)
                    }
                }

                // Hide progress bar
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                // Handle failure
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Failed to load user profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                Picasso.get().load(uri).into(binding.ivProfile)
            }
        }
    }

    private fun saveProfile() {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val name = binding.etUsername.text.toString()
        val fullname = binding.etFullname.text.toString()
        val bio = binding.etbio.text.toString()

        if (name.isEmpty() || fullname.isEmpty() || bio.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userUpdates = hashMapOf<String, Any>(
            "name" to name,
            "fullname" to fullname,
            "bio" to bio
        )

        binding.progressBar.visibility = View.VISIBLE

        if (selectedImageUri != null) {
            uploadImage(selectedImageUri!!, "profileImages") { imageUrl ->
                if (imageUrl != null) {
                    userUpdates["image"] = imageUrl
                    updateUserProfile(userId, userUpdates)
                } else {
                    // Hide progress bar on image upload failure
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            updateUserProfile(userId, userUpdates)
        }
    }

    private fun updateUserProfile(userId: String, userUpdates: Map<String, Any>) {
        Firebase.firestore.collection(USER_COLLECTION).document(userId)
            .update(userUpdates)
            .addOnSuccessListener {

                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                refreshProfileFragment()

                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->

                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun refreshProfileFragment() {
        val profileFragment = parentFragmentManager.findFragmentByTag("ProfileFragment") as? ProfileFragment
        profileFragment?.loadUserProfile()
    }
}