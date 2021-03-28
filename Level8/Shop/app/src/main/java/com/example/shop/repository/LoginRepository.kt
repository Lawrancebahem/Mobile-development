package com.example.shop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.api.Api
import com.example.shop.api.ApiError
import com.example.shop.api.LoginApiService
import com.example.shop.model.User
import retrofit2.Response
import retrofit2.http.Query

class LoginRepository {
    private val loginApiService: LoginApiService = Api(LoginApiService::class.java).createService()

    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> get() = _user

    private var _statusResponse:MutableLiveData<Int> = MutableLiveData()

    val statusResponse:LiveData<Int> = _statusResponse


    /**
     * Authenticate login
     */
    suspend fun login(email: String, password: String, randomCode:Int) {
        try {
            val response = loginApiService.login(email, password, randomCode)

            try {
                _user.value = response.body()
            }catch (e:Exception){

            }
            _statusResponse.value = response.code()
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

    /**
     * To reset the password
     */
    suspend fun resetPassword(email:String){
        try {
            val response = loginApiService.resetPassword(email)
            _statusResponse.value = response.code()
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

}