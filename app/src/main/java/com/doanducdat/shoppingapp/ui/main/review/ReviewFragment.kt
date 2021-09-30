package com.doanducdat.shoppingapp.ui.main.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.ReviewPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentReviewBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewFragment : BaseFragment<FragmentReviewBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReviewBinding = FragmentReviewBinding.inflate(inflater, container, false)

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    var productId: String = ""
    private val viewModel: ReviewViewModel by viewModels()
    private val reviewAdapter: ReviewPagingAdapter by lazy { ReviewPagingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromAnotherFragment()
        setUpBackFragment()

        setUpRcvReview()
        listenStateLoadReview()
        if(reviewAdapter.itemCount == 0) {
            loadReview()
        }
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    private fun getDataFromAnotherFragment() {
        val bundle = arguments
        if (bundle != null) {
            productId = bundle.getString("PRODUCT_ID").toString()
        }
        Log.d(AppConstants.TAG.REVIEW, "getDataFromAnotherFragment productId:${productId} ")
    }

    private fun setUpRcvReview() {
        binding.rcvReview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvReview.adapter = reviewAdapter

        reviewAdapter.mySetOnClickReview {
            if (it.comments.isEmpty()) {
                showLongToast(AppConstants.MsgInfo.COMMENTS_EMPTY)
            } else {
                controller.navigate(R.id.commentFragment, bundleOf("REVIEW_ID" to it.id))
            }
        }
    }

    private fun listenStateLoadReview() {
        lifecycleScope.launch {
            reviewAdapter.loadStateFlow.collectLatest { loadStates ->
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
            viewModel.getReviewPaging(productId).collectLatest {
                reviewAdapter.submitData(it)
            }
        }

    }
}