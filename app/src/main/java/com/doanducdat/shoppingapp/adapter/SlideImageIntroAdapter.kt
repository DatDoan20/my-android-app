package com.doanducdat.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemImgIntroBinding
import com.doanducdat.shoppingapp.model.SlideImage

class SlideImageIntroAdapter(
) : RecyclerView.Adapter<SlideImageIntroAdapter.SlideImageIntroViewHolder>() {

    private val listImagesIntro: MutableList<SlideImage> = mutableListOf()
    fun addImage(slideItem: SlideImage) {
        listImagesIntro.add(slideItem)
    }

    inner class SlideImageIntroViewHolder(binding: ItemImgIntroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imgIntro: ImageView = binding.imgIntro
        fun bind(slideItem: SlideImage) {
            imgIntro.load(slideItem.imgUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideImageIntroViewHolder {
        val binding =
            ItemImgIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideImageIntroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideImageIntroViewHolder, position: Int) {
        holder.bind(listImagesIntro[position])
    }

    override fun getItemCount(): Int {
        return listImagesIntro.size
    }
}