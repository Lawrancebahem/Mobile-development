package com.example.shop.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplicationtest2.DAO.UserRepository
import com.example.shop.R
import com.example.shop.databinding.FragmentLoginBinding
import com.example.shop.ui.main.viewModel.LoginViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel
    private lateinit var userRepository: UserRepository

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            onLogin()
        }

        binding.regBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        loginViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }

        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        userRepository = userDatabaseViewModel.userRepository

        loginViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        userRepository.logIn(it)
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }


    /**
     * On user login
     */
    private fun onLogin() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        loginViewModel.login(email, password)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}