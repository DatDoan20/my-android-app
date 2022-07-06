package com.doanducdat.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.model.product.Product
import com.doanducdat.shoppingapp.databinding.ItemProductGrid2Binding

class ProductPagingAdapter :
    PagingDataAdapter<Product, ProductPagingAdapter.ProductPagingViewHolder>(PRODUCT_COMPARATOR) {

    var callbackClickProduct: (product: Product) -> Unit = {}

    fun mySetOnClickProduct(callbackFun: (product: Product) -> Unit) {
        callbackClickProduct = callbackFun
    }

    inner class ProductPagingViewHolder(val binding: ItemProductGrid2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product?) {
            if (product != null) {
                binding.imgProductCover.load(product.getUrlImgCover())
                binding.txtNameProduct.text = product.name
                binding.ratingBar.rating = product.ratingsAverage
                binding.txtPriceProduct.text = product.getPrice()
                binding.txtDiscountProduct.text = product.getDiscount()
                binding.layoutItemProduct.setOnClickListener {
                    callbackClickProduct(product)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ProductPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPagingViewHolder {
        val binding = ItemProductGrid2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductPagingViewHolder(binding)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}