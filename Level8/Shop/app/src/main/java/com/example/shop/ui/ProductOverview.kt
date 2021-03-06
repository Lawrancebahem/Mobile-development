package com.example.shop.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shop.R
import com.example.shop.adapter.ImageSliderAdapter
import com.example.shop.databinding.ProductOverviewBinding
import com.example.shop.utility.ImageConverter
import com.example.shop.viewModel.AdvertisementViewModel

class ProductOverview : Fragment() {

    private lateinit var binding: ProductOverviewBinding

    private lateinit var imageSliderAdapter:ImageSliderAdapter

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = ProductOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    /**
     * Add the image slider plus the title, desc, date and price
     */
    private fun init(){
        val index =  advertisementViewModel.selectedProductIndex
        val selectedProduct = advertisementViewModel.productList.value!![index]
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


    private fun getBitmapArrayFromString(base64List:ArrayList<String>):ArrayList<Bitmap>{
        val arrayList:ArrayList<Bitmap> = ArrayList()
        for (i in base64List){
            arrayList.add(ImageConverter.decode(i))
        }
        return arrayList
    }
}