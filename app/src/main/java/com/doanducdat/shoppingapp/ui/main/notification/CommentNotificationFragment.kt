package com.doanducdat.shoppingapp.ui.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.NotifyCommentPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCommentNotificationBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentNotificationFragment : BaseFragment<FragmentCommentNotificationBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCommentNotificationBinding =
        FragmentCommentNotificationBinding.inflate(inflater, container, false)

    private val viewModel: NotificationViewModel by viewModels()
    private val notifyCommentAdapter = NotifyCommentPagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenLoadingForm()

        setUpRcvNotifyComment()
        listenLoadNotifyComment()
        loadNotifyComment()
    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                setStateVisibleView(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateVisibleView(View.GONE, binding.spinKitProgressBar)
            }
        })
    }

    private fun listenLoadNotifyComment() {
        lifecycleScope.launch {
            notifyCommentAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> viewModel.isLoading.value = true
                    is LoadState.Error -> {
                        viewModel.isLoading.value = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    }
                    is LoadState.NotLoading -> {
                        if (loadStates.append.endOfPaginationReached &&
                            notifyCommentAdapter.itemCount == 0
                        ) {
                            setStateBgToView(
                                R.drawable.empty_notification,
                                AppConstants.MsgErr.EMPTY_NOTIFICATION
                            )
                        }
                        viewModel.isLoading.value = false
                    }
                }
            }
        }
    }

    private fun setStateBgToView(idIcon: Int, msg: String) {
        binding.imgEmptyCommentNotify.setImageResource(idIcon)
        binding.txtEmptyCommentNotify.text = msg
        setStateVisibleView(View.VISIBLE, binding.imgEmptyCommentNotify)
        setStateVisibleView(View.VISIBLE, binding.txtEmptyCommentNotify)
    }

    private fun loadNotifyComment() {
        lifecycleScope.launch {
            viewModel.getNotifyCommentPaging().collectLatest {
                notifyCommentAdapter.submitData(it)
            }
        }
    }

    private fun setUpRcvNotifyComment() {
        binding.rcvNotifyComment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvNotifyComment.adapter = notifyCommentAdapter
    }
}