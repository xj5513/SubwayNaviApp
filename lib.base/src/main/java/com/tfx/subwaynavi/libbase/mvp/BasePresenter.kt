package com.tfx.subwaynavi.libbase.mvp

import java.lang.ref.WeakReference

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/23
 *
 */

open class BasePresenter<V : BasePresenterView> {

    /**
     * 持有UI接口的弱引用
     */
    private var mViewRef: WeakReference<V> ? = null

    val view: V?
        get() = if (mViewRef != null) {
            mViewRef?.get()
        } else {
            null
        }

    fun attachView(view: V) {
        mViewRef = WeakReference(view)
    }

    /**
     * 解绑
     */
    fun detach() {
        if (mViewRef != null) {
            mViewRef?.clear()
            mViewRef = null
        }
    }
}