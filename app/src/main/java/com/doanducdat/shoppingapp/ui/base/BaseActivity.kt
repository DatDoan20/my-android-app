package com.doanducdat.shoppingapp.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.doanducdat.shoppingapp.utils.AppConstants

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB

    private var backPressTime: Long = System.currentTimeMillis()
    private lateinit var toastExitApp: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getViewBinding())
        setUpView()
    }

    abstract fun getViewBinding(): Int
    abstract fun setUpView()

    override fun onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            toastExitApp.cancel() // cancel when toast still there after exit app
            super.onBackPressed()
            return
        } else {
            toastExitApp = Toast.makeText(
                this,
                AppConstants.MsgInfo.EXIT_APP, Toast.LENGTH_SHORT
            )
            toastExitApp.show()
        }
        backPressTime = System.currentTimeMillis()
    }
}