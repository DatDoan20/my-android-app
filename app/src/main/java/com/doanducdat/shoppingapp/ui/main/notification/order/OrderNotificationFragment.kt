package com.doanducdat.shoppingapp.ui.main.notification.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.NotifyOrderPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentOrderNotificationBinding
import com.doanducdat.shoppingapp.databinding.ItemNotificationBinding
import com.doanducdat.shoppingapp.model.order.NotifyOrder
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.MyPopupMenu
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

    private val notifyOrderPagingAdapter by lazy { NotifyOrderPagingAdapter(requireContext()) }
    private val viewModel: OrderNotificationViewModel by viewModels()
    private val myPopupMenu by lazy { MyPopupMenu(requireContext()) }

    private var viewClickAble = true
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
            viewClickAble = !it
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

        setUpActionClickRcv()

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

    private fun loadNotifyOrder() {
        lifecycleScope.launch {
            viewModel.getNotifyOrderPaging().collectLatest {
                viewModel.myPagingNotifyOrder = it
                notifyOrderPagingAdapter.submitData(it)
            }
        }
    }

    private fun setStateBgToView(idIcon: Int, msg: String) {
        binding.imgEmptyOrderNotify.setImageResource(idIcon)
        binding.txtEmptyOrderNotify.text = msg
        setStateVisibleView(View.VISIBLE, binding.imgEmptyOrderNotify)
        setStateVisibleView(View.VISIBLE, binding.txtEmptyOrderNotify)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpActionClickRcv() {
        // val flag = AppConstants.ActionClick.READ_NOTIFY_COMMENT

        notifyOrderPagingAdapter.mySetOnClickRead { notifyOrder, _, itemNotificationBinding ->
            if (viewClickAble) {
                with(itemNotificationBinding) {

                    delete(notifyOrder)
                    deleteAll()
                    markAsRead(notifyOrder, itemNotificationBinding)
                    markAsReadAll()

                    //use txtName to anchorView for PopupMenu
                    myPopupMenu.showMenu(txtName, R.menu.popup_menu_item_notification)
                }
            }
        }
    }


    private fun delete(notifyOrder: NotifyOrder) {
        myPopupMenu.setOnClickDelete {
            //change UI client side
            viewModel.deleteClientSideNotifyOrder(notifyOrder.id, notifyOrderPagingAdapter)
            //call api update server side
            viewModel.deleteNotifyOrder(notifyOrder.id)
        }
    }

    private fun deleteAll() {
        myPopupMenu.setOnClickDeleteAll {
            viewModel.dataStateDeleteAllOrder.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> viewModel.isLoading.value = true
                    Status.ERROR -> {
                        viewModel.isLoading.value = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                        Log.e(AppConstants.TAG.ORDER_NOTI, "deleteAll: ${it.message}")
                    }
                    Status.SUCCESS -> {
                        //clear all item in paging list
                        notifyOrderPagingAdapter.submitData(lifecycle, PagingData.empty())
                        setStateBgToView(
                            R.drawable.empty_notification,
                            AppConstants.MsgErr.EMPTY_NOTIFICATION
                        )
                        viewModel.isLoading.value = false
                    }

                }
            })
            viewModel.deleteAllNotifyOrder()
        }
    }

    private fun markAsRead(
        notifyOrder: NotifyOrder,
        itemNotificationBinding: ItemNotificationBinding
    ) {
        with(itemNotificationBinding) {
            myPopupMenu.setOnClickMarkAsRead {
                //time notify > time read all can update
                if (notifyOrder.updatedAt.after(InfoUser.currentUser?.readAllOrderNoti)) {
                    if (!notifyOrder.receiverIds[0].readState) { //unRead
                        //change state client side
                        notifyOrderPagingAdapter.setStateReadDot(imgBlueDotReadState)
                        notifyOrderPagingAdapter
                            .setColorReadTextView(txtName, txtTimeComment, txtContentNotify)
                        notifyOrder.receiverIds[0].readState = true

                        //call api update server side
                        viewModel.checkReadNotifyOrder(notifyOrder.id)
                    }
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun markAsReadAll() {
        myPopupMenu.setOnClickMarkAsReadAll {
            viewModel.dataStateReadAllOrder.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> viewModel.isLoading.value = true
                    Status.ERROR -> {
                        viewModel.isLoading.value = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                        Log.e(AppConstants.TAG.ORDER_NOTI, "markAsReadAll: ${it.message}")
                    }
                    Status.SUCCESS -> {
                        InfoUser.currentUser = it.response!!.data
                        notifyOrderPagingAdapter.notifyDataSetChanged()
                        viewModel.isLoading.value = false
                    }

                }
            })
            viewModel.checkReadAllNotifyOrder()
        }
    }
}