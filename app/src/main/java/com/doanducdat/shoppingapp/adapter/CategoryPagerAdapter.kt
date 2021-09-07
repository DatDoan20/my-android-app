package com.doanducdat.shoppingapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doanducdat.shoppingapp.ui.main.category.ManCategoryFragment
import com.doanducdat.shoppingapp.ui.main.category.WomanCategoryFragment

class CategoryPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return WomanCategoryFragment()
            1 -> return ManCategoryFragment()
        }
        return WomanCategoryFragment()
    }
}