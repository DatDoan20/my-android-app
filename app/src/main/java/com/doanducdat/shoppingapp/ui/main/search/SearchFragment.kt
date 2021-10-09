package com.doanducdat.shoppingapp.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doanducdat.shoppingapp.databinding.FragmentSearchBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenUpdateBadgeCountNotify(binding.myAppBarLayout.layoutNotification.imgRedDot)

        setUpSearchView()
    }

    private fun setUpSearchView() {
        binding.myAppBarLayout.searchView.requestFocus()
        binding.myAppBarLayout.searchView.showKeyboard()
        hideSearchPlate(binding.myAppBarLayout.searchView)
    }





}