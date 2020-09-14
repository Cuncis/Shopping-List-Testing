package com.cuncisboss.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.cuncisboss.shoppinglist.R
import com.cuncisboss.shoppinglist.adapter.ImageAdapter
import com.cuncisboss.shoppinglist.adapter.ShoppingItemAdapter
import com.cuncisboss.shoppinglist.data.local.ShoppingItem
import com.cuncisboss.shoppinglist.getOrAwaitValue
import com.cuncisboss.shoppinglist.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


// androidTest

@MediumTest     // integration test (interaction between 2 fragment)
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ShoppingFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var glide: RequestManager
    private lateinit var shoppingAdapter: ShoppingItemAdapter
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var fragmentFactoryTest: ShoppingFragmentFactoryTest

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        glide = Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
        )
        shoppingAdapter = ShoppingItemAdapter(glide)
        imageAdapter = ImageAdapter(glide)
        fragmentFactoryTest = ShoppingFragmentFactoryTest(imageAdapter, glide, shoppingAdapter)
    }

    @Test
    fun swipeShoppingItem_deleteItemInDb() {
        val shoppingItem = ShoppingItem("TEST", 1, 1f, "TEST", 1)
        var testViewModel: ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment>(
            fragmentFactory = fragmentFactoryTest
        ) {
            testViewModel = viewModel
            viewModel?.insertShoppingItemIntoDb(shoppingItem)
        }

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.shoppingItems?.getOrAwaitValue()?.isEmpty())
    }

    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<ShoppingFragment>(
            fragmentFactory = fragmentFactoryTest
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )
    }

}