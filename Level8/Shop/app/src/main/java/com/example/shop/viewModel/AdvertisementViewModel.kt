package com.example.shop.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdvertisementViewModel:ViewModel(){

    var bitmapList:MutableLiveData<ArrayList<Bitmap>> = MutableLiveData()

    init {
        bitmapList.value = ArrayList()
    }

}