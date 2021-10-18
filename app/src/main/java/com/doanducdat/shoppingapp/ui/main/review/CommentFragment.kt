package com.doanducdat.shoppingapp.ui.main.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CommentPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCommentBinding
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.model.review.CommentPost
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CommentFragment : BaseFragment<FragmentCommentBinding>(), MyActionApp {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCommentBinding = FragmentCommentBinding.inflate(inflater, container, false)

    private var reviewId: String? = null
    private val viewModel: ReviewViewModel by viewModels()
    private val commentAdapter: CommentPagingAdapter by lazy { CommentPagingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeListenLoadingForm()
        getDataFromAnotherFragment()
        setUpBackFragment()

        setUpRcvReview()
        listenStateLoadReview()
        loadReview()

        listenCreateComment()
        setUpEventSend()
    }

    private fun subscribeListenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            with(binding) {
                if (it) {
                    setStateVisibleView(View.VISIBLE, spinKitProgressBar)
                } else {
                    setStateVisibleView(View.GONE, spinKitProgressBar)
                }
            }
        })
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
            controllerMain.popBackStack()
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
                        viewModel.isLoading.value = true
                    }
                    is LoadState.Error -> {
                        setStateBgToView(
                            R.drawable.error,
                            AppConstants.MsgErr.GENERIC_ERR_MSG
                        )
                        viewModel.isLoading.value = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    }
                    is LoadState.NotLoading -> {
                        if (loadStates.append.endOfPaginationReached && commentAdapter.itemCount == 0) {
                            setStateBgToView(
                                R.drawable.empty_comment,
                                AppConstants.MsgErr.EMPTY_COMMENT
                            )
                        }
                        viewModel.isLoading.value = false
                    }
                    else -> viewModel.isLoading.value = false

                }
            }
        }
    }

    private fun loadReview() {
        if (reviewId == null) return
        lifecycleScope.launch {
            viewModel.getCommentPaging(reviewId!!).collectLatest {
                commentAdapter.submitData(it)
            }
        }
    }

    private fun setStateBgToView(idIcon: Int, msg: String) {
        binding.imgEmptyComment.setImageResource(idIcon)
        binding.txtEmptyComment.text = msg
        setStateVisibleView(View.VISIBLE, binding.imgEmptyComment)
        setStateVisibleView(View.VISIBLE, binding.txtEmptyComment)
    }

    private fun setUpEventSend() {
        binding.imgSend.setOnClickListener {
//            val flag = AppConstants.ActionClick.CREATE_COMMENT
            doActionClick(AppConstants.ActionClick.CREATE_COMMENT)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.CREATE_COMMENT -> {
                createComment()
            }
        }
    }

    private fun createComment() {
        if (reviewId == null) return
        val contentComment = binding.edtReply.text.toString().trim()
        if (TextUtils.isEmpty(contentComment)) {
            showLongToast(AppConstants.MsgInfo.CONTENT_REPLY_COMMENT_EMPTY)
            return
        }
        viewModel.createComment(reviewId!!, CommentPost(contentComment))
    }

    private fun listenCreateComment() {
        viewModel.dataStateCreateComment.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    viewModel.isLoading.value = false
                    Log.e(AppConstants.TAG.COMMENT, "listenCreateComment: ${it.message}")
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                }
                Status.SUCCESS -> {
                    viewModel.isLoading.value = false
                    binding.edtReply.setText("")
                    binding.edtReply.clearFocus()
                    commentAdapter.refresh()
                }
            }
        })
    }
}