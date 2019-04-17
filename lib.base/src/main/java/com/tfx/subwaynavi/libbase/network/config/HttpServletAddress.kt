package com.tfx.subwaynavi.libbase.network.config

import android.text.TextUtils



/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */

class HttpServletAddress private constructor() {

    var onlineAddress : String = ""   //线上地址
    var offlineAddress: String = ""  //线下地址

    val servletAddress: String?
        get() = if (TextUtils.isEmpty(onlineAddress)) offlineAddress else onlineAddress

    private object SingletonHolder {
        val INSTANCE = HttpServletAddress()
    }

    companion object {

        val instance: HttpServletAddress
            get() = SingletonHolder.INSTANCE
    }
}