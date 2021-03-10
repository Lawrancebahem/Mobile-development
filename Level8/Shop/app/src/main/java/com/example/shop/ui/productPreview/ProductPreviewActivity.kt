package com.example.shop.ui.productPreview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.shop.R
import com.example.shop.databinding.ActivityProductPreviewBinding
import com.example.shop.model.Conversation
import com.example.shop.model.Message
import com.example.shop.model.Product
import com.example.shop.model.User
import com.example.shop.ui.chat.viewmodel.ChatViewModel
import com.example.shop.ui.main.adapter.ImageSliderAdapter
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.example.shop.utility.ImageConverter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@AndroidEntryPoint
class ProductPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductPreviewBinding
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var frommSmall: Animation
    private lateinit var selectedProduct: Product
    private val advertisementViewModel: AdvertisementViewModel by viewModels()
    private lateinit var currentUser: LiveData<User>

    private lateinit var userDatabaseViewModel: UserDatabaseViewModel

    //with AM,PM
//    val sdf = SimpleDateFormat("MMM-dd-yyyy HH:mm:ss aaa")


    //to format the date
    val sdf = SimpleDateFormat("MMM-dd-yyyy HH:mm:ss aaa")


    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        frommSmall = AnimationUtils.loadAnimation(this, R.anim.fromsmall)
        binding.mesgBox.alpha = 0f
        binding.overbox.alpha = 0f

        init()
    }


    /**
     * Add the image slider plus the title, desc, date and price
     */
    private fun init() {
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        currentUser = userDatabaseViewModel.userRepository.getUser()


        //when sending a message
        binding.msgBtn.setOnClickListener {
//            binding.mesgBox.animation = frommSmall
            onMessageClick()
        }

        binding.sendBtn.setOnClickListener{
            sendMessage()
        }

        //close the message box
        binding.cancBtn.setOnClickListener {
            hideMessageBox()
        }
        val productId = intent.getSerializableExtra("selectedProductId") as Long

        //get the selected product
        getProduct(productId)

        //to add the information of the product
        previewProduct()

    }


    private fun previewProduct(){

        advertisementViewModel.product.observe(this) {
            selectedProduct = it


            //hide the message button if the same user has added the product
            currentUser.observe(this){
                if (it.id == selectedProduct.user!!.id){
                    binding.msgBtn.isVisible = false
                }
            }


            val bitmapList: ArrayList<Bitmap> = getBitmapArrayFromString(selectedProduct.images!!)
            imageSliderAdapter = ImageSliderAdapter(bitmapList)
            binding.imageSlider.setSliderAdapter(imageSliderAdapter)


            //some configuration for the image slider
//        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.SWAP); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        binding.imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH;
//        binding.imageSlider.indicatorSelectedColor = Color.WHITE;
//        binding.imageSlider.indicatorUnselectedColor = Color.GRAY;
            binding.imageSlider.scrollTimeInSec = 5 //set scroll delay in seconds :
            binding.imageSlider.startAutoCycle()

            //set the profile picture of the user
            if (selectedProduct.user!!.profilePicture != "") {
                val userPic = selectedProduct.user!!.profilePicture
                binding.userPic.setImageBitmap(ImageConverter.decode(userPic!!))
            } else {
                binding.userPic.setImageResource(R.drawable.profile_picture)
            }

            binding.vTit.text = selectedProduct.title
            binding.vDis.text = selectedProduct.description
            binding.vDate.text = selectedProduct.date

            (selectedProduct.user!!.firstName + " " + selectedProduct.user!!.lastName).also {
                binding.vUsNm.text = it
            }
            ("€" + selectedProduct.price.toString()).also { binding.vPrc.text = it }
        }
    }

    private fun getBitmapArrayFromString(base64List: ArrayList<String>): ArrayList<Bitmap> {
        val arrayList: ArrayList<Bitmap> = ArrayList()
        for (i in base64List) {
            arrayList.add(ImageConverter.decode(i))
        }
        return arrayList
    }


    /**
     * To animate the send message box
     */
    private fun onMessageClick() {
        binding.mesgBox.alpha = 1f
        binding.overbox.alpha = 1f
        binding.mesgBox.startAnimation(frommSmall)

        binding.mgsUsNm.text =
            selectedProduct.user!!.firstName + " " + selectedProduct.user!!.lastName

        //set the profile picture of the user
        if (selectedProduct.user!!.profilePicture != "") {
            val userPic = selectedProduct.user!!.profilePicture
            binding.msgPc.setImageBitmap(ImageConverter.decode(userPic!!))
        } else {
            binding.msgPc.setImageResource(R.drawable.profile_picture)
        }
    }

    /**
     * Get the product from the database
     */
    private fun getProduct(productId: Long) {
        advertisementViewModel.getProduct(productId)
    }

    /**
     * To hide the message box
     */
    private fun hideMessageBox() {
        binding.overbox.alpha = 0f
        binding.mesgBox.alpha = 0f
    }


    private fun sendMessage() {
        val textMessage = binding.txtMsg.text.toString()
        //check if message empty
        if (textMessage.isEmpty()) {
            Snackbar.make(binding.mesgBox, getString(R.string.addMessage), Snackbar.LENGTH_SHORT).show()
            return
        }

//        get the current user from the database
//        Observe the user
        currentUser.observe(this ){sender ->

            val receiver = selectedProduct.user!!
            //make message and add the sender and receiver
            val message = Message(null, textMessage,sdf.format(Date()), currentUser.value!!.id, receiver.id,false)
            //add it into the database
            val conversation= Conversation(null,sender, receiver, arrayListOf(message))
            chatViewModel.addNewConversation(conversation)
            hideMessageBox()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}