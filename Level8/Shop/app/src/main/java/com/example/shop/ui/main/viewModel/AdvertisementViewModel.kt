package com.example.shop.ui.main.viewModel

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.R
import com.example.shop.api.ApiError
import com.example.shop.model.Product
import com.example.shop.repository.AdvertisementRepository
import kotlinx.coroutines.launch

class AdvertisementViewModel : ViewModel() {


    var bitmapList: MutableLiveData<ArrayList<Bitmap>> = MutableLiveData()
    private lateinit var builder: AlertDialog.Builder

    init {
        bitmapList.value = ArrayList()
    }

    private val advertisementRepository: AdvertisementRepository = AdvertisementRepository()
    private var _error: MutableLiveData<String> = MutableLiveData()

    val success: LiveData<Boolean> = advertisementRepository.success
    val error: LiveData<String> = _error

    //for certain product (is used for previewing a product)
    val product: LiveData<Product> get() = advertisementRepository.product

    val productList: LiveData<ArrayList<Product>> = advertisementRepository.productList

    //when user clicks on a certain product
    var selectedProductIndex: Int = 0

    //user likes
    val userLikes: LiveData<Set<Product>> = advertisementRepository.userLikes

    //for the added products of user
    val userAddProducts: LiveData<List<Product>> = advertisementRepository.userAddProductsList

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


    /**
     * To insert a like, when the user clicks on the like button
     */
    fun insertLike(userId: Long, productId: Long) {
        viewModelScope.launch {
            try {
                advertisementRepository.insertLike(userId, productId)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * to get all liked products of a user
     */
    fun getUserLikes(id: Long) {

        viewModelScope.launch {
            try {
                advertisementRepository.getUserLikes(id)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * to get all add products of a user
     */
    fun getUserProducts(id: Long) {
        viewModelScope.launch {
            try {
                advertisementRepository.getUserProducts(id)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * to remove a like of user
     */
    fun removeLike(userId: Long, productId: Long) {
        viewModelScope.launch {
            try {
                advertisementRepository.removeLike(userId, productId)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * To create dialog builder and return for further additions
     */
     fun getConfirmationDialog(message: String, context:Context): AlertDialog.Builder {
        builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setCancelable(false)
            .setNegativeButton(context.getString(R.string.no)) { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        return builder
    }



    /**
     * To get all products that are based on the search key
     */
    fun getProductsBasedOnSearch(searchKey:String){
        viewModelScope.launch {
            try {
                advertisementRepository.getProductsBasedOnSearch(searchKey)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * To get all products that are based on the category value
     */
    fun getProductsBasedOnCategory(categoryId:Int){
        viewModelScope.launch {
            try {
                advertisementRepository.getProductsBasedOnCategory(categoryId)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }
}