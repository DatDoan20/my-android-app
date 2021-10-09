package com.doanducdat.shoppingapp.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CategoryPagerAdapter
import com.doanducdat.shoppingapp.databinding.FragmentCategoryBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.android.material.tabs.TabLayoutMediator


class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryBinding = FragmentCategoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenUpdateBadgeCountNotify(binding.myAppBarLayout.layoutNotification.imgRedDot)

        hideSearchPlate(binding.myAppBarLayout.searchView)
        //set up adapter Viewpager have to be in advance TabLayout
        setUpViewpagerCategory()
        //Tab Layout
        setUpTabLayoutWithViewPager()
        //
        setUpActionClick()

    }

    private fun setUpViewpagerCategory() {
        binding.viewPagerCategory.adapter = CategoryPagerAdapter(requireActivity())
    }

    private fun setUpTabLayoutWithViewPager() {
        TabLayoutMediator(binding.tabLayoutCategory, binding.viewPagerCategory) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = AppConstants.TextTab.WOMAN
                }
                1 -> tab.text = AppConstants.TextTab.MAN
            }
        }.attach()
    }

    private fun setUpActionClick() {
        // event click search
        val callbackOnSearch: () -> Unit = {
            controller.navigate(R.id.searchFragment)
        }
        setOnSearchView(binding.myAppBarLayout.searchView, callbackOnSearch)
    }

}