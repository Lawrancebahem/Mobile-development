package com.example.shop.ui.productPreview

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shop.R
import com.example.shop.databinding.ActivityProductPreviewBinding
import com.example.shop.model.Product
import com.example.shop.ui.main.adapter.ImageSliderAdapter
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import com.example.shop.utility.ImageConverter

class ProductPreviewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProductPreviewBinding

    private lateinit var imageSliderAdapter:ImageSliderAdapter

    private val advertisementViewModel: AdvertisementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    /**
     * Add the image slider plus the title, desc, date and price
     */
    private fun init(){

        val productId = intent.getSerializableExtra("selectedProductId") as Long

        //get the selected product
        getProduct(productId)

        advertisementViewModel.product.observe(this){
            val selectedProduct = it
            val bitmapList:ArrayList<Bitmap> = getBitmapArrayFromString(selectedProduct.images!!)
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
            if (selectedProduct.user!!.profilePicture != ""){
                val userPic = selectedProduct.user.profilePicture
                binding.userPic.setImageBitmap(ImageConverter.decode(userPic!!))
            }else{
                binding.userPic.setImageResource(R.drawable.profile_picture)
            }

            binding.vTit.text = selectedProduct.title
            binding.vDis.text = selectedProduct.description
            binding.vDate.text = selectedProduct.date

            (selectedProduct.user.firstName + " " + selectedProduct.user.lastName).also { binding.vUsNm.text = it }
            ("€"+selectedProduct.price.toString()).also { binding.vPrc.text = it }
        }

    }

    private fun getBitmapArrayFromString(base64List:ArrayList<String>):ArrayList<Bitmap>{
        val arrayList:ArrayList<Bitmap> = ArrayList()
        for (i in base64List){
            arrayList.add(ImageConverter.decode(i))
        }
        return arrayList
    }




    private fun getProduct(productId:Long){
        advertisementViewModel.getProduct(productId)
    }

}