package com.example.giphyapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GiphyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(com.example.giphyapp.di.modules)
            androidContext(this@GiphyApp)
        }

    }

}