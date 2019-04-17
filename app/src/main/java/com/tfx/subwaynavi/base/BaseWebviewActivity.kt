package com.tfx.subwaynavi.base

import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.libbase.base.BaseActivity
import kotlinx.android.synthetic.main.activity_metro_map.*

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/5
 *
 */
open class BaseWebviewActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        mMetroWebView.setBackgroundColor(Color.TRANSPARENT)


        //设置WebView支持JavaScript
        mMetroWebView.settings.javaScriptEnabled = true
        mMetroWebView.addJavascriptInterface(this, "native")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMetroWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            //解决android5.0以上不显示无后缀名的图片的问
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mMetroWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        mMetroWebView.settings.setSupportZoom(true)
        // support local storage
        mMetroWebView.settings.domStorageEnabled = true
        mMetroWebView.settings.allowFileAccess = true
        mMetroWebView.clearCache(true)
        mMetroWebView.clearHistory()
        mMetroWebView.settings.loadsImagesAutomatically = true

        mMetroWebView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                handler?.proceed()
                Log.e("onReceivedSslError: ",""+error?.primaryError)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                mMetroWebView.reload()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                closeLoadingDialog()
            }
        }

        mMetroWebView.webChromeClient = object : WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                mWebTitle.text = title
            }
        }

        mBtnBack.setOnClickListener { goBack() }
    }

    override fun onBackPressed() {
        goBack()
    }

    private fun goBack(){
        if(mMetroWebView.canGoBack()){
            mMetroWebView.goBack()
        }else{
            finish()
        }
    }


    override fun getMainContentResId(): Int = R.layout.activity_metro_map
}