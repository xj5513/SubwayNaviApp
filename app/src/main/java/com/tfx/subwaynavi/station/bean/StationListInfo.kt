package com.tfx.subwaynavi.station.bean

import com.tfx.subwaynavi.libbase.base.KeepAttr
import com.tfx.subwaynavi.station.adapter.StationListAdapter
import com.tfx.subwaynavi.station.model.StationBaseModel

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/2
 *
 */
open class StationListInfo : KeepAttr , StationBaseModel(){

    var poiid : String = ""
    var name : String = ""


    override fun getType(): Int {
        return StationListAdapter.TYPE_CONTENT
    }

    override fun getContent(): String {
        return name
    }

    override fun getClickContent(): String? {
        return poiid
    }
}