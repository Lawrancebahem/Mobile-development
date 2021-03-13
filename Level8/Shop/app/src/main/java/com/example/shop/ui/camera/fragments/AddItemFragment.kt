package com.example.shop.ui.camera.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.databinding.FragmentAddItemBinding
import com.example.shop.model.Category
import com.example.shop.model.Product
import com.example.shop.model.User
import com.example.shop.ui.camera.adapter.ImageAdapter
import com.example.shop.ui.camera.adapter.SpinnerAdapter
import com.example.shop.ui.main.MainActivity
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.example.shop.utility.ImageConverter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AddItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private lateinit var userDatabaseViewModel: UserDatabaseViewModel

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var imageAdapter: ImageAdapter
    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()

    private lateinit var currentUser: LiveData<User>

    //with AM,PM
//    val sdf = SimpleDateFormat("MMM-dd-yyyy HH:mm:ss aaa")


    //to format the date
    val sdf = SimpleDateFormat("MMM-dd-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        currentUser = userDatabaseViewModel.userRepository.getUser()
        init()
    }

    @SuppressLint("ResourceAsColor")
    private fun init() {

        //fill the spinner
        val itemsSpinner = ArrayList<Category>()
        for (i in Category.values()) {
            itemsSpinner.add(i)
        }

        //set the adpater of the spinner
        val adapterSpinner = SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, itemsSpinner)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryDrop.adapter = adapterSpinner


        //change the home indicator
        val closeIcon = requireContext().resources.getDrawable(R.drawable.clear)
        closeIcon.setTint(R.color.black)
        val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(closeIcon)
        actionBar?.title = getString(R.string.adOverview)
        setHasOptionsMenu(true)

        //set the adapter
        imageAdapter = ImageAdapter(advertisementViewModel.bitmapList.value!!, ::onDelete)

        //adapter layout
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imageContainer.layoutManager = layoutManager
        binding.imageContainer.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()


        binding.addMore.setOnClickListener {
            findNavController().navigate(R.id.cameraFragment)
        }
        binding.postBtn.setOnClickListener {
            addProduct()
        }

        userDatabaseViewModel.userRepository.getUser()
        //observe error message
        advertisementViewModel.error.observe(viewLifecycleOwner) {
            showSnackBarMessage(it)
        }
    }

    /**
     * To add the product with the related images to it, once the user clicks on post button
     */
    private fun addProduct() {
        val title: String = binding.title.text.toString()
        val description: String = binding.description.text.toString()
        val price: Double = binding.price.text.toString().toDouble()
        val category: Category = binding.categoryDrop.selectedItem as Category
        //validate fields
        if (validateFields(title, description, price)) {
            CoroutineScope(Dispatchers.Main).launch {
                //get current user
                currentUser.observe(viewLifecycleOwner) { user ->
                    val product = Product(
                        productId = 0, title = title,
                        description = description, price = price, category = category,
                        user = user, images = getImagesInBase64(), date = sdf.format(Date())
                    )

                    //add product to the database
                    advertisementViewModel.insertProduct(product)

                    //observe success message
                    advertisementViewModel.success.observe(viewLifecycleOwner) {
                        advertisementViewModel.bitmapList.value?.clear()
                        navigateToHome()
                    }
                }
            }
        }
    }

    /**
     * Validate the fields
     */
    private fun validateFields(title: String, description: String, price: Double): Boolean {

        return when {
            title.isEmpty() -> {
                showSnackBarMessage(getString(R.string.errorTitle))
                false
            }
            description.isEmpty() -> {
                showSnackBarMessage(getString(R.string.errorDescription))
                false
            }
            price.isNaN() || price <= 0 -> {
                showSnackBarMessage(getString(R.string.errorPrice))
                false
            }
            else -> true
        }
    }

    /**
     * get all images encoded into base64 from the bitmapList from  advertisementViewModel
     */
    private fun getImagesInBase64(): ArrayList<String> {

        val bitmapList: ArrayList<String> = ArrayList()
        for (i in advertisementViewModel.bitmapList.value!!) {
            val encoded: String = ImageConverter.encode(Uri.EMPTY, requireActivity(), i)
            bitmapList.add(encoded)
        }
        return bitmapList
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    }

    /**
     * When the user wants to leave
     */
    private fun onClose() {
        val alertBuilder = advertisementViewModel.getConfirmationDialog(getString(R.string.sureClose), requireContext())
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitmapList.value!!.clear()
            imageAdapter.notifyDataSetChanged()
            navigateToHome()
//            findNavController().navigate(R.id.homeFragment)
        }
        alertBuilder.create()
        alertBuilder.show()
    }


    private fun navigateToHome() {
        val i = Intent(context, MainActivity::class.java)
        startActivity(i)
    }

    /**
     * When clicking on item to delete
     */
    private fun onDelete(index: Int) {
        val alertBuilder = advertisementViewModel.getConfirmationDialog(getString(R.string.sureDelete), requireContext())
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitmapList.value!!.removeAt(index)
            imageAdapter.notifyDataSetChanged()
        }
        alertBuilder.create()
        alertBuilder.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onClose()
        return true
    }
}