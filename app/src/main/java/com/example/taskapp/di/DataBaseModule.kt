package com.example.taskapp.di

import android.content.Context
import androidx.room.Room
import com.example.taskapp.database.AppDataBase
import com.example.taskapp.database.dao.StreakDao
import com.example.taskapp.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
 object DataBaseModule {

    @Singleton
    @JvmStatic
    @Provides
    fun provideDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, AppDataBase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Reusable
   @JvmStatic
    @Provides
    fun provideTaskDao(appDataBase: AppDataBase): TaskDao = appDataBase.taskDao()

    @JvmStatic
    @Reusable
    @Provides
    fun provideStreakDao(appDataBase: AppDataBase): StreakDao = appDataBase.streakDao()
}