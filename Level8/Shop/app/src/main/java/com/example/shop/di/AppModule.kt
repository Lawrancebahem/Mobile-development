package com.example.myapplicationtest2.DI

import android.app.Application
import androidx.room.Room
import com.example.myapplicationtest2.DAO.UserRoomDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(app, UserRoomDatabase::class.java, UserRoomDatabase.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideDao(database:UserRoomDatabase) = database.productDao()


    @Provides
    @Singleton
    fun provideRandomCodeDao(database:UserRoomDatabase) = database.randomCodeDao()
}