package com.doanducdat.shoppingapp.ui.main.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CartAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCartBinding
import com.doanducdat.shoppingapp.module.response.Status
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.dialog.MyYesNoDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)


    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    private val dialog by lazy { MyYesNoDialog(requireContext()) }
    private val cartAdapter: CartAdapter = CartAdapter()
    private val viewModel: CartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRcvCart()
        listenLoadingForm()
    }

    private fun setUpRcvCart() {
        InfoUser.currentUser?.let { cartAdapter.setCartList(it.cart) }
        binding.rcvCart.adapter = cartAdapter
        binding.rcvCart.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //event click adapter
        cartAdapter.mySetOnClickMinusOrPlush { CODE_ACTION_CLICK, txtQuantity, txtPrice, idProduct ->
            val price = txtPrice.text.toString().replace(",","").toInt()
            val quantity = txtQuantity.text.toString().toInt()
            val priceOneItem = price / quantity

            var newQuantity = 0
            var newPrice = 0

            if (CODE_ACTION_CLICK == AppConstants.ActionClick.PLUSH_PRODUCT_IN_CART) {
                newQuantity = quantity + 1
                newPrice = price + priceOneItem
            } else {
                if (quantity == 1) {
                    newQuantity = quantity
                    newPrice = price
                    showDialogConfirmDeleteProductInCart(idProduct)
                } else {
                    newQuantity = quantity - 1
                    newPrice = price - priceOneItem
                }
            }
            txtPrice.text = DecimalFormat("#,###").format(newPrice)
            txtQuantity.text = newQuantity.toString()
        }
    }

    private fun showDialogConfirmDeleteProductInCart(idProduct: String) {
        dialog.setText(AppConstants.MsgInfo.CONFIRM_DELETE_PRODUCT_IN_CART)
        dialog.mySetOnClickYes {
            deleteProductInCart(idProduct)
        }
        dialog.show()
    }

    private fun deleteProductInCart(idProduct: String) {
        listenDeleteProductInCart()
        Log.e("TAG", "deleteProductInCart: $idProduct")
        viewModel.deleteProductInCart(idProduct)
    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            cartAdapter.isClickAble = !it
            if (it) {
                setStateProgressBar(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateProgressBar(View.GONE, binding.spinKitProgressBar)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun listenDeleteProductInCart() {
        viewModel.dataStateDeleteProductInCart.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    Log.e("TAG", "listenDeleteProductInCart: ${it.message}")
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    Log.e("TAG", "listenDeleteProductInCart: ${it.response!!.message}")
                    showToast(AppConstants.MsgInfo.MSG_INFO_DELETE_PRODUCT_IN_CART)

                    //update currentUser variable in Ram
                    InfoUser.currentUser = it.response.data

                    //update adapter rcv
                    cartAdapter.setCartList(InfoUser.currentUser!!.cart)
                    cartAdapter.notifyDataSetChanged()

                    viewModel.isLoading.value = false
                    dialog.dismiss()
                }
            }
        })
    }
}