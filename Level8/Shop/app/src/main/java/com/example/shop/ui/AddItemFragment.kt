package com.example.shop.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.adapter.ImageAdapter
import com.example.shop.adapter.SpinnerAdapter
import com.example.shop.databinding.FragmentAddItemBinding
import com.example.shop.viewModel.AdvertisementViewModel
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.ViewModelProvider
import com.example.shop.model.Category
import com.example.shop.model.Product
import com.example.shop.utility.ImageConverter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var builder:AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container, false)

        val itemsSpinner = ArrayList<Category>()
        for (i in Category.values()){
            itemsSpinner.add(i)
        }

        val adapterSpinner = SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, itemsSpinner)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryDrop.adapter = adapterSpinner


        binding.categoryDrop.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
//                if (itemsSpinner[0]getString(R.string.choose)){
//                    itemsSpinner.removeAt(0)
//                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        init()
    }



    @SuppressLint("ResourceAsColor")
    private fun init(){
        builder =  AlertDialog.Builder(requireContext())

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
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imageContainer.layoutManager = layoutManager
        binding.imageContainer.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()


        binding.addMore.setOnClickListener{
            findNavController().navigate(R.id.cameraFragment)
        }
        binding.postBtn.setOnClickListener{
            addProduct()
        }


        //observe error message
        advertisementViewModel.error.observe(viewLifecycleOwner){
            showSnackBarMessage(it)
        }
    }

    /**
     * To add the product with the related images to it, once the user clicks on post button
     */
    private fun addProduct(){
        val title:String = binding.title.text.toString()
        val description:String = binding.description.text.toString()
        val price:Double = binding.price.text.toString().toDouble()
        val category:Category = binding.categoryDrop.selectedItem as Category
        //validate fields
        if (validateFields(title,description, price)){
            CoroutineScope(Dispatchers.Main).launch{
                //get current user
                val user = withContext(Dispatchers.IO){
                    userDatabaseViewModel.userRepository.getUser()
                }
                val product = Product( id = 0,title = title,
                        description =  description, price = price, category = category,
                        user = user[0],images = getImagesInBase64())

                //add product to the database
                advertisementViewModel.insertProduct(product)

                //observe success message
                advertisementViewModel.success.observe(viewLifecycleOwner){
                    advertisementViewModel.bitmapList.value?.clear()
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }
    }

    /**
     * Validate the fields
     */
    private fun validateFields(title:String, description:String, price:Double):Boolean{

        return when{
            title.isEmpty() ->{
                showSnackBarMessage(getString(R.string.errorTitle))
                false
            }
            description.isEmpty() ->{
                showSnackBarMessage(getString(R.string.errorDescription))
                false
            }
            price.isNaN() || price <= 0 ->{
                showSnackBarMessage(getString(R.string.errorPrice))
                false
            }
            else-> true
        }
    }

    /**
     * get all images encoded into base64 from the bitmapList from  advertisementViewModel
     */
    private fun getImagesInBase64():ArrayList<String>{

        val bitmapList:ArrayList<String> = ArrayList()
        for (i in advertisementViewModel.bitmapList.value!!){
            val encoded:String = ImageConverter.encode(Uri.EMPTY, requireActivity(), i)
            bitmapList.add(encoded)
        }
        return bitmapList
    }

    private fun showSnackBarMessage(message:String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    }
    /**
     * When the user wants to leave
     */
    private fun onClose() {
        val alertBuilder = getDialog(getString(R.string.sureClose))
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitmapList.value!!.clear()
            imageAdapter.notifyDataSetChanged()
            findNavController().navigate(R.id.homeFragment)
        }
        alertBuilder.create()
        alertBuilder.show()
    }


    /**
     * When clicking on item to delete
     */
    private fun onDelete(index: Int){
        val alertBuilder = getDialog(getString(R.string.sureDelete))
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitmapList.value!!.removeAt(index)
            imageAdapter.notifyDataSetChanged()
        }
        alertBuilder.create()
        alertBuilder.show()

    }

    /**
     * To create dialog builder and return for further additions
     */
    private fun getDialog(message: String): AlertDialog.Builder {
        builder.setMessage(message)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        return builder
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onClose()
        return true
    }
}