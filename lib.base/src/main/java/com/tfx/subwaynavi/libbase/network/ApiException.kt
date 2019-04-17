package com.tfx.subwaynavi.libbase.network

import com.tfx.subwaynavi.libbase.network.code.ConstantCode


/**
 * 异常处理的一个类
 */
class ApiException : RuntimeException {

    private var mErrorCode: Int? = 0

    constructor(errorCode: Int, errorMessage: String) : super(errorMessage){
        mErrorCode = errorCode
    }


    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    fun isTokenExpried(): Boolean {
        return mErrorCode == ConstantCode.EXCEPTION_TOKEN
    }

}