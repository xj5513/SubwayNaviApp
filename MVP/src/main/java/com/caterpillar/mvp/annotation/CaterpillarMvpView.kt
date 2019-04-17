package com.caterpillar.mvp.annotation

import java.lang.ref.WeakReference

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 *
 * view代理类，的顶级父类
 */

abstract class CaterpillarMvpView<V> {

    private var mWRView: WeakReference<V> ?= null

    fun CaterpillarMvpView(view: V) {
        mWRView = WeakReference<V>(view)
    }

    @Throws(Exception::class)
    fun getView(): V? {
        if (mWRView != null && mWRView?.get() != null) {
            return mWRView?.get()
        }
        throw Exception("view 引用已经被GC回收！")
    }
}