package com.example.shop.ui.main.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.myapplicationtest2.DAO.UserRepository

class UserDatabaseViewModel @ViewModelInject constructor(val userRepository: UserRepository):ViewModel()

