package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class SearchResponse constructor(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val search: List<ListItem>? = null,
    val totalResults: Int = 0
) {
    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}