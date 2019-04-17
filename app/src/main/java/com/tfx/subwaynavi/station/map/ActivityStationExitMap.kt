package com.tfx.subwaynavi.station.map

import android.os.Bundle
import com.tfx.subwaynavi.base.BaseWebviewActivity
import kotlinx.android.synthetic.main.activity_metro_map.*

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/23
 *
 */
class ActivityStationExitMap : BaseWebviewActivity() {

    private var bdid = ""
    //private var mUrl = "http://123.206.49.147/guideonev5/guideone/index.html?bdid="

    private var mUrl = "http://101.200.75.28/hospital/resource/yingguo/guideone/index.html?screen=horizontal&deviceId=1&bdid="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        showLoadingDialog()

        bdid = intent?.getStringExtra("bdid")?:""

        mUrl += bdid

        mMetroWebView.loadUrl(mUrl)

    }
}