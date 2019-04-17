package com.tfx.subwaynavi.libbase.network.retrofit

import com.tfx.subwaynavi.libbase.network.convert.CustomGsonConverterFactory
import com.tfx.subwaynavi.libbase.network.intercepter.BasicParamsInterceptor
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import com.tfx.subwaynavi.libbase.network.config.HttpServletAddress


/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
abstract class BaseRetrofit {
    var mRetrofit : Retrofit ?= null

    companion object {
        val DEFAULT_TIME = 15L       //默认超时时间
        val RETRY_TIME = 1L          //重订阅次数
    }

    init {
        if(mRetrofit == null){
            var builder = OkHttpClient().newBuilder()
            builder.readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            builder.connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            //设置拦截器
            //builder.addInterceptor(BasicParamsInterceptor.Builder().addParamsMap(getCommonMap()).build())
            var okHttpClient = builder.build()
            mRetrofit = Retrofit.Builder()
                    .baseUrl(HttpServletAddress.instance.servletAddress)
                    .client(okHttpClient)
                    .addConverterFactory(CustomGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
    }

    fun <T> toSubscribe(observable : Observable<T>? ,observer: Observer<T> ) {
        observable?.subscribeOn(Schedulers.io())    // 指定subscribe()发生在IO线程
                ?.observeOn(AndroidSchedulers.mainThread())  // 指定Subscriber的回调发生在io线程
                ?.timeout(DEFAULT_TIME, TimeUnit.SECONDS)    //重连间隔时间
                ?.retry(RETRY_TIME)
//          .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
//                @Override
//                public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
//                    return null;
//                }
//           })
                ?.subscribe(observer)  //订阅
    }

    //公共参数
    abstract fun getCommonMap(): Map<String, String>

    protected fun <T> getPresent(cls: Class<T>): T? {
        var instance: T?
        try {
            instance = cls.newInstance()
            return if (instance == null) {
                null
            } else instance
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return null
    }

}