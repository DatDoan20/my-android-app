package com.doanducdat.shoppingapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.databinding.ItemReviewBinding
import com.doanducdat.shoppingapp.module.review.Review
import com.doanducdat.shoppingapp.utils.HandlerTime

class ReviewPagingAdapter :
    PagingDataAdapter<Review, ReviewPagingAdapter.ReviewPagingViewHolder>(PRODUCT_COMPARATOR) {

    var callbackClickReview: (review: Review) -> Unit = {}

    fun mySetOnClickReview(callbackFun: (review: Review) -> Unit) {
        callbackClickReview = callbackFun
    }

    inner class ReviewPagingViewHolder(val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(review: Review?) {
            if (review != null) {
                binding.imgAvatar.load(review.userId.getUrlAvatar())
                binding.txtName.text = review.userId.name
                binding.ratingBar.rating = review.rating.toFloat()
                binding.txtReview.text = review.review
                binding.txtComment.text = "${review.comments.size} Bình luận"
                binding.txtTimeReview.text = HandlerTime.getTimeAgo(review.updatedAt.time)
                binding.layoutComment.setOnClickListener {
                    callbackClickReview(review)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ReviewPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewPagingViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewPagingViewHolder(binding)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}