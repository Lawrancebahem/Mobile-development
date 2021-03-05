package com.example.shop.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.shop.utility.ImageConverter
import com.example.shop.R
import com.example.shop.databinding.FragmentRegsiterBinding
import com.example.shop.model.User
import com.example.shop.viewModel.RegisterViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegsiterBinding? = null
    private lateinit var bitmapImage: Bitmap
    private var filePath: Uri? = null

    private val registerViewModel: RegisterViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegsiterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }


    /**
     * Set event listeners on buttons
     */
    private fun init() {

        registerViewModel.success.observe(viewLifecycleOwner) {
            showToastMessage(it)
        }
        registerViewModel.error.observe(viewLifecycleOwner) {
            showToastMessage(it)
        }
        //on upload picture
        binding.uploadPictureBtn.setOnClickListener {
            startFileChooser()
        }

        binding.registerBtn.setOnClickListener {
            onRegister()
        }
    }

    /**
     * Handle register button click
     */
    private fun onRegister() {
        val user: User? = validateFields()
        if (user != null) {
            registerViewModel.createNewUser(user)

        } else {
            showToastMessage(R.string.invalidFields.toString())
        }
    }

    /**
     * When the user clicks to upload an image
     */
    private fun startFileChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 1111)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK && data != null) {
            filePath = data.data!!
            bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
            binding.imageView.load(bitmapImage)
        }
    }

    /**
     * To validate the fields
     */
    private fun validateFields(): User? {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        var profileImage = ""
        return when {
            firstName.isEmpty() -> null
            lastName.isEmpty() -> null
            email.isEmpty() -> null
            password.isEmpty() -> null
            else -> {
                if (filePath != null) {
                    profileImage = ImageConverter.encode(filePath!!, requireActivity(),null)
                }
                return User(0, firstName, lastName, email, password, profileImage)
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}