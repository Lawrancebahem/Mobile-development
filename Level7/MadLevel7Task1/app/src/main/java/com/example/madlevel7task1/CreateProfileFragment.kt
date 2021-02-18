package com.example.madlevel7task1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task1.databinding.FragmentCreateBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CreateProfileFragment : Fragment() {

    companion object {
        const val GALLERY_REQUEST_CODE = 100
    }

    private val viewModel: ProfileViewModel by activityViewModels()

    private var profileImageUri: Uri? = null
    private var _binding: FragmentCreateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnGallery.setOnClickListener {
            onGalleryClick()
        }
        binding.btnConfirm.setOnClickListener {
            onConfirmClick()
        }
    }


    private fun onGalleryClick() {
        // Create an Intent with action as ACTION_PICK
        val galleryIntent = Intent(Intent.ACTION_PICK)

        // Sets the type as image/*. This ensures only components of type image are selected
        galleryIntent.type = "image/*"

        // Start the activity using the gallery intent
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    profileImageUri = data?.data
                    binding.ivProfileImage.setImageURI(profileImageUri)
                }
            }
        }
    }



    private fun CharSequence?.ifNullOrEmpty(default: String) =
        if (this.isNullOrEmpty()) default else this.toString()

    private fun Uri?.ifNullOrEmpty() =
        this?.toString() ?: ""

    private fun onConfirmClick() {
        viewModel.createProfile(
            binding.etFirstName.text.ifNullOrEmpty(""),
            binding.etLastName.text.ifNullOrEmpty(""),
            binding.etProfileDescription.text.ifNullOrEmpty(""),
            profileImageUri.ifNullOrEmpty()
        )

        observeProfileCreation()

        findNavController().navigate(R.id.profileFragment)
    }

    private fun observeProfileCreation() {
        viewModel.createSuccess.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, R.string.successfully_created_profile, Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
        })

        viewModel.errorText.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}