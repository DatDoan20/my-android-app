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
        listenLoadingForm()

        setUpRcvNotifyOrder()
        listenLoadNotifyOrder()
        if (notifyOrderPagingAdapter.itemCount == 0) {
            loadNotifyOrder()
        }
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
                        viewModel.isLoading.value = true
                    }
                    is LoadState.Error -> {
                        viewModel.isLoading.value = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    }
                    is LoadState.NotLoading -> {
                        if (loadStates.append.endOfPaginationReached &&
                            notifyOrderPagingAdapter.itemCount == 0
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
        binding.imgEmptyOrderNotify.setImageResource(idIcon)
        binding.txtEmptyOrderNotify.text = msg
        setStateVisibleView(View.VISIBLE, binding.imgEmptyOrderNotify)
        setStateVisibleView(View.VISIBLE, binding.txtEmptyOrderNotify)
    }

    private fun loadNotifyOrder() {
        lifecycleScope.launch {
            viewModel.getNotifyOrderPaging().collectLatest {
                notifyOrderPagingAdapter.submitData(it)
            }
        }
    }


}