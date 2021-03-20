package com.example.shop.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.api.ApiError
import com.example.shop.model.User
import com.example.shop.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val SUCCESS_MESSAGE = "You're account has been created successfully"

    private val registerRepository: RegisterRepository = RegisterRepository()

    private var _error: MutableLiveData<String> = MutableLiveData()
    private var _success: MutableLiveData<String> = MutableLiveData()

    val error: LiveData<String> = _error
    val success: LiveData<String> = _success
    val user: LiveData<User> = registerRepository.user

    val codeResendSuccessFully: LiveData<Boolean> = registerRepository.success

    /**
     * To create new user
     */
    fun createNewUser(user: User) {
        viewModelScope.launch {
            try {
                registerRepository.createNewUser(user)
                _success.value = SUCCESS_MESSAGE
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }


    /**
     * To resend a verification code
     */
    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            try {
                registerRepository.resendVerificationCode(email)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }

    /**
     * To update the profile
     */
    fun updateProfile(user: User) {

        viewModelScope.launch {
            try {
                registerRepository.updateProfile(user)
            } catch (ex: ApiError) {
                _error.value = ex.message
            }
        }
    }
}