package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemProductBinding
import com.doanducdat.shoppingapp.model.product.Product

class ProductBasicAdapter : RecyclerView.Adapter<ProductBasicAdapter.ProductViewHolder>() {
    private var productList: List<Product> = listOf()

    private var callbackClick: (product:Product) -> Unit = {}
    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(products: List<Product>) {
        productList = products
        notifyDataSetChanged()
    }
    fun mySetOnClick(funClick:(product:Product)->Unit){
        callbackClick = funClick
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.imgProductCover.load(product.getUrlImgCover())
            binding.txtNameProduct.text = product.name
            binding.ratingBar.rating = product.ratingsAverage
            binding.txtPriceProduct.text = product.getPrice()
            binding.txtDiscountProduct.text = product.getDiscount()
            binding.layoutItemProduct.setOnClickListener {
                callbackClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}