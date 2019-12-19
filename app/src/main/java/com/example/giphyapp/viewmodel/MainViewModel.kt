package com.example.giphyapp.viewmodel

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.giphyapp.base.BaseViewModel
import com.example.giphyapp.data.manager.MainDataManager

class MainViewModel(
    private val mainDataManager: MainDataManager
) : BaseViewModel() {

    val loading = mainDataManager.loading

    companion object {

        @JvmStatic
        @BindingAdapter("bind:isVisible")
        fun bindVisibility(view: View, isVisible: Boolean) {
            view.isVisible = isVisible
        }

    }

    val giphItems = mainDataManager.giphItems
    val itemReceivedData = mainDataManager.itemReceivedData

    fun load(query: String = "") = mainDataManager.load(query.trim())

    fun nextPage() = mainDataManager.nextPage()

    override fun onCleared() {
        mainDataManager.dispose()
        super.onCleared()
    }

}