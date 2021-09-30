package com.doanducdat.shoppingapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemSmallImgProductBinding
import com.google.android.material.card.MaterialCardView

class SlideImageSmallProductAdapter :
    RecyclerView.Adapter<SlideImageSmallProductAdapter.SlideImageSmallProductViewHolder>() {

    private var urlImagesProduct: MutableList<String> = mutableListOf()

    fun setUrlImagesProduct(urlImages: MutableList<String>) {
        this.urlImagesProduct = urlImages
    }
    fun getFirstUrlImage(): String {
        return urlImagesProduct[0]
    }
    private var callbackClickLayoutImage: (layoutImage: MaterialCardView, clickedImage: Drawable) -> Unit =
        { _: View, _: Drawable -> }

    fun mySetOnclickImage(funClickImage: (layoutImage: MaterialCardView, clickedImage: Drawable) -> Unit) {
        callbackClickLayoutImage = funClickImage
    }

    private var preClickedLayoutImage: MaterialCardView? = null

    inner class SlideImageSmallProductViewHolder(val binding: ItemSmallImgProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(urlImg: String) {
            binding.imgProduct.load(urlImg)
            binding.imgProduct.setOnClickListener {
                if (preClickedLayoutImage != null) {
                    preClickedLayoutImage!!.strokeWidth = 0
                }
                preClickedLayoutImage = binding.layoutImgProduct
                callbackClickLayoutImage(binding.layoutImgProduct, binding.imgProduct.drawable)
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