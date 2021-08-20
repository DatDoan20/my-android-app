package com.doanducdat.shoppingapp.ui.onboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.ScreenSlidePagerAdapter
import com.doanducdat.shoppingapp.animation.ZoomOutPageTransformer
import com.doanducdat.shoppingapp.databinding.ActivityOnboardScreenBinding
import com.doanducdat.shoppingapp.ui.login.LoginActivity
import com.doanducdat.shoppingapp.ui.main.MainActivity
import com.doanducdat.shoppingapp.utils.AppConstants

class OnboardScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardScreenBinding
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboard_screen)
        viewPager2 = binding.viewPager2
        setUpViewPager()
        setUpIndicator()
        onNextViewpager()
    }

    private fun setUpViewPager() {
        val adapter: ScreenSlidePagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager2.adapter = adapter
        showContent(viewPager2.currentItem)
        viewPager2.isUserInputEnabled = false;
        viewPager2.setPageTransformer(ZoomOutPageTransformer())
    }

    private fun setUpIndicator() {
        binding.indicator3.setViewPager(viewPager2)
    }

    private fun onNextViewpager() {
        binding.btnNext.setOnClickListener {
            if (viewPager2.currentItem in 0..1) {
                viewPager2.currentItem = viewPager2.currentItem + 1
                showContent(viewPager2.currentItem)
            } else if (viewPager2.currentItem == 2) {
                openActivityLogin()
            }
        }
    }

    override fun onBackPressed() {
        if (viewPager2.currentItem == 0) return
        viewPager2.currentItem = viewPager2.currentItem - 1
        showContent(viewPager2.currentItem)
    }

    private fun showContent(currentItem: Int) {
        when (currentItem) {
            0 -> {
                setUpContent(
                    AppConstants.ContentOnboard.TITLE_FIRST_FRAGMENT,
                    AppConstants.ContentOnboard.DES_FIRST_FRAGMENT
                )
            }
            1 -> {
                setUpContent(
                    AppConstants.ContentOnboard.TITLE_SECOND_FRAGMENT,
                    AppConstants.ContentOnboard.DES_SECOND_FRAGMENT
                )
            }
            2 -> {
                setUpContent(
                    AppConstants.ContentOnboard.TITLE_THIRD_FRAGMENT,
                    AppConstants.ContentOnboard.DES_THIRD_FRAGMENT
                )
            }
        }
    }

    private fun setUpContent(title: String, des: String) {
        binding.txtTitle.text = title
        binding.txtDes.text = des
    }

    private fun openActivityLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}