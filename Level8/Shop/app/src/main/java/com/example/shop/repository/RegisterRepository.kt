package com.example.shop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.api.Api
import com.example.shop.api.ApiError
import com.example.shop.api.RegisterApiService
import com.example.shop.model.User

class RegisterRepository {
    private val TAG = "RegisterRepository"
    private val registerApiService: RegisterApiService =
        Api(RegisterApiService::class.java).createService()

    private var _user: MutableLiveData<User> = MutableLiveData()
    private var _error:MutableLiveData<String> = MutableLiveData()

    val user: LiveData<User> get() = _user
    val error:LiveData<String> get() = _error

    /**
     * To create a user
     */
    suspend fun createNewUser(user: User) {
        try {
            val result = registerApiService.createNewUser(user)
            _user.value = result
        }catch (error:Throwable){
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

}