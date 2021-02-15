package com.example.madlevel5task1.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.madlevel5task1.dao.NoteDao
import com.example.madlevel5task1.databaase.NoteRoomDatabase
import com.example.madlevel5task1.model.Note

class NoteRepository(context: Context){

    private val noteDao:NoteDao

    init {
        val noteDatabase = NoteRoomDatabase.getDatabase(context)
        noteDao = noteDatabase!!.notDao()
    }

    fun getNotepad(): LiveData<Note?>{
        return this.noteDao.getNotepad()
    }

    suspend fun updateNote(note: Note){
        this.noteDao.updateNote(note)
    }
}