package com.example.giphyapp.util

data class Result<T>(
    val status: Status,
    val message: String? = null,
    private val dataInitializer: (() -> T)? = null
) {

    val data by lazy {
        dataInitializer!!()
    }

    companion object {

        fun <T> success(data: T) = Result<T>(
            Status.SUCCESS,
            dataInitializer = { data })

        fun <T> error(message: String?) = Result<T>(
            Status.ERROR,
            message
        )
    }

    enum class Status {
        SUCCESS,
        ERROR
    }

}