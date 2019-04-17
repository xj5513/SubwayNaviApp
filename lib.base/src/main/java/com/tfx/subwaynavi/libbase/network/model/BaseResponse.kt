package com.tfx.subwaynavi.libbase.network.model

import com.tfx.subwaynavi.libbase.base.KeepAttr

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
data class BaseResponse<T>(var code : Int,
                        var data : T,
                        var now : Long,
                        var message : String) : KeepAttr {

}