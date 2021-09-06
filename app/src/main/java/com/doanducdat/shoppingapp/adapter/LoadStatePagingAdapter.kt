package com.doanducdat.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.databinding.ItemStateLoadBinding

class LoadStatePagingAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStatePagingAdapter.LoadStatePagingViewHolder>() {

    inner class LoadStatePagingViewHolder(val binding: ItemStateLoadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.btnTryAgain.setOnClickListener { retry.invoke() }
            if (loadState is LoadState.Error) {
                binding.txtErrMsg.text = loadState.error.localizedMessage
            }
            //loading -> show progressbar
            binding.spinKitProgressBar.isVisible = loadState is LoadState.Loading
            //loading is done -> show button + text
            binding.txtErrMsg.isVisible = loadState is LoadState.Error
            binding.btnTryAgain.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadStatePagingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStatePagingViewHolder {
        val binding =
            ItemStateLoadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStatePagingViewHolder(binding)
    }
}