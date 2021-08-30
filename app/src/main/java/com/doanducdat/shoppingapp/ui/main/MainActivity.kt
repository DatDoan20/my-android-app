package com.doanducdat.shoppingapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.bubbleBtmNvgMain.setNavigationChangeListener { _, position ->
            when (position) {
                0 -> { //home
                }
                1 -> {//cate
                }
                2 -> {//search
                }
                3 -> {//cart
                }
                4 -> {//profile
                }
            }
        }
    }
}