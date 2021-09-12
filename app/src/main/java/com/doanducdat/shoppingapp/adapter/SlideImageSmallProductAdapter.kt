package com.doanducdat.shoppingapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemImgProductBinding
import com.doanducdat.shoppingapp.databinding.ItemSmallImgProductBinding

class SlideImageSmallProductAdapter :
    RecyclerView.Adapter<SlideImageSmallProductAdapter.SlideImageSmallProductViewHolder>() {

    private var urlImagesProduct: MutableList<String> = mutableListOf()

    fun setUrlImagesProduct(urlImages: MutableList<String>) {
        this.urlImagesProduct = urlImages
    }

    private var callbackClickImage: (image: Drawable) -> Unit = {}
    fun mySetOnclickImage(funClickImage: (image: Drawable) -> Unit) {
        callbackClickImage = funClickImage
    }

    inner class SlideImageSmallProductViewHolder(val binding: ItemSmallImgProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(urlImg: String) {
            binding.imgProduct.load(urlImg)
            binding.imgProduct.setOnClickListener {
                callbackClickImage(binding.imgProduct.drawable)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideImageSmallProductAdapter.SlideImageSmallProductViewHolder {
        val binding =
            ItemSmallImgProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideImageSmallProductViewHolder(binding)
    }

    override fun onBindViewHolder(holderSmall: SlideImageSmallProductViewHolder, position: Int) {
        holderSmall.bind(urlImagesProduct[position])
    }

    override fun getItemCount(): Int {
        return urlImagesProduct.size
    }
}