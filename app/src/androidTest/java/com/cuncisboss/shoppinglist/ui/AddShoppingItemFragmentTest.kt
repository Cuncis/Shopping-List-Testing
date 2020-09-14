package com.cuncisboss.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.cuncisboss.shoppinglist.R
import com.cuncisboss.shoppinglist.adapter.ImageAdapter
import com.cuncisboss.shoppinglist.data.local.ShoppingItem
import com.cuncisboss.shoppinglist.getOrAwaitValue
import com.cuncisboss.shoppinglist.launchFragmentInHiltContainer
import com.cuncisboss.shoppinglist.repositories.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@MediumTest     // integration test (interaction between 2 fragment)
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AddShoppingItemFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var glide: RequestManager
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var fragmentFactory: ShoppingFragmentFactory


    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        glide = Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
        )
        imageAdapter = ImageAdapter(glide)
        fragmentFactory = ShoppingFragmentFactory(imageAdapter, glide)
    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb() {
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory = fragmentFactory) {
            viewModel = testViewModel
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("shopping item", 5, 5.5f, ""))
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        verify(navController).popBackStack()
    }

}