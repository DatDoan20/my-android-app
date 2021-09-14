package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ItemProductSizeBinding
import com.google.android.material.card.MaterialCardView

class ProductSizeAdapter(
) : RecyclerView.Adapter<ProductSizeAdapter.ProductSizeViewHolder>() {

    var preLayoutTxtClickedSize: MaterialCardView? = null

    private var productSizes: MutableList<String> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setProductSizes(productSizes: MutableList<String>) {
        this.productSizes = productSizes
        notifyDataSetChanged()
    }

    private var callbackClickSize: (sizeItem: String, layoutTxtSize: MaterialCardView) -> Unit =
        { _: String, _: MaterialCardView -> }

    fun mySetOnClickSizeProduct(function: (sizeItem: String, layoutTxtSize: MaterialCardView) -> Unit) {
        callbackClickSize = function
    }


    inner class ProductSizeViewHolder(val binding: ItemProductSizeBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sizeItem: String) {
            binding.txtSize.text = sizeItem

            binding.txtSize.setOnClickListener {
                //make pre clicked layoutTextSize(CardView) get bg color is grey
                formatUnClickSize()

                //make current clicked layoutTextSize(CardView) get bg color is blue
                callbackClickSize.invoke(sizeItem, binding.layoutTxtSize)
            }
        }

        private fun formatUnClickSize() {
            if (preLayoutTxtClickedSize !== null) {
                preLayoutTxtClickedSize!!.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.sizeProductUnPick)
                )
            }
            preLayoutTxtClickedSize = binding.layoutTxtSize
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