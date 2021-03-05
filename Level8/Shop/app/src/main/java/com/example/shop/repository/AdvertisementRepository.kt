package com.example.shop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.api.AdvertisementApi
import com.example.shop.api.Api
import com.example.shop.api.ApiError
import com.example.shop.model.Product

class AdvertisementRepository {

    private val advertisementApi: AdvertisementApi = Api(AdvertisementApi::class.java).createService()


    private var _product: MutableLiveData<Product> = MutableLiveData()
    private var _success: MutableLiveData<Boolean> = MutableLiveData()

    private val _productsList:MutableLiveData<ArrayList<Product>> = MutableLiveData()

    val productList:LiveData<ArrayList<Product>> get () =_productsList
    val success: LiveData<Boolean> get() = _success

    var product: MutableLiveData<Product> = MutableLiveData()



    /**
     * To get all products from the database
     */
    suspend fun getAllProducts() {
        try {
            _productsList.value = advertisementApi.getAllProducts()
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }
    /**
     * To add new product into the database
     */
    suspend fun insertProduct(product: Product) {
        try {
            _success.value = advertisementApi.insertProduct(product)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }


    /**
     * To add new product into the database
     */
    suspend fun getProduct(id: Long) {
        try {
            product.value = advertisementApi.getProduct(id)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }


    /**
     * To remove a product based on the given id
     */
    suspend fun removeProduct(id: Long) {
        try {
            _success.value = advertisementApi.removeProduct(id)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }
}