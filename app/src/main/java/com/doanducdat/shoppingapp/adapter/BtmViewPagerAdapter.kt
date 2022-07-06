package com.doanducdat.shoppingapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BtmViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private var arrFragment: ArrayList<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return arrFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return arrFragment[position]
    }
}