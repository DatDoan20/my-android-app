package com.doanducdat.shoppingapp.ui.main.order.management

import android.annotation.SuppressLint
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
import com.doanducdat.shoppingapp.adapter.OrderPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentOrdersBinding
import com.doanducdat.shoppingapp.module.order.Order
import com.doanducdat.shoppingapp.module.response.Status
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.ui.main.order.OrderViewModel
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.dialog.MyYesNoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersFragment : BaseFragment<FragmentOrdersBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrdersBinding = FragmentOrdersBinding.inflate(inflater, container, false)

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    val viewModel: OrderViewModel by viewModels()
    private val orderAdapter: OrderPagingAdapter = OrderPagingAdapter()
    private val myYesNoDialog by lazy { MyYesNoDialog(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenLoadingForm()

        setUpRcvOrders()
        listenStateLoadProduct()
        loadOrders()

        setUpActionClick()

        listenCancelOrder()
    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                setStateProgressBar(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateProgressBar(View.GONE, binding.spinKitProgressBar)
            }
        })
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
                        viewModel.isLoading.value = true
                    }
                    is LoadState.Error -> {
                        viewModel.isLoading.value = false
                    }
                    else -> viewModel.isLoading.value = false
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
            cancelOrder(it)
        }
        orderAdapter.mySetOnClickViewDetail {
        }
        orderAdapter.mySetOnClickNavWriteReview {
            controller.navigate(R.id.showProductReviewFragment, bundleOf("ORDER" to it))
        }
    }

    private fun cancelOrder(order: Order) {
        myYesNoDialog.setText(AppConstants.MsgInfo.CONFIRM_DELETE_ORDER)
        myYesNoDialog.mySetOnClickYes {
            order.id?.let { viewModel.cancelOrder(it) }
        }
        myYesNoDialog.show()
    }

    private fun listenCancelOrder() {
        viewModel.dataStateCancel.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    myYesNoDialog.setStateDialog(false)
                    myYesNoDialog.setStateProgressBar(View.VISIBLE)
                }
                Status.ERROR -> {
                    myYesNoDialog.dismiss()
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    Log.e(AppConstants.TAG.ORDER_MANAGEMENT, "listenCancelOrder: ${it.message}")
                }
                Status.SUCCESS -> {
                    myYesNoDialog.dismiss()
                    showLongToast(AppConstants.MsgInfo.DELETE_ORDER)
                    orderAdapter.refresh()
                }
            }
        })
    }
}