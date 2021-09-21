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
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.ui.main.order.OrderViewModel
import com.doanducdat.shoppingapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrdersBinding = FragmentOrdersBinding.inflate(inflater, container, false)

    val viewModel: OrderViewModel by viewModels()
    private val orderAdapter: OrderPagingAdapter = OrderPagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRcvOrders()
        listenStateLoadProduct()
        loadOrders()
    }

    private fun setUpRcvOrders() {
        binding.rcvOrders.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rcvOrders.adapter = orderAdapter
        orderAdapter.mySetOnClickOder {
//            controller.navigate(R.id.productFragment, bundleOf("PRODUCT" to it))
        }
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

}