package com.example.madlevel5task2.ui

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.madlevel5task2.model.Game
import com.example.madlevel5task2.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val TITLE_ERROR = "Please fill the title field"
    private val PLATFORM_ERROR = "Please fill the platform field"
    private val RELEASE_DATE_ERROR = "Please fill a valid date"


    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val gameRepository = GameRepository(application.applicationContext)
    val games = gameRepository.getAllGames()
    var error = MutableLiveData<String>()
    var sucsess = MutableLiveData<Boolean>()


    /**
     * To insert a game into the database
     */
    fun insertGame(titleEdittext: EditText, platformEditText: EditText, dayEditText: EditText, monthEditText: EditText, yearEditText: EditText) {
        val game = isValid(titleEdittext, platformEditText, dayEditText, monthEditText, yearEditText)
        if (game != null) {
            mainScope.launch {
                withContext(Dispatchers.IO){
                    gameRepository.insertGame(game)
                }
            }
        }
    }


    /**
     * To delete a certain game
     */
    fun deleteGame(game:Game){

        mainScope.launch {
            withContext(Dispatchers.IO){
                gameRepository.deleteGame(game)
            }
            sucsess.value = true
        }
    }

    /**
     * To delete all games
     */
    fun deleteAllGames(){

        mainScope.launch {
            withContext(Dispatchers.IO){
                gameRepository.deleteAllGames()
            }
            sucsess.value = true
        }
    }


    /**
     * To check whether the fields are valid or not
     */
    private fun isValid(titleEdittext: EditText, platformEditText: EditText, dayEditText: EditText, monthEditText: EditText, yearEditText: EditText): Game? {
        when {
            titleEdittext.text.isBlank() -> {
                error.value = TITLE_ERROR
                return null
            }
            platformEditText.text.isBlank() -> {
                error.value = PLATFORM_ERROR
                return null
            }
            else -> {
                return try {
                    val day = dayEditText.text.toString().toInt()
                    val month = monthEditText.text.toString().toInt()
                    val year = yearEditText.text.toString().toInt()
                    val gameReleaseDate = LocalDate.of(year, month, day)
                    val title = titleEdittext.text.toString()
                    val platform = platformEditText.text.toString()

                    sucsess.value = true
                    Game(title = title, platform = platform, releaseDate = gameReleaseDate)

                } catch (e: Exception) {
                    error.value = RELEASE_DATE_ERROR
                    null
                }
            }
        }
    }
}