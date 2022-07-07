package com.doanducdat.shoppingapp.ui.main.order

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.ActivityVnPayBinding
import com.doanducdat.shoppingapp.model.VnPay
import com.doanducdat.shoppingapp.model.order.Order
import com.doanducdat.shoppingapp.ui.base.BaseActivity


class VnPayActivity : BaseActivity<ActivityVnPayBinding>() {

    lateinit var order: Order

    override fun getViewBinding(): Int = R.layout.activity_vn_pay

    override fun setUpView() {
        val bundle = intent.extras
        setUpWebView(bundle?.get("ORDER_TOTAL_PAYMENT").toString())

    }

    private fun setUpWebView(totalPayment: String?) {
        val paymentVnpay = VnPay(totalPayment)
        val callback = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d("TEST", "${request?.url}");
                if(request?.url.toString().contains("ReturnUrl", true)){
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                return super.shouldOverrideUrlLoading(view, request)

            }

        }
        binding.wvVnPay.settings.setSupportZoom(true)
        binding.wvVnPay.webViewClient = callback
        binding.wvVnPay.settings.javaScriptEnabled = true
        binding.wvVnPay.clearCache(true)
        binding.wvVnPay.loadUrl(paymentVnpay.getUrl())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (binding.wvVnPay.canGoBack()) {
                    binding.wvVnPay.goBack();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event)

    }


}