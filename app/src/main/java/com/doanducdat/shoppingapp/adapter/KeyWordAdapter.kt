package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.databinding.ItemKeyWordBinding
import com.doanducdat.shoppingapp.room.entity.KeyWordCacheEntity

class KeyWordAdapter : RecyclerView.Adapter<KeyWordAdapter.KeyWordViewHolder>() {

    private var keyWordList: List<KeyWordCacheEntity> = listOf()
    fun setKeyWordList(keyWordList: List<KeyWordCacheEntity>) {
        this.keyWordList = keyWordList
    }

    var callback: (keyWord: String) -> Unit = {}
    fun setOnClick(funCallback: (keyWord: String) -> Unit) {
        callback = funCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAllKeyWord() {
        keyWordList = emptyList()
        notifyDataSetChanged()
    }

    inner class KeyWordViewHolder(val binding: ItemKeyWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keyWord: KeyWordCacheEntity) {
            binding.txtKeyWord.text = keyWord.keyWord
            binding.layoutKeyWord.setOnClickListener {
                callback.invoke(keyWord.keyWord)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyWordViewHolder {
        val binding =
            ItemKeyWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeyWordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeyWordViewHolder, position: Int) {
        holder.bind(keyWordList[position])
    }

    override fun getItemCount(): Int {
        return keyWordList.size
    }
}