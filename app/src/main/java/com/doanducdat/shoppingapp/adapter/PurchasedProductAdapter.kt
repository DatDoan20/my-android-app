package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemProductInOrderBinding
import com.doanducdat.shoppingapp.module.order.PurchasedProduct

class PurchasedProductAdapter :
    RecyclerView.Adapter<PurchasedProductAdapter.ProductReviewHolder>() {
    private var purchasedProductList: MutableList<PurchasedProduct> = mutableListOf()

    private var callbackClick: (purchasedProduct: PurchasedProduct, position: Int) -> Unit =
        { _: PurchasedProduct, _: Int -> }

    @SuppressLint("NotifyDataSetChanged")
    fun setPurchasedProductList(purchasedProductList: MutableList<PurchasedProduct>) {
        this.purchasedProductList = purchasedProductList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteProductItem(index: Int) {
        purchasedProductList.removeAt(index)
        notifyDataSetChanged()
    }

    fun mySetOnClick(funClick: (purchasedProduct: PurchasedProduct, position: Int) -> Unit) {
        callbackClick = funClick
    }

    inner class ProductReviewHolder(val binding: ItemProductInOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(purchasedProductList: PurchasedProduct, position: Int) {
            binding.imgProductCover.load(purchasedProductList.getUrlImgCover())
            binding.txtNameProduct.text = purchasedProductList.name
            binding.layoutWriteReview.setOnClickListener {
                callbackClick.invoke(purchasedProductList, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewHolder {
        val binding =
            ItemProductInOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductReviewHolder, position: Int) {
        holder.bind(purchasedProductList[position], position)
    }

    override fun getItemCount(): Int {
        return purchasedProductList.size
    }
}