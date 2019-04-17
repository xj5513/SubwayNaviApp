package com.tfx.subwaynavi.base

import android.text.TextUtils
import android.util.Log
import com.tfx.subwaynavi.api.SubWayApi
import com.tfx.subwaynavi.libbase.network.retrofit.BaseRetrofit

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
open class BaseModel : BaseRetrofit() {

    var mServletApi : SubWayApi?= null

    init {
        mServletApi = mRetrofit?.create<SubWayApi>(com.tfx.subwaynavi.api.SubWayApi::class.java)
    }

    var mParams: MutableMap<String, String> = HashMap()

    override fun getCommonMap(): Map<String, String> {
        val map = HashMap<String,String>()
        return map
    }

    fun addParams(key: String, value: String) {
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "the key is null")
            return
        }
        mParams.put(key, value)
    }

    fun addParams(params: Map<String, String>?) {
        if (null == params) {
            Log.e(TAG, "the map is null")
            return
        }
        mParams.putAll(params)
    }

    companion object {
        private val TAG = "BaseModel"
    }
}