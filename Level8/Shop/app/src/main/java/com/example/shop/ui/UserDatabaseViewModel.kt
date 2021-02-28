package com.example.shop.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.myapplicationtest2.DAO.UserRepository
import com.example.myapplicationtest2.DAO.UserRoomDatabase

class UserDatabaseViewModel @ViewModelInject constructor(val userRepository: UserRepository):ViewModel()

