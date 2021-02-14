package com.example.madlevel5example.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.madlevel5example.model.Reminder

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminderTable")
     fun getAllReminders():LiveData<List<Reminder>>

    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)

}