package com.example.madlevel5task1.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.madlevel5task1.model.Note
import com.example.madlevel5task1.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val noteRepository = NoteRepository(application.applicationContext)

    val note = noteRepository.getNotepad()
    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()


    fun updateNote(title: String, text: String) {

        val newNote = Note(id = note.value?.id, title = title, lastUpdated = Date(), text = text)

        if (isNotValid(newNote)) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    noteRepository.updateNote(newNote)
                }
                success.value = true
            }
        }
    }

    private fun isNotValid(note: Note): Boolean {
        return when {
            note.title.isBlank() -> {
                error.value = "Title must not be empty"
                false
            }
            else -> true
        }
    }

}