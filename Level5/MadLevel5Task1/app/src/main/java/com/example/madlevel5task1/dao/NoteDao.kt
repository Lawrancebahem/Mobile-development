package com.example.madlevel5task1.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.madlevel5task1.model.Note


@Dao
interface NoteDao{

    @Insert
    suspend fun insertNote(note:Note)

    @Query("SELECT * FROM noteTable LIMIT 1")
    fun getNotepad():LiveData<Note?>

    @Update
    suspend fun updateNote(note: Note)
}