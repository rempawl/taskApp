package com.example.taskapp

import android.app.Application
import com.example.taskapp.di.AppComponent
import com.example.taskapp.di.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen

//todo Reminder,Duration,Frequency
//todo changing updateDate
class MyApp : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(applicationContext)
   }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}