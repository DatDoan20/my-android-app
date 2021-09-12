package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ItemProductSizeBinding
import com.doanducdat.shoppingapp.utils.MyBgCustom

class ProductSizeAdapter(
) : RecyclerView.Adapter<ProductSizeAdapter.ProductSizeViewHolder>() {

    var preTxtClickedSize: TextView? = null
    var preClickedSize: String? = null

    private var productSizes: MutableList<String> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setProductSizes(productSizes: MutableList<String>) {
        this.productSizes = productSizes
        notifyDataSetChanged()
    }

    private var callbackClickSize: (clickedSize: String, txtSize: TextView) -> Unit =
        { _: String, _: TextView -> }

    fun mySetOnClickSizeProduct(function: (clickedSize: String, txtSize: TextView) -> Unit) {
        callbackClickSize = function
    }


    inner class ProductSizeViewHolder(val binding: ItemProductSizeBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sizeItem: String) {
            val bgItemSize =
                MyBgCustom.getInstance().bgRadiusSize(context, R.color.sizeProductUnPick)
            binding.txtSize.background = bgItemSize
            binding.txtSize.text = sizeItem

            binding.txtSize.setOnClickListener {
                //make pre clicked get bg color is grey
                formatUnClickSize(sizeItem, bgItemSize)

                //make clicked color get bg color is blue
                callbackClickSize.invoke(sizeItem, binding.txtSize)
            }
        }

        private fun formatUnClickSize(sizeItem: String, bgItemSize: GradientDrawable) {
            if (preClickedSize !== null) {
                preTxtClickedSize!!.background = bgItemSize
            }
            //assign current clicked productSizeItem is preClickedSize
            preClickedSize = sizeItem
            preTxtClickedSize = binding.txtSize
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSizeViewHolder {
        val binding =
            ItemProductSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductSizeViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ProductSizeViewHolder, position: Int) {
        holder.bind(productSizes[position])
    }

    override fun getItemCount(): Int {
        return productSizes.size
    }

}