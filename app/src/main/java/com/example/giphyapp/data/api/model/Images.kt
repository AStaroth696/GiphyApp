package com.example.giphyapp.data.api.model

import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("preview_gif")
    val image: Image
)