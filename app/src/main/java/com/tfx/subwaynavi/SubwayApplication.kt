package com.tfx.subwaynavi

import com.tfx.subwaynavi.libbase.BaseApplication
import com.tfx.subwaynavi.libbase.network.config.HttpServletAddress

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/2
 *
 */
class SubwayApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        HttpServletAddress.instance.offlineAddress = "http://47.254.42.238:81/subwaynavi/"
        HttpServletAddress.instance.onlineAddress = "http://47.254.42.238:81/subwaynavi/"
    }
}