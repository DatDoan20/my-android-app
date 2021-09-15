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
    var isClickAble: Boolean = true
    private var cartList: MutableList<PopulatedCart> = mutableListOf()

    /**
     * carList is list in cart of InfoUser.currentUser, the same scope
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setCartList(cartList: MutableList<PopulatedCart>) {
        this.cartList = cartList
        notifyDataSetChanged()
    }

    private var callbackDelete: (idProduct: String) -> Unit = {}
    fun mySetOnClickDelete(funClickDelete: (idProduct: String) -> Unit) {
        callbackDelete = funClickDelete
    }

    private var callbackMinusOrPlush: (
        CODE_ACTION_CLICK: Int, populatedCart: PopulatedCart, txtQuantity: TextView, txtPrice: TextView
    ) -> Unit = { _: Int, _: PopulatedCart, _: TextView, _: TextView -> }

    fun mySetOnClickMinusOrPlush(
        function: (CODE_ACTION_CLICK: Int, populatedCart: PopulatedCart, txtQuantity: TextView, txtPrice: TextView)
        -> Unit
    ) {
        callbackMinusOrPlush = function
    }

    inner class CartViewHolder(val binding: ItemProductInCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(populatedCart: PopulatedCart) {
            bindDataUI(populatedCart)
            setUpClickItemAdapter(populatedCart)
        }

        private fun bindDataUI(populatedCart: PopulatedCart) {
            with(binding) {
                txtName.text = populatedCart.infoProduct.name
                txtSize.text = populatedCart.size
                txtColor.text = populatedCart.color
                txtPrice.text = populatedCart.getFormatFinalPrice()
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
        }

        private fun setUpClickItemAdapter(populatedCart: PopulatedCart) {
            binding.imgPlush.setOnClickListener {
                callbackMinusOrPlush(
                    AppConstants.ActionClick.PLUSH_PRODUCT_IN_CART, populatedCart,
                    binding.txtQuantity, binding.txtPrice
                )
            }
            binding.imgMinus.setOnClickListener {
                callbackMinusOrPlush(
                    AppConstants.ActionClick.MINUS_PRODUCT_IN_CART, populatedCart,
                    binding.txtQuantity, binding.txtPrice
                )
            }
            binding.imgDelete.setOnClickListener {
                callbackDelete.invoke(populatedCart.infoProduct.id)
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