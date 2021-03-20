package com.example.shop.ui.profile.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.example.shop.R
import com.example.shop.databinding.FragmentProfileBinding
import com.example.shop.model.User
import com.example.shop.ui.main.MainActivity
import com.example.shop.ui.main.viewModel.RegisterViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel
    private lateinit var currentUser: LiveData<User>
    private lateinit var binding: FragmentProfileBinding

    private val registerViewModel: RegisterViewModel by activityViewModels()
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        binding.myProfileInclude.okBtn.setOnClickListener {
            binding.myProfileInclude.overbox.visibility = View.GONE
            binding.myProfileInclude.infoBox.visibility = View.GONE
            navigateToLogin()
        }
    }


    /**
     * To add the user's information
     */
    private fun init() {
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        currentUser = userDatabaseViewModel.userRepository.getUser()

        currentUser.observe(viewLifecycleOwner) { user ->

            if (user != null) {
                if (user.profilePicture != "") {
                    Glide.with(this).load(user.profilePicture)
                        .into(binding.myProfileInclude.imageView)
                } else {
                    binding.myProfileInclude.imageView.setImageResource(R.drawable.profile_picture)
                }
                binding.myProfileInclude.firstName.setText(user.firstName)
                binding.myProfileInclude.lastName.setText(user.lastName)
                binding.myProfileInclude.email.setText(user.email)
                binding.myProfileInclude.registerBtn.text = getString(R.string.update)

                binding.logout.setOnClickListener {
                    logout()
                }
                storage = FirebaseStorage.getInstance()
            }
        }

        binding.myProfileInclude.registerBtn.setOnClickListener{
            updateProfile()
        }


        //If the user is updated, update it in the database
        registerViewModel.user.observe(viewLifecycleOwner){
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO){
                    userDatabaseViewModel.userRepository.update(it)
                }
            }
        }


        binding.myProfileInclude.uploadPictureBtn.setOnClickListener{
            startFileChooser()
        }
    }

    /**
     * To handle the click button on update
     */
    private fun updateProfile() {

        //if not all fields are filled in show a snackbar message
        if (!validateFields()){
            Snackbar.make(binding.root, getString(R.string.invalidFields), Snackbar.LENGTH_SHORT).show()
            //if the email is different than the old one, show that there will be email sent to this email to verify it
        }else if(!currentUser.value!!.email.equals(binding.myProfileInclude.email.text.toString(), ignoreCase = true)){
            binding.myProfileInclude.overbox.visibility = View.VISIBLE
            binding.myProfileInclude.infoBox.visibility = View.VISIBLE
        }else{
            Toast.makeText(context,getString(R.string.success), Toast.LENGTH_SHORT).show()
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
            Glide.with(requireContext()).load(filePath).into(binding.myProfileInclude.imageView)
        }
    }


    /**
     * To validate the fields
     */
    private fun validateFields(): Boolean {
        val firstName = binding.myProfileInclude.firstName.text.toString()
        val lastName = binding.myProfileInclude.lastName.text.toString()
        val email = binding.myProfileInclude.email.text.toString()
        val password = binding.myProfileInclude.password.text.toString()
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
                            val user = User(currentUser.value!!.id, firstName, lastName, email, password, profileImageUrl)
                            registerViewModel.updateProfile(user)
                        }
                    }
                    return true
                } else {
                    //We send the user to database without profile image
                    val user = User(currentUser.value!!.id, firstName, lastName, email, password, profileImage)
                    registerViewModel.updateProfile(user)
                    return true
                }
            }
        }

    }



    /**
     * WHen the user's clicks on logout
     */
    private fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                userDatabaseViewModel.userRepository.logOut(currentUser.value!!)
            }
            navigateToLogin()
        }
    }

    /**
     * To navigate to login page
     */
    private fun navigateToLogin(){
        val actv = Intent(requireContext(), MainActivity::class.java)
        actv.putExtra("destination",R.id.loginFragment)
        startActivity(actv)
        requireActivity().overridePendingTransition(0, 0)
        requireActivity().finish()

    }
}