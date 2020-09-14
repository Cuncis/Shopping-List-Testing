package com.cuncisboss.shoppinglist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.cuncisboss.shoppinglist.R
import com.cuncisboss.shoppinglist.util.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import javax.inject.Inject


class AddShoppingItemFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObserver()

        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }

        ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    fun subscribeToObserver() {
        viewModel.curImageUrl.observe(viewLifecycleOwner, {
            glide.load(it).into(ivShoppingImage)
        })

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView().rootView,
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }
}











