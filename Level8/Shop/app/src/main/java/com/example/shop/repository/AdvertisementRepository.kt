package com.example.shop.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.api.AdvertisementApi
import com.example.shop.api.Api
import com.example.shop.api.ApiError
import com.example.shop.model.Product

class AdvertisementRepository {

    private val advertisementApi: AdvertisementApi =
        Api(AdvertisementApi::class.java).createService()


    private var _success: MutableLiveData<Boolean> = MutableLiveData()

    private val _productsList: MutableLiveData<ArrayList<Product>> = MutableLiveData()

    //for the home preview
    val productList: LiveData<ArrayList<Product>> get() = _productsList
    val success: LiveData<Boolean> get() = _success

    // for a certain returned product
    var product: MutableLiveData<Product> = MutableLiveData()

    //user likes
    private val _userLikes: MutableLiveData<Set<Product>> = MutableLiveData()
    val userLikes: LiveData<Set<Product>> = _userLikes


    private val _userAddProductsList:MutableLiveData<List<Product>> = MutableLiveData()
    val userAddProductsList:LiveData<List<Product>> = _userAddProductsList

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

    /**
     * To insert a like, when the user clicks on the like button
     */
    suspend fun insertLike(userId: Long, productId: Long) {

        try {
            _success.value = advertisementApi.insertLike(userId, productId)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

    /**
     * to get all liked products of a user
     */
    suspend fun getUserLikes(id: Long) {

        try {
            _userLikes.value = advertisementApi.getUserLikes(id)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

    /**
     * to get all add products of a user
     */
    suspend fun getUserProducts(id: Long) {
        Log.d("Has been called 117", "")
        try {
            _userAddProductsList.value = advertisementApi.getUserProducts(id)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

    /**
     * to remove a like of user
     */
    suspend fun removeLike(userId: Long, productId: Long) {
        try {
            _success.value = advertisementApi.removeLike(userId, productId)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }
}