package com.doanducdat.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemImgProductBinding

class SlideImageProductAdapter :
    RecyclerView.Adapter<SlideImageProductAdapter.SlideImageProductViewHolder>() {

    private var urlImagesProduct: MutableList<String> = mutableListOf()

    fun setUrlImagesProduct(urlImages: MutableList<String>) {
        this.urlImagesProduct = urlImages
    }

    inner class SlideImageProductViewHolder(val binding: ItemImgProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(urlImg: String) {
            binding.imgProduct.load(urlImg)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideImageProductAdapter.SlideImageProductViewHolder {
        val binding =
            ItemImgProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideImageProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideImageProductViewHolder, position: Int) {
        holder.bind(urlImagesProduct[position])
    }

    override fun getItemCount(): Int {
        return urlImagesProduct.size
    }
}