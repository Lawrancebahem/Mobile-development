package com.example.shop.ui.main.fragments

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
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.shop.R
import com.example.shop.databinding.FragmentRegsiterBinding
import com.example.shop.model.User
import com.example.shop.ui.main.viewModel.RegisterViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegsiterBinding? = null
    private lateinit var bitmapImage: Bitmap
    private var filePath: Uri? = null

    private val registerViewModel: RegisterViewModel by activityViewModels()


    private lateinit var userDatabaseViewModel: UserDatabaseViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegsiterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.title = getString(R.string.register)
        storage = FirebaseStorage.getInstance()
    }

    /**
     * Set event listeners on buttons
     */
    private fun init() {

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

        binding.okBtn.setOnClickListener {
            binding.overbox.visibility = View.GONE
            binding.infoBox.visibility = View.GONE
            findNavController().navigate(R.id.loginFragment)
        }

        binding.resendCode.setOnClickListener {
            val email = binding.email.text.toString()
            registerViewModel.resendVerificationCode(email)

            registerViewModel.codeResendSuccessFully.observe(viewLifecycleOwner) {
                showToastMessage(getString(R.string.resendCodeSuccess))
            }
        }

        registerViewModel.success.observe(viewLifecycleOwner) {
            binding.overbox.visibility = View.VISIBLE
            binding.infoBox.visibility = View.VISIBLE
        }
    }

    /**
     * Handle register button click
     */
    private fun onRegister() {
        val isValid = validateFields()
        if (!isValid) {
            showToastMessage(getString(R.string.invalidFields))
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
    private fun validateFields(): Boolean {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val profileImage = ""
        return when {
            firstName.isEmpty() -> false
            lastName.isEmpty() -> false
            email.isEmpty() -> false
            password.isEmpty() -> false
            else -> {
                if (filePath != null) {
                    val path: String = "fireimages/" + UUID.randomUUID() + ".png" // path image
                    val fireImages: StorageReference = storage.getReference(path)
                    // wee need to add the picture to firebase sotrage
                    fireImages.putFile(filePath!!).addOnSuccessListener {
                        //this is the new way to do it
                        fireImages.downloadUrl.addOnCompleteListener {
                            val profileImageUrl = it.result.toString()
                            val user = User(0, firstName, lastName, email, password, profileImageUrl)
                            registerViewModel.createNewUser(user)
                        }
                    }
                    return true
                } else {
                    //We send the user to database without profile image
                    val user = User(0, firstName, lastName, email, password, profileImage)
                    registerViewModel.createNewUser(user)
                    return true
                }
            }
        }
    }

    /**
     * To show a toast message
     */
    private fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}