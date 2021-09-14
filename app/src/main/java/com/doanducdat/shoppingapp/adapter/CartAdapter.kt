package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemProductInCartBinding
import com.doanducdat.shoppingapp.module.cart.PopulatedCart
import com.doanducdat.shoppingapp.utils.AppConstants

class CartAdapter(
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var callbackClickMinusOrPlush: (
        CODE_ACTION_CLICK: Int,
        txtQuantity: TextView,
        txtPrice: TextView,
        finalPrice: Int,
        quantity: Int,
        idProduct: String
    ) -> Unit =
        { _: Int, _: TextView, _: TextView, _: Int, _: Int, _: String -> }

    private var cartList: MutableList<PopulatedCart> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setCartList(cartList: MutableList<PopulatedCart>) {
        this.cartList = cartList
        notifyDataSetChanged()
    }


    fun mySetOnClickMinusOrPlush(
        function: (
            CODE_ACTION_CLICK: Int,
            txtQuantity: TextView,
            txtPrice: TextView,
            finalPrice: Int,
            quantity: Int,
            idProduct: String
        ) -> Unit
    ) {
        callbackClickMinusOrPlush = function
    }

    inner class CartViewHolder(val binding: ItemProductInCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(populatedCart: PopulatedCart) {
            with(binding) {
                txtName.text = populatedCart.infoProduct.name
                txtSize.text = populatedCart.size
                txtColor.text = populatedCart.color
                txtPrice.text = populatedCart.getUnFormatFinalPrice().toString()
                txtQuantity.text = populatedCart.quantity.toString()
                imgProductCover.load(populatedCart.infoProduct.getUrlImgCover())
                //show discount when discount != 0
                if (populatedCart.infoProduct.getUnFormatDiscount() != 0) {
                    txtDiscountProduct.text = populatedCart.infoProduct.getDiscount()
                    txtPriceNotDiscount.text =
                        populatedCart.getFinalPriceUnDiscount(populatedCart.infoProduct.getUnFormatDiscount())
                    txtPriceNotDiscount.paintFlags =
                        txtPriceNotDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    txtDiscountProduct.visibility = View.VISIBLE
                    txtPriceNotDiscount.visibility = View.VISIBLE
                }
            }

            binding.imgPlush.setOnClickListener {
                minusOrPlushProduct(
                    AppConstants.ActionClick.PLUSH_PRODUCT_IN_CART,
                    populatedCart.getUnFormatFinalPrice(),
                    populatedCart.quantity,
                    populatedCart.infoProduct.id
                )
            }
            binding.imgMinus.setOnClickListener {
                minusOrPlushProduct(
                    AppConstants.ActionClick.MINUS_PRODUCT_IN_CART,
                    populatedCart.getUnFormatFinalPrice(),
                    populatedCart.quantity,
                    populatedCart.infoProduct.id
                )
            }
        }

        private fun minusOrPlushProduct(
            CODE_ACTION_CLICK: Int,
            finalPrice: Int,
            quantity: Int,
            idProduct: String
        ) {
            callbackClickMinusOrPlush(
                CODE_ACTION_CLICK,
                binding.txtQuantity,
                binding.txtPrice,
                finalPrice,
                quantity,
                idProduct
            )
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