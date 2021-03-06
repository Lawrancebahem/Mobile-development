package com.example.shop.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotLoggedInViewModel:ViewModel(){
    var _info:MutableLiveData<String> = MutableLiveData()
    var _imageName:MutableLiveData<String> = MutableLiveData()

    val info:LiveData<String> get() =  _info
    val imageName:LiveData<String> get() = _imageName
}