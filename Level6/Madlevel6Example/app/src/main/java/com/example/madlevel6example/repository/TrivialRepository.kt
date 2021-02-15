package com.example.madlevel6example.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.madlevel6example.api.TrivialApi
import com.example.madlevel6example.api.TrivialApiService
import com.example.madlevel6example.model.Trivial
import com.example.madlevel6example.viewModel.TrivialViewModel
import kotlinx.coroutines.withTimeout
import androidx.fragment.app.activityViewModels
class TriviaRepository {
    private val triviaApiService: TrivialApiService = TrivialApi.createApi()

    private val _trivia: MutableLiveData<Trivial> = MutableLiveData()
//    private val viewModel: TrivialViewModel by activityViewModels()

    /**
     * Expose non MutableLiveData via getter
     * Encapsulation :)
     */
    val trivia: LiveData<Trivial> get() = _trivia

//
//    private fun observeTrivia() {
//        viewModel.trivial.observe(viewLifecycleOwner, Observer {
//            tvTrivia.text = it?.text
//        })
//
//        // Observe the error message.
//        viewModel.errorText.observe(viewLifecycleOwner, Observer {
//            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
//        })
//    }

    /**
     * suspend function that calls a suspend function from the numbersApi call
     */
    suspend fun getRandomNumberTrivia()  {
        try {
            //timeout the request after 5 seconds
            val result = withTimeout(5_000) {
                triviaApiService.getRandomNumberTrivial()
            }
            _trivia.value = result
        } catch (error: Throwable) {
            throw TriviaRefreshError("Unable to refresh trivia", error)
        }
    }

    class TriviaRefreshError(message: String, cause: Throwable) : Throwable(message, cause)

}
