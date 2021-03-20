package com.example.shop.ui.camera.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
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
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.shop.R
import com.example.shop.databinding.FragmentAddItemBinding
import com.example.shop.ml.ModelTF
import com.example.shop.model.Category
import com.example.shop.model.Product
import com.example.shop.model.User
import com.example.shop.ui.camera.adapter.ImageAdapter
import com.example.shop.ui.camera.adapter.SpinnerAdapter
import com.example.shop.ui.main.MainActivity
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
    private lateinit var storage: FirebaseStorage

    private val fileName = "labels.txt"
    private lateinit var labelsString: String

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
        storage = FirebaseStorage.getInstance()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        currentUser = userDatabaseViewModel.userRepository.getUser()
        labelsString = requireActivity().application.assets.open(fileName).bufferedReader()
            .use { it.readText() }
        init()


        binding.kBtn.setOnClickListener{
            binding.overbox.visibility = View.GONE
            binding.categoryBox.visibility = View.GONE
        }
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
        imageAdapter = ImageAdapter(advertisementViewModel.bitMapList.value!!, ::onDelete)

        //adapter layout
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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



        //recognise an image of the viewModel images
        val sampleImage = advertisementViewModel.bitMapList.value!![0]
        recogniseImage(sampleImage)

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


                    val imagesUrl: ArrayList<String> = ArrayList()
                    //get images as hashmap with their random paths
                    val hashMap = getImagesAsByteArray()

                    for (i in hashMap) {
                        val fireImages: StorageReference = storage.getReference(i.key)
//                        val uploadTask: UploadTask = fireImages.putBytes(i.value)

                        fireImages.putBytes(i.value)
                            .addOnSuccessListener {
                                //this is the new way to do it
                                fireImages.downloadUrl.addOnCompleteListener { task ->
                                        val profileImageUrl = task.result.toString()
                                        imagesUrl.add(profileImageUrl)

                                        //if all images are processed
                                        if (imagesUrl.size == hashMap.size) {
                                            val product = Product(productId = 0, title = title, description = description,
                                                price = price, category = category, user = user, images = imagesUrl, date = sdf.format(Date()))

                                            //add product to the database
                                            advertisementViewModel.insertProduct(product)

                                            //observe success message
                                            advertisementViewModel.success.observe(
                                                viewLifecycleOwner
                                            ) {
                                                advertisementViewModel.bitMapList.value?.clear()
                                                navigateToHome()
                                            }
                                        }
                                    }
                            }
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
     * To get a hashMap with path of each image with its byteArray
     */
    private fun getImagesAsByteArray(): HashMap<String, ByteArray> {
        val byteArrayImage: HashMap<String, ByteArray> = HashMap()
        for (i in advertisementViewModel.bitMapList.value!!) {
            val outputStream = ByteArrayOutputStream()
            i.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val data: ByteArray = outputStream.toByteArray()
            val path: String = "fireimages/" + UUID.randomUUID() + ".png"
            byteArrayImage[path] = data
        }
        return byteArrayImage
    }


    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * When the user wants to leave
     */
    private fun onClose() {
        val alertBuilder = advertisementViewModel.getConfirmationDialog(
            getString(R.string.sureClose),
            requireContext()
        )
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitMapList.value!!.clear()
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
        val alertBuilder = advertisementViewModel.getConfirmationDialog(
            getString(R.string.sureDelete),
            requireContext()
        )
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitMapList.value!!.removeAt(index)
            imageAdapter.notifyDataSetChanged()
        }
        alertBuilder.create()
        alertBuilder.show()

//        recogniseImage(blob.toByteArray())
    }




    /**
     * Recognise an image based on the given byteArray
     */
    private fun recogniseImage(data: Bitmap) {
        val imageBitmap = TransformationUtils.rotateImage(data, 90)
        //set preview for the user

        val resizedBitmap: Bitmap = Bitmap.createScaledBitmap(imageBitmap, 224, 224, true)
        val model = ModelTF.newInstance(requireContext())
        val tbuffer = TensorImage.fromBitmap(resizedBitmap)
        val byteBuffer = tbuffer.buffer
        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val categoryList = labelsString.split("\n")
        val max = getMax(outputFeature0.floatArray)

        Handler().postDelayed(
            {
                binding.progressBar.visibility = View.INVISIBLE
            binding.catoInfo.text = getString(R.string.foundCategory,categoryList[max])
                binding.categoryDrop.setSelection(max)
            },
            2000 // value in milliseconds
        )
        // Releases model resources if no longer used.
        model.close()
    }


    /**
     * To get the max floating index, which is the index of the label
     */
    private fun getMax(array: FloatArray): Int {
        var index = 0;
        var min = 0.0f
        for (i in array.indices) {
            if (array[i] > min) {
                index = i
                min = array[i]
            }
        }
        return index
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onClose()
        return true
    }
}