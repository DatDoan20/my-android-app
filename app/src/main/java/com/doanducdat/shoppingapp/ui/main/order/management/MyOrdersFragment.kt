package com.doanducdat.shoppingapp.ui.main.order.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.OrderPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentOrdersBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.ui.main.order.OrderViewModel
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.dialog.MyYesNoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersFragment : BaseFragment<FragmentOrdersBinding>(), MyActionApp {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrdersBinding = FragmentOrdersBinding.inflate(inflater, container, false)

    val viewModel: OrderViewModel by viewModels()
    private val orderAdapter: OrderPagingAdapter = OrderPagingAdapter()
    private val myYesNoDialog by lazy { MyYesNoDialog(requireContext()) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRcvOrders()
        listenStateLoadProduct()
        loadOrders()
        setUpActionClick()
    }

    private fun setUpRcvOrders() {
        binding.rcvOrders.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvOrders.adapter = orderAdapter

    }

    private fun listenStateLoadProduct() {
        lifecycleScope.launch {
            orderAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        setStateProgressBar(View.VISIBLE, binding.spinKitProgressBar)
                    }
                    is LoadState.Error -> {
                        setStateProgressBar(View.GONE, binding.spinKitProgressBar)
                    }
                    else -> setStateProgressBar(View.GONE, binding.spinKitProgressBar)
                }
            }
        }
    }

    private fun loadOrders() {
        lifecycleScope.launch {
            viewModel.getOrderPaging().collectLatest {
                orderAdapter.submitData(it)
            }
        }
    }

    private fun setUpActionClick() {
        orderAdapter.mySetOnClickCancelOrder {
            doActionClick(AppConstants.ActionClick.CANCEL_ORDER)
        }
        orderAdapter.mySetOnClickViewDetail {
            doActionClick(AppConstants.ActionClick.NAV_DETAIL_ORDER)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.CANCEL_ORDER -> {
                myYesNoDialog.setText(AppConstants.MsgInfo.CONFIRM_DELETE_ORDER)
                myYesNoDialog.show()
            }
            AppConstants.ActionClick.NAV_DETAIL_ORDER -> {

            }
        }
    }
}