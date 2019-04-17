package com.tfx.subwaynavi.libbase.network.convert

import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import okhttp3.RequestBody
import okhttp3.ResponseBody
import com.google.gson.Gson
import retrofit2.Converter
import java.lang.reflect.Type


/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
class CustomGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    init {
        if (null == gson) {
            throw NullPointerException("gson is null")
        }
    }


    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonRequestBodyConverter(gson, adapter)
    }

    companion object {

        @JvmOverloads
        fun create(gson: Gson = Gson()): CustomGsonConverterFactory {
            return CustomGsonConverterFactory(gson)
        }
    }

}