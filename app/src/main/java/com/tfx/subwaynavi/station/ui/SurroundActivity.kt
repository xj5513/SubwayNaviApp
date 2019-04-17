package com.tfx.subwaynavi.station.ui

import android.os.Bundle
import com.tfx.subwaynavi.base.BaseWebviewActivity
import kotlinx.android.synthetic.main.activity_metro_map.*

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/2
 *
 */
class SurroundActivity : BaseWebviewActivity(){

    private var mUrl = "http://47.254.42.238:81/zhoubian.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideToolbar()
        showLoadingDialog()
        mMetroWebView.loadUrl(mUrl)
    }

    override fun onPause() {
        super.onPause()
        mMetroWebView.loadUrl("about:blank")
    }

}