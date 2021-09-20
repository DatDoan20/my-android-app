package com.doanducdat.shoppingapp.ui.main.cart

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doanducdat.shoppingapp.databinding.FragmentOrderBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants


class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)

    var stateErrName = false
    private var stateErrAddress = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickRadio()
        preOrder()
    }

    private fun preOrder() {
        //check validation not yet
        if (TextUtils.isEmpty(binding.edtName.text.toString())) {
            binding.edtName.requestFocus()
            showLongToast(AppConstants.MsgErr.NAME_ERR_MSG)
            return
        }
        if (TextUtils.isEmpty(binding.edtAddress.text.toString())) {
            binding.edtAddress.requestFocus()
            showLongToast(AppConstants.MsgErr.ADDRESS_ERR_MSG)
            return
        }
    }

    private fun setUpClickRadio() {
        binding.layoutCod.setOnClickListener {
            binding.rdoCod.isChecked = true
            binding.rdoMomo.isChecked = false
        }
        binding.layoutMomo.setOnClickListener {
            binding.rdoCod.isChecked = false
            binding.rdoMomo.isChecked = true
        }
        binding.rdoMomo.setOnClickListener {
            binding.rdoCod.isChecked = false
        }
        binding.rdoCod.setOnClickListener {
            binding.rdoMomo.isChecked = false
        }
    }

}