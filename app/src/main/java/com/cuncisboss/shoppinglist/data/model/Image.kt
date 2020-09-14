package com.cuncisboss.shoppinglist.data.model

import com.google.gson.annotations.SerializedName

object Image {
    data class Response(
        val total: Int,
        val totalHits: Int,
        val hits: List<Hits>
    ) {
        data class Hits(
            val webformatHeight: Int,
            val imageWidth: Int,
            val favorites: Int,
            val previewHeight: Int,
            val webformatURL: String,
            val userImageURL: String,
            val previewURL: String,
            val comments: Int,
            val type: String,
            val imageHeight: Int,
            val tags: String,
            val previewWidth: Int,
            val downloads: Int,
            @SerializedName("user_id")
            val userId: Int,
            val largeImageURL: String,
            val pageURL: String,
            val id: Int,
            val imageSize: Int,
            val webformatWidth: Int,
            val user: String,
            val views: Int,
            val likes: Int
        )
    }
}