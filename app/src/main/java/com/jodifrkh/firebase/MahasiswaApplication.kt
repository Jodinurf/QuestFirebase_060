package com.jodifrkh.firebase

import android.app.Application
import com.jodifrkh.firebase.dependenciesinjection.AppContainer
import com.jodifrkh.firebase.dependenciesinjection.MahasiswaContainer

class MahasiswaApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = MahasiswaContainer()
    }
}