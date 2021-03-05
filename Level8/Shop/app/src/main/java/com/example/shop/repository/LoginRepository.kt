package com.example.shop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.api.Api
import com.example.shop.api.ApiError
import com.example.shop.api.LoginApiService
import com.example.shop.model.User

class LoginRepository {
    private val loginApiService: LoginApiService = Api(LoginApiService::class.java).createService()

    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> get() = _user

    /**
     * Authenticate login
     */
    suspend fun login(email: String, password: String) {
        try {
            _user.value = loginApiService.login(email, password)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }
}