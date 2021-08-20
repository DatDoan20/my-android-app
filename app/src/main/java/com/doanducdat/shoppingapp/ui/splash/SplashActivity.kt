package com.doanducdat.shoppingapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.ui.login.LoginActivity
import com.doanducdat.shoppingapp.ui.onboard.OnboardScreenActivity
import com.doanducdat.shoppingapp.utils.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var myDataStore: MyDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadingSplashScreen()
    }

    private fun loadingSplashScreen() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            checkFirstTimeOpenApp()
        }
    }

    private suspend fun checkFirstTimeOpenApp() {
        val isFirstTimeOpenApp: Boolean? = myDataStore.firstTimeOpenAppFlow.first()
        openActivity(isFirstTimeOpenApp)
    }

    private fun openActivity(isFirstTimeOpenApp: Boolean?) {
        //null, true -> is first time open app
        if (isFirstTimeOpenApp != false) {
            startActivity(
                Intent(
                    this@SplashActivity,
                    OnboardScreenActivity::class.java
                )
            )
        } else {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
        finish()
    }
}