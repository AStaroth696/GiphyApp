package com.example.giphyapp.data.manager

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import com.example.giphyapp.BuildConfig
import com.example.giphyapp.data.api.GiphyService
import com.example.giphyapp.data.api.model.Giph
import com.example.giphyapp.data.api.model.ResponseWrapper
import com.example.giphyapp.ui.adapter.GiphAdapter
import com.example.giphyapp.util.GIPHS_PRE_PAGE
import com.example.giphyapp.util.Result
import com.example.giphyapp.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

interface MainDataManager {

    val giphItems: MutableList<Giph>
    val itemReceivedData: LiveData<Result<GiphAdapter.NotifyEvent>>
    val loading: ObservableBoolean

    fun load(query: String)
    fun nextPage()
    fun dispose()

}

class MainDataManagerImpl(private val giphyService: GiphyService) : MainDataManager {

    override val giphItems = mutableListOf<Giph>()
    override val itemReceivedData = SingleLiveEvent<Result<GiphAdapter.NotifyEvent>>()
    override val loading = ObservableBoolean(false)

    private var requestDisposable: Disposable? = null
    private var query = ""

    override fun load(query: String) {
        giphItems.clear()
        loading.set(true)
        this.query = query
        executeRequest(query) {
            itemReceivedData.callWithValue(
                Result.success(GiphAdapter.NotifyEvent.DatasetChanged)
            )
        }
    }

    override fun nextPage() {
        if (!loading.get()) {
            executeRequest(query) {
                itemReceivedData.callWithValue(
                    Result.success(GiphAdapter.NotifyEvent.RangeInserted(it.data.data.size))
                )
            }
        }
    }

    override fun dispose() {
        requestDisposable?.dispose()
    }

    private fun executeRequest(
        query: String,
        onComplete: (Result<ResponseWrapper<List<Giph>>>) -> Unit
    ) {
        loading.set(true)
        requestDisposable?.dispose()
        requestDisposable = if (query.isEmpty()) {
            giphyService.requestTrending(
                BuildConfig.GIPHY_API_KEY,
                GIPHS_PRE_PAGE,
                giphItems.size
            )
        } else {
            giphyService.requestGiffs(
                BuildConfig.GIPHY_API_KEY,
                query,
                GIPHS_PRE_PAGE,
                giphItems.size
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loading.set(false)
                giphItems.addAll(it.data)
                onComplete(Result.success(it))
            }, {
                loading.set(false)
                itemReceivedData.callWithValue(Result.error(it.message))
            })
    }

}