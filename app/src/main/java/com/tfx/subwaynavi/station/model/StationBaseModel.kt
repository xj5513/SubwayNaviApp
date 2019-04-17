package com.tfx.subwaynavi.station.model

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/2
 *
 */
abstract class StationBaseModel {
    abstract fun getType() : Int
    abstract fun getContent() : String
    abstract fun getClickContent() : String?
}