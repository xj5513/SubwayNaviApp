package com.tfx.subwaynavi.libbase.network.intercepter

import android.text.TextUtils
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.util.Map.*


/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
class BasicParamsInterceptor private constructor() : Interceptor {

    internal var queryParamsMap: MutableMap<String, String> = HashMap()
    internal var paramsMap: MutableMap<String, String> = HashMap()
    internal var headerParamsMap: MutableMap<String, String> = HashMap()
    internal var headerLinesList: MutableList<String> = ArrayList()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val body = request.body()
        // process post body inject
        if (paramsMap != null && paramsMap.isNotEmpty() && request.method() == "POST" && null != body) {
            var newBody: RequestBody? = null
            if (body is FormBody) {
                newBody = addParamsToFormBody(body)
            } else if (request.body() is MultipartBody) {
                newBody = addParamsToMultipartBody(body as MultipartBody)
            }
            if (null != newBody) {
                val newRequest = request.newBuilder()
                        .url(request.url())
                        .method(request.method(), newBody)
                        .build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(request)
    }

    private fun addParamsToMultipartBody(body: MultipartBody): MultipartBody {
        val builder = MultipartBody.Builder()
        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val iterator = paramsMap.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next() as Entry<*, *>
            multipartBuilder.addFormDataPart(entry.key as String, entry.value as String)
        }

        val oldParts = body.parts()
        if (oldParts != null && oldParts.size > 0) {
            for (part in oldParts) {
                multipartBuilder.addPart(part)
            }
        }
        return builder.build()
    }

    private fun addParamsToFormBody(body: FormBody): FormBody {
        val builder = FormBody.Builder()
        if (paramsMap.isNotEmpty()) {
            val iterator = paramsMap!!.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Entry<*, *>
                builder.add(entry.key as String, entry.value as String)
            }
        }

        val paramSize = body.size()
        if (paramSize > 0) {
            for (i in 0 until paramSize) {
                builder.add(body.name(i), body.value(i))
            }
        }
        return builder.build()
    }

    private fun canInjectIntoBody(request: Request?): Boolean {
        if (request == null) {
            return false
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false
        }
        val body = request.body() ?: return false
        val mediaType = body.contentType() ?: return false
        return TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")
    }

    // func to inject params into url
    private fun injectParamsIntoUrl(httpUrlBuilder: HttpUrl.Builder, requestBuilder: Request.Builder, paramsMap: Map<String, String>): Request? {
        if (paramsMap.isNotEmpty()) {
            val iterator = paramsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as java.util.Map.Entry<*, *>
                httpUrlBuilder.addQueryParameter(entry.key as String, entry.value as String)
            }
            requestBuilder.url(httpUrlBuilder.build())
            return requestBuilder.build()
        }

        return null
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }

    class Builder {

        internal var interceptor: BasicParamsInterceptor

        init {
            interceptor = BasicParamsInterceptor()
        }

        fun addParam(key: String, value: String): Builder {
            interceptor.paramsMap.put(key, value)
            return this
        }

        fun addParamsMap(paramsMap: Map<String, String>): Builder {
            interceptor.paramsMap.putAll(paramsMap)
            return this
        }

        fun addHeaderParam(key: String, value: String): Builder {
            interceptor.headerParamsMap.put(key, value)
            return this
        }

        fun addHeaderParamsMap(headerParamsMap: Map<String, String>): Builder {
            interceptor.headerParamsMap.putAll(headerParamsMap)
            return this
        }

        fun addHeaderLine(headerLine: String): Builder {
            val index = headerLine.indexOf(":")
            if (index == -1) {
                throw IllegalArgumentException("Unexpected header: " + headerLine)
            }
            interceptor.headerLinesList.add(headerLine)
            return this
        }

        fun addHeaderLinesList(headerLinesList: List<String>): Builder {
            for (headerLine in headerLinesList) {
                val index = headerLine.indexOf(":")
                if (index == -1) {
                    throw IllegalArgumentException("Unexpected header: " + headerLine)
                }
                interceptor.headerLinesList.add(headerLine)
            }
            return this
        }

        fun addQueryParam(key: String, value: String): Builder {
            interceptor.queryParamsMap.put(key, value)
            return this
        }

        fun addQueryParamsMap(queryParamsMap: Map<String, String>): Builder {
            interceptor.queryParamsMap.putAll(queryParamsMap)
            return this
        }

        fun build(): BasicParamsInterceptor {
            return interceptor
        }

    }
}