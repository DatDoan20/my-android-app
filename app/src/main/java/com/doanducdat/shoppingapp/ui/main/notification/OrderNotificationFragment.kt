package com.doanducdat.shoppingapp.ui.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.adapter.NotifyOrderPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentOrderNotificationBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderNotificationFragment : BaseFragment<FragmentOrderNotificationBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderNotificationBinding =
        FragmentOrderNotificationBinding.inflate(inflater, container, false)

    private val notifyOrderPagingAdapter = NotifyOrderPagingAdapter()
    private val viewModel: NotificationViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRcvNotifyOrder()
        listenLoadNotifyOrder()
        if(notifyOrderPagingAdapter.itemCount == 0) {
            loadNotifyOrder()
        }
    }

    private fun setUpRcvNotifyOrder() {
        binding.rcvNotifyOrder.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvNotifyOrder.adapter = notifyOrderPagingAdapter
    }

    private fun listenLoadNotifyOrder() {
        lifecycleScope.launch {
            notifyOrderPagingAdapter.loadStateFlow.collectLatest { loadStates ->
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

    private fun loadNotifyOrder() {
        lifecycleScope.launch {
            viewModel.getNotifyOrderPaging().collectLatest {
                notifyOrderPagingAdapter.submitData(it)
            }
        }
    }


}