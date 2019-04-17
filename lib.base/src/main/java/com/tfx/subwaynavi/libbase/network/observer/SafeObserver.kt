package com.tfx.subwaynavi.libbase.network.observer

import android.accounts.NetworkErrorException
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.net.ParseException
import android.util.Log
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.tfx.subwaynavi.libbase.utils.UiUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/23
 *
 */
abstract class SafeObserver<T> : LifecycleObserver, Observer<T> {

    private var mLifecycle: Lifecycle ?=null
    private var mDisposable: Disposable? = null

    abstract fun onBaseError(t : Throwable)

    abstract fun onBaseNext(data : T)


    /*不传入Lifecycle，手动管理该Observer的生命周期*/
    constructor() {
        //unSafe
    }

    /*传入Lifecycle，自动保证回调安全*/
    constructor(lifecycle: Lifecycle) {
        //lifecycle safe.
        mLifecycle = lifecycle
    }

    override fun onSubscribe(d: Disposable) {
        mDisposable = d
        if (mLifecycle != null)
            mLifecycle?.addObserver(this) //加入到lifecycle观察者。
    }

    override fun onNext(data: T) {
        if(null != data){
            onBaseNext(data)
        }
    }

    override fun onError(e: Throwable) {

        val sb = StringBuilder()
        sb.append("请求失败：")
        if (e is NetworkErrorException || e is UnknownHostException || e is ConnectException) {
            sb.append("网络异常")
        } else if (e is SocketTimeoutException || e is InterruptedIOException || e is TimeoutException) {
            sb.append("请求超时")
        } else if (e is JsonSyntaxException) {
            sb.append("请求不合法")
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {   //  解析错误
            sb.append("解析错误")
        } else {
            UiUtil.showToast("onBaseError: " + e?.message)
            return
        }
        Log.e(TAG, "onBaseError: " + sb.toString())
        UiUtil.showToast( "onBaseError: " + sb.toString())
        e.printStackTrace()
        onEnd()
        onBaseError(e)
    }

    override fun onComplete() {
        onEnd()
    }

    private fun onEnd() {
        Log.i(TAG, "onEnd")
        onDisPose() //RxObserver结束后自动释放。
    }

    /*释放Rx观察者（在Lifecycle的ON_DESTROY事件发生时会自动调用该方法）*/
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDisPose() {
        Log.i(TAG, "dispose")
        if (mLifecycle != null)
            mLifecycle!!.removeObserver(this)//解除与lifecycle的绑定。
        if (mDisposable != null && (mDisposable?.isDisposed == false))
            mDisposable?.dispose()
    }

    companion object {
        val TAG = "SafeObserver"
    }
}