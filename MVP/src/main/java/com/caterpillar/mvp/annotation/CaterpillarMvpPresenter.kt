package com.caterpillar.mvp.annotation

import java.lang.ref.WeakReference

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 *
 * presenter代理类，的顶级父类
 */

abstract class CaterpillarMvpPresenter<V , P> {

    private lateinit var mWRPresenter: WeakReference<P>
    private lateinit var mViewProxy: CaterpillarMvpView<V>

    fun CaterpillarMvpPresenter(view: V, presenter: P) {
        mViewProxy = createViewProxy(view)
        mWRPresenter = WeakReference<P>(presenter)
    }

    /**
     * 创建view层的代理类
     *
     * @param view 具体的view
     * @return view的代理类
     */
    abstract fun createViewProxy(view: V): CaterpillarMvpView<V>

    /**
     * 获取view层的代理类
     *
     * @return 获取view的代理类
     */
    fun getViewProxy(): CaterpillarMvpView<V> {
        return mViewProxy
    }

    /**
     * 获取presenter
     *
     * @return 具体的presenter
     * @throws Exception 弱引用被回收异常
     */
    @Throws(Exception::class)
    fun getPresenter(): P? {
        if (mWRPresenter != null && mWRPresenter.get() != null) {
            return mWRPresenter.get()
        }
        throw Exception("presenter 引用已经被GC回收！")
    }
}