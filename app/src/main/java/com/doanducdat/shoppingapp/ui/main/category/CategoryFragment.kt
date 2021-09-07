package com.doanducdat.shoppingapp.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doanducdat.shoppingapp.adapter.CategoryPagerAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCategoryBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryBinding = FragmentCategoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Viewpager
        setUpViewpagerCategory()
        //Tab Layout
        setUpTabLayoutWithViewPager()


    }

    private fun setUpViewpagerCategory() {
        binding.viewPagerCategory.adapter = CategoryPagerAdapter(requireActivity())
    }

    private fun setUpTabLayoutWithViewPager() {
        TabLayoutMediator(binding.tabLayoutCategory, binding.viewPagerCategory) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Nữ"
                }
                1 -> tab.text = "Nam"
            }
        }.attach()
    }
}