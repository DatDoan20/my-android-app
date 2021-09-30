package com.doanducdat.shoppingapp.ui.main.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.doanducdat.shoppingapp.databinding.FragmentOrderNotificationBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment


class OrderNotificationFragment : BaseFragment<FragmentOrderNotificationBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderNotificationBinding =
        FragmentOrderNotificationBinding.inflate(inflater, container, false)

}