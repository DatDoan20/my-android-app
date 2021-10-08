package com.doanducdat.shoppingapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doanducdat.shoppingapp.ui.main.notification.comment.CommentNotificationFragment
import com.doanducdat.shoppingapp.ui.main.notification.order.OrderNotificationFragment

class NotificationPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CommentNotificationFragment()
            1 -> return OrderNotificationFragment()
        }
        return CommentNotificationFragment()
    }
}