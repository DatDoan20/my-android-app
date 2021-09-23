package com.doanducdat.shoppingapp.ui.main.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CommentPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCommentBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.view.MotionEvent

import android.view.View.OnTouchListener




@AndroidEntryPoint
class CommentFragment : BaseFragment<FragmentCommentBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCommentBinding = FragmentCommentBinding.inflate(inflater, container, false)

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    private var reviewId: String = ""
    private val viewModel: ReviewViewModel by viewModels()
    private val commentAdapter: CommentPagingAdapter by lazy { CommentPagingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromAnotherFragment()
        setUpBackFragment()

        setUpRcvReview()
        listenStateLoadReview()
        loadReview()

        setUpEventSend()
    }

    private fun getDataFromAnotherFragment() {
        val bundle = arguments
        if (bundle != null) {
            reviewId = bundle.getString("REVIEW_ID").toString()
        }
        Log.d(AppConstants.TAG.REVIEW, "getDataFromAnotherFragment productId:${reviewId} ")
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpRcvReview() {
        binding.rcvComment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvComment.adapter = commentAdapter
        commentAdapter.mySetOnClickRepComment {
            binding.edtReply.setText("${it.userId.name}: ")
            binding.edtReply.requestFocus()
            binding.edtReply.setSelection(binding.edtReply.length())
        }

    }

    private fun listenStateLoadReview() {
        lifecycleScope.launch {
            commentAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding.spinKitProgressBar.visibility = View.VISIBLE
                    }
                    is LoadState.Error -> {
                        binding.spinKitProgressBar.visibility = View.GONE
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    }
                    else -> binding.spinKitProgressBar.visibility = View.GONE

                }
            }
        }
    }

    private fun loadReview() {
        lifecycleScope.launch {
            viewModel.getCommentPaging(reviewId).collectLatest {
                commentAdapter.submitData(it)
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setUpEventSend() {
        binding.imgSend.setOnTouchListener { v, event ->
            v.isSelected = event.action == MotionEvent.ACTION_DOWN
            true
        }
    }
}