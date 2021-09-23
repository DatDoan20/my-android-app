package com.doanducdat.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ItemCommentBinding
import com.doanducdat.shoppingapp.module.review.Comment
import com.doanducdat.shoppingapp.utils.HandleTime

class CommentPagingAdapter :
    PagingDataAdapter<Comment, CommentPagingAdapter.CommentPagingViewHolder>(PRODUCT_COMPARATOR) {

    var callbackClickRepComment: (comment: Comment) -> Unit = {}

    fun mySetOnClickRepComment(callbackFun: (comment: Comment) -> Unit) {
        callbackClickRepComment = callbackFun
    }

    inner class CommentPagingViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment?) {
            if (comment != null) {
                binding.imgAvatar.load(comment.userId.getUrlAvatar())
                binding.txtName.text = comment.userId.name
                binding.txtContentComment.text = comment.comment
                binding.txtTimeComment.text = HandleTime.getTimeAgo(comment.updatedAt.time)
                if (comment.updatedAt.after(comment.createdAt)) {
                    binding.txtEdited.visibility = View.VISIBLE
                }
                binding.layoutRepComment.setOnClickListener {
                    callbackClickRepComment(comment)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CommentPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentPagingViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentPagingViewHolder(binding)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}