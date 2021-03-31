package com.example.shop.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.api.ApiError
import com.example.shop.model.User
import com.example.shop.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loginRepository: LoginRepository = LoginRepository()
    private var _error: MutableLiveData<String> = MutableLiveData()

    val error: LiveData<String> = _error
    val user: LiveData<User> = loginRepository.user


    var statusResponse: LiveData<Int> = loginRepository.statusResponse


    /**
     * When logging, handle error as well
     */
    fun login(email: String, password: String, randomCode: Int) {
        viewModelScope.launch {
            try {
                loginRepository.login(email, password, randomCode)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * To reset the password
     */
    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                loginRepository.resetPassword(email)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * To check the reset password code
     */
    fun checkResetPasswordCode(email: String, randomCode: Int) {
        viewModelScope.launch {
            try {
                loginRepository.checkResetPasswordCode(email, randomCode)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }


    /**
     * To set a new password
     */
    fun setNewPassword(email: String, password: String, randomCode: Int) {
        viewModelScope.launch {
            try {
                loginRepository.setNewPassword(email, password, randomCode)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }
}