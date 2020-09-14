package com.cuncisboss.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.cuncisboss.shoppinglist.data.local.ShoppingDao
import com.cuncisboss.shoppinglist.data.local.ShoppingItemDatabase
import com.cuncisboss.shoppinglist.data.remote.PixabayApi
import com.cuncisboss.shoppinglist.repositories.DefaultShoppingRepository
import com.cuncisboss.shoppinglist.repositories.ShoppingRepository
import com.cuncisboss.shoppinglist.util.Constants.BASE_URL
import com.cuncisboss.shoppinglist.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayApi
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }

}













