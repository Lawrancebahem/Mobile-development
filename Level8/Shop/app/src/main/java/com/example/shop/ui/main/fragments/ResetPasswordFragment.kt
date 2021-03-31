package com.example.shop.ui.main.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.shop.R
import com.example.shop.api.Api
import com.example.shop.databinding.FragmentResetPasswordBinding
import com.example.shop.ui.main.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()

    private lateinit var binding: FragmentResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cnfirmBtn.setOnClickListener{
            onConfirmClick()
        }
    }


    /**
     * When the user clicks on confirm button
     */
    private fun onConfirmClick() {
        val password = binding.newPas.text.toString()
        val confirmPassword = binding.confPass.text.toString()
        if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                setFragmentResultListener(LoginFragment.KEY) { key, bundle ->
                    val array = bundle.get(LoginFragment.BUNDLE_KEY) as Array<String>
                    val email = array[0]
                    val code = array[1].toInt()
                    loginViewModel.setNewPassword(email, password, code)

                    loginViewModel.statusResponse.observe(viewLifecycleOwner) {
                        if (it == Api.ACCEPTED_CODE) {
                            binding.sucPas.visibility = View.VISIBLE
                            Handler().postDelayed(
                                {
                                    binding.sucPas.visibility = View.GONE
                                    findNavController().popBackStack()
                                },
                                2000 // value in milliseconds
                            )

                        } else if (it == Api.ERROR_CODE) {
                            showSnackBarMessage(getString(R.string.incorrectCode))
                        }
                    }
                }

            } else {
                showSnackBarMessage(getString(R.string.password_unequal))
            }
        } else {
            showSnackBarMessage(getString(R.string.invalidFields))
        }
    }

    fun showSnackBarMessage(message: kotlin.String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}