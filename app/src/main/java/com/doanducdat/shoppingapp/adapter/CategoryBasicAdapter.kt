package com.doanducdat.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.databinding.ItemHotCategoryBinding
import com.doanducdat.shoppingapp.module.category.Category

class CategoryBasicAdapter : RecyclerView.Adapter<CategoryBasicAdapter.CategoryViewHolder>() {

    private var categoryList: MutableList<Category> = mutableListOf()

    fun setCategories(categories: MutableList<Category>) {
        categoryList = categories
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: ItemHotCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.imgHotCategory.setImageResource(category.ImageResource)
            binding.txtNameHotCategory.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemHotCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}