package com.vp.list.model

import java.util.*


data class SearchResult private constructor(
    val items: List<ListItem>,
    private val hasResponse: Boolean,
    val totalResult: Int
) {

    fun hasResponse(): Boolean {
        return hasResponse
    }

    companion object {
        @JvmStatic
        fun error(): SearchResult {
            return SearchResult(Collections.emptyList(), false, 0)
        }

        @JvmStatic
        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, true, totalResult)
        }
    }
}