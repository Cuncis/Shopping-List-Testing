package com.cuncisboss.shoppinglist.data.remote

import com.cuncisboss.shoppinglist.BuildConfig
import com.cuncisboss.shoppinglist.data.model.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<Image.Response>

}