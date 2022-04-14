package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class ListItem (
    @SerializedName("Title")
    val title: String? = null,

    @SerializedName("Year")
    val year: String? = null,

    val imdbID: String? = null,

    @SerializedName("Poster")
    val poster: String? = null
)

