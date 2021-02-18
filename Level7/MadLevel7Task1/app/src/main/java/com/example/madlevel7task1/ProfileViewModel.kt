package com.example.madlevel7task1

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"
    private val profileRepository: ProfileRepository = ProfileRepository()

    val profile: LiveData<Profile> = profileRepository.profile

    val createSuccess: LiveData<Boolean> = profileRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getProfile() {
        viewModelScope.launch {
            try {
                profileRepository.getProfile()
            } catch (ex: ProfileRepository.ProfileRetrievalError) {
                val errorMsg = "Something went wrong while retrieving profile"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun createProfile(firstName: String, lastName: String, description: String, imageUri: String) {
        // persist data to firestore
        val profile = Profile(firstName, lastName, description, imageUri)

        viewModelScope.launch {
            try {
                profileRepository.createProfile(profile)
            } catch (ex: ProfileRepository.ProfileSaveError) {
                val errorMsg = "Something went creating the profile"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }
}