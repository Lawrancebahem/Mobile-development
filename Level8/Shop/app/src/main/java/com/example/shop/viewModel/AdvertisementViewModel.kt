package com.example.shop.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.api.ApiError
import com.example.shop.model.Product
import com.example.shop.repository.AdvertisementRepository
import kotlinx.coroutines.launch

class AdvertisementViewModel : ViewModel() {

    var bitmapList: MutableLiveData<ArrayList<Bitmap>> = MutableLiveData()

    init {
        bitmapList.value = ArrayList()
    }

    private val advertisementRepository: AdvertisementRepository = AdvertisementRepository()
    private var _error: MutableLiveData<String> = MutableLiveData()

    val success: LiveData<Boolean> = advertisementRepository.success
    val error: LiveData<String> = _error

    val product: LiveData<Product> get() = advertisementRepository.product

    val productList:LiveData<ArrayList<Product>> =  advertisementRepository.productList

    //when user clicks on a certain product
    var selectedProductIndex:Int = 0

    /**
     * add product
     */
    fun getAllProducts() {
        viewModelScope.launch {
            try {
                advertisementRepository.getAllProducts()
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }


    /**
     * add product
     */
    fun insertProduct(product: Product) {
        viewModelScope.launch {
            try {
                advertisementRepository.insertProduct(product)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }


    /**
     * To add new product into the database
     */
    fun getProduct(id: Long) {
        viewModelScope.launch {
            try {
                advertisementRepository.getProduct(id)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * To remove a product based on the given id
     */
    fun removeProduct(id: Long) {
        viewModelScope.launch {
            try {
                advertisementRepository.removeProduct(id)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }
}