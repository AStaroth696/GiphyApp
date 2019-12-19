package com.example.giphyapp.di

import com.example.giphyapp.data.api.GiphyService
import com.example.giphyapp.data.manager.MainDataManager
import com.example.giphyapp.data.manager.MainDataManagerImpl
import com.example.giphyapp.viewmodel.MainViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private val dataModule = module {

    factory { Retrofit.Builder()
        .baseUrl("https://api.giphy.com/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
        .create(GiphyService::class.java)
    }

    factory { MainDataManagerImpl(get()) as MainDataManager }

}

private val viewModelModule = module {

    viewModel { MainViewModel(get()) }

}

val modules = listOf(dataModule, viewModelModule)