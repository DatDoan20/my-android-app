package com.doanducdat.shoppingapp.ui.main.order.management

import android.view.LayoutInflater
import android.view.ViewGroup
import com.doanducdat.shoppingapp.databinding.FragmentOrderHandlingBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment


class OrderHandlingFragment : BaseFragment<FragmentOrderHandlingBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderHandlingBinding =
        FragmentOrderHandlingBinding.inflate(inflater, container, false)

}