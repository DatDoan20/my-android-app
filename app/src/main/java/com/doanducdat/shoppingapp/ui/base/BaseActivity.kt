package com.doanducdat.shoppingapp.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.network.NetworkMonitorUtil

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB
    val controllerMain by lazy {
        (supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    private var backPressTime: Long = System.currentTimeMillis()
    private lateinit var toastExitApp: Toast

    private val networkMonitorUtil: NetworkMonitorUtil by lazy { NetworkMonitorUtil(this) }
    var availableNetwork: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getViewBinding())
        /*this fun will init result to return state network,
         that have to IN ADVANCE register in onResume()
         */
        subscribeListenNetwork()

        setUpView()

    }

    override fun onResume() {
        super.onResume()
        networkMonitorUtil.register()
    }

    abstract fun getViewBinding(): Int

    private fun subscribeListenNetwork() {
        networkMonitorUtil.result = { isAvailable ->
            //get value into variable to inherited classes use
            availableNetwork = isAvailable
            if (!isAvailable) {
                Toast.makeText(this, AppConstants.MsgErr.GENERIC_ERR_RESPONSE, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * children class inherit use this function as onCreate
     */
    abstract fun setUpView()

    override fun onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            toastExitApp.cancel() // cancel when toast still there after exit app
            super.onBackPressed()
            return
        } else {
            toastExitApp = Toast.makeText(
                this,
                AppConstants.MsgErr.EXIT_APP, Toast.LENGTH_SHORT
            )
            toastExitApp.show()
        }
        backPressTime = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()
        networkMonitorUtil.unregister()
    }
}