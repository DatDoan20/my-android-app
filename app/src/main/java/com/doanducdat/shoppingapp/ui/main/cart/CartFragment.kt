package com.doanducdat.shoppingapp.ui.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CartAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCartBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser

class CartFragment : BaseFragment<FragmentCartBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    private val cartAdapter: CartAdapter = CartAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRcvCart()
    }

    private fun setUpRcvCart() {
        InfoUser.currentUser?.let { cartAdapter.setCartList(it.cart) }
        binding.rcvCart.adapter = cartAdapter
        binding.rcvCart.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //event click adapter
        cartAdapter.mySetOnClickMinusOrPlush { CODE_ACTION_CLICK, txtQuantity, txtPrice, _, _, _ ->
            val price = txtPrice.text.toString().toInt()
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
                } else {
                    newQuantity = quantity - 1
                    newPrice = price - priceOneItem
                }
            }
            txtPrice.text = newPrice.toString()
            txtQuantity.text = newQuantity.toString()
        }

    }
}