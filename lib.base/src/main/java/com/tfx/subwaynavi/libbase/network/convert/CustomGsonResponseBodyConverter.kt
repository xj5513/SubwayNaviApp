package com.tfx.subwaynavi.libbase.network.convert

import okhttp3.ResponseBody
import com.google.gson.TypeAdapter
import com.google.gson.Gson
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset


/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
class CustomGsonResponseBodyConverter<T> internal constructor(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val response = value.string()
        val contentType = value.contentType()
        val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
        val inputStream = ByteArrayInputStream(response.toByteArray())
        val reader = InputStreamReader(inputStream, charset)
        val jsonReader = gson.newJsonReader(reader)
        try {
            return adapter.read(jsonReader)
        } finally {
            value.close()
        }
    }

    companion object {
        private val UTF_8 = Charset.forName("UTF-8")
    }
}