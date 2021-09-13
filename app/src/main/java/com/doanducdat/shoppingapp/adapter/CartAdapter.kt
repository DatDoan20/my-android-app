package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemProductInCartBinding
import com.doanducdat.shoppingapp.module.cart.PopulatedCart

class CartAdapter(
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var callbackClickCart: (populatedCart: PopulatedCart) -> Unit = {}

    private var cartList: MutableList<PopulatedCart> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setCartList(cartList: MutableList<PopulatedCart>) {
        this.cartList = cartList
        notifyDataSetChanged()
    }

    fun mySetOnClickCartAdapter(function: (populatedCart: PopulatedCart) -> Unit) {
        callbackClickCart = function
    }

    inner class CartViewHolder(val binding: ItemProductInCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(populatedCart: PopulatedCart) {
            with(binding) {
                txtName.text = populatedCart.infoProduct.name
                txtSize.text = populatedCart.size
                txtColor.text = populatedCart.color

                imgProductCover.load(populatedCart.infoProduct.getUrlImgCover())
                if (populatedCart.infoProduct.getUnFormatDiscount() != 0) {
                    txtDiscountProduct.text = populatedCart.infoProduct.getDiscount()
                    txtPriceNotDiscount.text = populatedCart.infoProduct.getPriceUnDiscount()
                    txtPriceNotDiscount.paintFlags =
                        txtPriceNotDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    txtDiscountProduct.visibility = View.VISIBLE
                    txtPriceNotDiscount.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            ItemProductInCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

}