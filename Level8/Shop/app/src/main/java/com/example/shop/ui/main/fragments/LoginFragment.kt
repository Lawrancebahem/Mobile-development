package com.example.shop.ui.main.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplicationtest2.DAO.UserRepository
import com.example.shop.R
import com.example.shop.api.Api
import com.example.shop.api.ApiError
import com.example.shop.dao.RandomCodeRepository
import com.example.shop.databinding.FragmentLoginBinding
import com.example.shop.model.RandomCode
import com.example.shop.ui.main.viewModel.LoginViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.String
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var randomCodeTimeRepository: RandomCodeRepository
    private lateinit var randomCodeTime: LiveData<RandomCode>
    private lateinit var timesTamp: Timestamp

    private val JUST_LOGIN = 0
    private val RESEND_CODE = -1

    private val EXPIRATION_TIME = 15

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

        val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.title = getString(R.string.login)
        binding.loginBtn.setOnClickListener {
            onLogin(JUST_LOGIN)
        }
        binding.rsnBtn.setOnClickListener {
            onLogin(RESEND_CODE)
        }

        binding.regBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // on forgot button click
        binding.forgoBtn.setOnClickListener{
            binding.resetBox.visibility = View.VISIBLE
            binding.overbox.visibility = View.VISIBLE
        }

        //on reset button click
        binding.reset.setOnClickListener{
            onResetPassword()
        }

        loginViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }
        //database instance
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        userRepository = userDatabaseViewModel.userRepository // userRepository database
        randomCodeTimeRepository = userDatabaseViewModel.randomCodeRepository // randomCodeRepository database
        randomCodeTime = randomCodeTimeRepository.getResendCode()


        //if the random code time is already in the room database, show the input fields of random code and the resend button
        randomCodeTime.observe(viewLifecycleOwner) {
            if (it != null) {
                countdown(it.timesTamp)
            }
        }


        // handle login response status
        loginViewModel.statusResponse.observe(viewLifecycleOwner){status->
            if (status == Api.OK_CODE){
                //Check if there is no code sent, that is stored in the room database
                randomCodeTime.observe(viewLifecycleOwner) {
                    if (it == null) {
                        timesTamp = calculateExpireType()
                        val newRandomCode = RandomCode(null, timesTamp)
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.IO) {
                                randomCodeTimeRepository.insertResendCode(newRandomCode)
                            }
                            timesTamp = calculateExpireType()
                            countdown(timesTamp)
                        }
                    }
                }
            }else if (status ==  Api.ERROR_CODE){
                showSnackBarMessage(getString(R.string.incorrectCode))
            }else if (status ==  Api.ACCEPTED_CODE){
                val user = loginViewModel.user.value
                // when the user is returned, this means the verification is done and the user is allowed to log in
                if (user?.verificationToken?.token != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            userRepository.logIn(user)
                        }
                        deleteRandomCodeFormRoomDatabase()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }else if (status == Api.NOT_VERIFIED){
                showSnackBarMessage(getString(R.string.verifyEmail))
            }else if (status == Api.UNAUTHORIZED){
                showSnackBarMessage(getString(R.string.email_password_incorrect))
            }else if (status == Api.NOT_FOUND){
                showSnackBarMessage(getString(R.string.email_unknown))
            }
        }

    }

    /**
     * On user login
     */
    private fun onLogin(code: Int) {

        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showSnackBarMessage(getString(R.string.invalidFields))
            return
        }
        var randomCode = code
        //Check if the random code field is empty
        if (binding.rndomCode.text!!.isNotEmpty()) {
            randomCode = binding.rndomCode.text.toString().toInt()
        } else if (binding.randomCodeInput.visibility == View.VISIBLE) {
            showSnackBarMessage(getString(R.string.verificationCodeRequired))
            return
        }

        loginViewModel.login(email, password, randomCode)


    }

    /**
     * To show the user the remaining time to resend the code
     */
    private fun countdown(timestamp: Timestamp) {

        binding.randomCodeInput.visibility = View.VISIBLE
        binding.rsnBtn.visibility = View.VISIBLE
        binding.rsnBtn.isClickable = false

        val differenceMillis: Long = (timestamp.time - System.currentTimeMillis())
        object : CountDownTimer(differenceMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                try {
                    val millis = millisUntilFinished
                    val hms = String.format(
                        "%02d:%02d",
                        (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(
                                millis
                            )
                        )),
                        (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millis)
                        ))
                    )
                    binding.rsnBtn.text = getString(R.string.countdownCode, hms)
                } catch (ex: Exception) {

                }
            }

            override fun onFinish() {
                // do something after countdown is done ie. enable button, change color
                binding.rsnBtn.text = getString(R.string.resendCode)
                binding.rsnBtn.isClickable = true
                binding.randomCodeInput.visibility = View.GONE
                deleteRandomCodeFormRoomDatabase()
            }
        }.start()
    }

    override fun onPause() {
        countdown(Timestamp(System.currentTimeMillis()))
        super.onPause()
    }

    //calculate expire date of the generated code
    private fun calculateExpireType(): Timestamp {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, EXPIRATION_TIME)
        return Timestamp(cal.time.time)
    }


    /**
     * To delete the random code from the room database
     */
    private fun deleteRandomCodeFormRoomDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                randomCodeTimeRepository.deleteResendCode()
            }
        }
    }


    /**
     * When the user clicks on reset password
     */
    private fun onResetPassword(){
        val email = binding.resetEmailInput.text.toString()
        if (email.isEmpty()){
            showSnackBarMessage(getString(R.string.email_required))
        }else{
            loginViewModel.resetPassword(email)
            loginViewModel.statusResponse.observe(viewLifecycleOwner){
                if (it == Api.ACCEPTED_CODE){
                    binding.sucessMessage.visibility = View.VISIBLE
                }
            }
        }
    }

    fun showSnackBarMessage(message: kotlin.String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}