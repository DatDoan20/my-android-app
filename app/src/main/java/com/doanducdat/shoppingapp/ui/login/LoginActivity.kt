package com.doanducdat.shoppingapp.ui.login

import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ActivityLoginBinding
import com.doanducdat.shoppingapp.ui.base.BaseActivity
import com.doanducdat.shoppingapp.utils.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @Inject
    lateinit var myDataStore: MyDataStore

    override fun getViewBinding(): Int = R.layout.activity_login

    override fun setUpView() {
        checkFirstTimeOpenApp()
    }

    private fun checkFirstTimeOpenApp() {
        CoroutineScope(Dispatchers.IO).launch {
            val isFirstTimeOpenApp: Boolean? = myDataStore.firstTimeOpenAppFlow.first()
            if (isFirstTimeOpenApp != false) {
                myDataStore.writeCheckFirstTimeOpenApp(false)
            }
        }
    }


}