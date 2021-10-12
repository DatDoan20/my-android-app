package com.doanducdat.shoppingapp.ui.main.notification.comment

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
import com.doanducdat.shoppingapp.adapter.NotifyCommentPagingAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCommentNotificationBinding
import com.doanducdat.shoppingapp.databinding.ItemNotificationBinding
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.model.review.NotifyComment
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.handler.HandlerPopupMenu
import com.doanducdat.shoppingapp.utils.handler.HandlerViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CommentNotificationFragment : BaseFragment<FragmentCommentNotificationBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCommentNotificationBinding =
        FragmentCommentNotificationBinding.inflate(inflater, container, false)

    private val handlerViewState by lazy { HandlerViewState(requireContext()) }
    private val viewModelComment: CommentNotificationViewModel by viewModels()
    private val notifyCommentAdapter by lazy { NotifyCommentPagingAdapter(requireContext()) }
    private var viewClickAble = true
    private val myPopupMenu by lazy { HandlerPopupMenu(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenLoadingForm()

        setUpRcvNotifyComment()
        listenLoadNotifyComment()
        loadNotifyComment()
    }

    private fun listenLoadingForm() {
        viewModelComment.isLoading.observe(viewLifecycleOwner, {
            viewClickAble = !it
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
                    is LoadState.Loading -> viewModelComment.isLoading.value = true
                    is LoadState.Error -> {
                        viewModelComment.isLoading.value = false
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
                        } else {
                            notificationShareViewModel.countNumberUnReadNotifyComment(
                                notifyCommentAdapter
                            )
                        }
                        viewModelComment.isLoading.value = false
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
            viewModelComment.getNotifyCommentPaging().collectLatest {
                viewModelComment.myPagingNotifyComment = it
                notifyCommentAdapter.submitData(it)
            }
        }
    }

    private fun setUpRcvNotifyComment() {
        binding.rcvNotifyComment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvNotifyComment.adapter = notifyCommentAdapter
        setUpActionClickRcv()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpActionClickRcv() {
//         val flag = AppConstants.ActionClick.READ_NOTIFY_ORDER

        notifyCommentAdapter.mySetOnClickRead { notifyComment, itemNotificationBinding ->
            if (viewClickAble) {
                with(itemNotificationBinding) {
                    delete(notifyComment)
                    deleteAll()
                    markAsRead(notifyComment, itemNotificationBinding)
                    markAsReadAll()
                    //use txtName to anchorView for PopupMenu
                    myPopupMenu.showMenu(txtName, R.menu.popup_menu_item_notification)
                }
            }
        }
    }

    private fun delete(notifyComment: NotifyComment) {
        myPopupMenu.setOnClickDelete {
            viewModelComment.deleteNotifyComment(notifyComment.id)
            viewModelComment.deleteClientSideNotifyComment(notifyComment.id, notifyCommentAdapter)
            if (!notifyComment.receiverIds[0].readState) { //UnRead
                notificationShareViewModel.minusOneDigitCountUnReadNotifyComment()
            }
        }
    }

    private fun deleteAll() {
        myPopupMenu.setOnClickDeleteAll {
            viewModelComment.dataStateDeleteAllComment.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> viewModelComment.isLoading.value = true
                    Status.ERROR -> {
                        viewModelComment.isLoading.value = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                        Log.e(AppConstants.TAG.COMMENT_NOTI, "deleteAll: ${it.message}")
                    }
                    Status.SUCCESS -> {
                        notifyCommentAdapter.submitData(lifecycle, PagingData.empty())
                        notificationShareViewModel.clearCountUnReadNotifyComment()
                        setStateBgToView(
                            R.drawable.empty_notification,
                            AppConstants.MsgErr.EMPTY_NOTIFICATION
                        )
                        viewModelComment.isLoading.value = false
                    }
                }
            })
            viewModelComment.deleteAllNotifyComment()
        }
    }

    private fun markAsRead(
        notifyComment: NotifyComment,
        itemNotificationBinding: ItemNotificationBinding
    ) {
        myPopupMenu.setOnClickMarkAsRead {
            with(itemNotificationBinding) {
                //time notify > time read all can update
                if (notifyComment.updatedAt.after(InfoLocalUser.currentUser?.readAllOrderNoti)) {
                    if (!notifyComment.receiverIds[0].readState) { //unRead
                        //call api update server side
                        viewModelComment.checkReadNotifyComment(notifyComment.id)
                        //change state view client side
                        handlerViewState.setStateReadDot(imgBlueDotReadState)
                        handlerViewState.setColorReadTextView(txtName, txtTimeComment, txtContentNotify)
                        notifyComment.receiverIds[0].readState = true
                        //update badge
                        notificationShareViewModel.minusOneDigitCountUnReadNotifyComment()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun markAsReadAll() {
        myPopupMenu.setOnClickMarkAsReadAll {
            viewModelComment.dataStateReadAllComment.observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> viewModelComment.isLoading.value = true
                    Status.ERROR -> {
                        viewModelComment.isLoading.value = false
                        showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                        Log.e(AppConstants.TAG.COMMENT_NOTI, "markAsReadAll: ${it.message}")
                    }
                    Status.SUCCESS -> {
                        /* return readAllCommentNoti -> update that is time now */
                        InfoLocalUser.currentUser?.readAllOrderNoti = Calendar.getInstance().time
                        notifyCommentAdapter.notifyDataSetChanged()
                        notificationShareViewModel.clearCountUnReadNotifyComment()
                        viewModelComment.isLoading.value = false
                    }
                }
            })
            viewModelComment.checkReadAllNotifyComment()
        }
    }
}