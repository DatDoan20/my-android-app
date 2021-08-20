package com.doanducdat.shoppingapp.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ActivityLoginBinding
import com.doanducdat.shoppingapp.utils.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var myDataStore: MyDataStore

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

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