package com.tfx.subwaynavi.libbase.network

import com.tfx.subwaynavi.libbase.network.model.BaseResponse
import io.reactivex.functions.Function



/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/27
 *
 */
class HttpFunction<T> : Function<BaseResponse<T>, T> {

    @Throws(Exception::class)
    override fun apply(response: BaseResponse<T>): T {
        if (response.code != 0) {
            throw ApiException(response.code, response.message)
        }
        return response.data
    }
}