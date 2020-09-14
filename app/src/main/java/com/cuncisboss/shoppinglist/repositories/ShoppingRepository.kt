package com.cuncisboss.shoppinglist.repositories

import androidx.lifecycle.LiveData
import com.cuncisboss.shoppinglist.data.local.ShoppingItem
import com.cuncisboss.shoppinglist.data.model.Image
import com.cuncisboss.shoppinglist.util.Resource
import retrofit2.Response


interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<Image.Response>

}