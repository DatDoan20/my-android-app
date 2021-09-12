package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.databinding.ItemProductColorBinding
import com.doanducdat.shoppingapp.module.product.ProductColor
import com.doanducdat.shoppingapp.utils.MyBgCustom

class ProductColorAdapter(
) : RecyclerView.Adapter<ProductColorAdapter.ProductColorViewHolder>() {

    var preTxtClickedColor: TextView? = null
    var preClickedColor: ProductColor? = null

    private var productColors: MutableList<ProductColor> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setProductColors(productColors: MutableList<ProductColor>) {
        this.productColors = productColors
        notifyDataSetChanged()
    }

    private var callbackClickColor: (clickedColor: ProductColor, txtColorHex: TextView) -> Unit =
        { _: ProductColor, _: TextView -> }

    fun mySetOnClickColorProduct(function: (clickedColor: ProductColor, txtColor: TextView) -> Unit) {
        callbackClickColor = function
    }


    inner class ProductColorViewHolder(val binding: ItemProductColorBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(colorItem: ProductColor) {
            val bgItemColor =
                MyBgCustom.getInstance().bgOval( colorItem.getHexColor())
            binding.txtColorHex.background = bgItemColor

            binding.txtColorHex.setOnClickListener {
                //make pre clicked color lost stroke
                formatUnClickColor(colorItem)

                //make clicked color has stroke
                callbackClickColor.invoke(colorItem, binding.txtColorHex)
            }
        }

        private fun formatUnClickColor(colorItem: ProductColor) {
            if (preClickedColor !== null) {
                val bgPreClickedItemColor = MyBgCustom.getInstance()
                    .bgOval( preClickedColor!!.getHexColor())
                preTxtClickedColor!!.background = bgPreClickedItemColor
            }
            //assign current clicked productColorItem is preClickedColor
            preClickedColor = colorItem
            preTxtClickedColor = binding.txtColorHex
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductColorViewHolder {
        val binding =
            ItemProductColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductColorViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ProductColorViewHolder, position: Int) {
        holder.bind(productColors[position])
    }

    override fun getItemCount(): Int {
        return productColors.size
    }

}