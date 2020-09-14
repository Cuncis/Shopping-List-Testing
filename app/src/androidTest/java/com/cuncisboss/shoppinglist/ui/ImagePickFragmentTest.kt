package com.cuncisboss.shoppinglist.ui

import android.content.Context
import android.content.pm.PackageManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
import com.cuncisboss.shoppinglist.getOrAwaitValue
import com.cuncisboss.shoppinglist.launchFragmentInHiltContainer
import com.cuncisboss.shoppinglist.repositories.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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
        fragmentFactory = ShoppingFragmentFactory(imageAdapter)
    }

    @Test
    fun clickImage_popBackStackAndSetImageUrl() {
        val navController = mock(NavController::class.java)
        val imageUrl = "TEST"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)
            viewModel = testViewModel
        }

        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
                click()
            )
        )

        verify(navController).popBackStack()
        assertThat(testViewModel.curImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)
    }

}









