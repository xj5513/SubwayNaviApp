package com.tfx.subwaynavi.station.model

import com.tfx.subwaynavi.base.BaseModel
import com.tfx.subwaynavi.libbase.network.HttpFunction
import com.tfx.subwaynavi.station.bean.StationDetailInfo
import io.reactivex.Observer

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/3
 *
 */
class StationDetailModel : BaseModel() {

    fun getInstance(): StationDetailModel? {
        return getPresent(StationDetailModel::class.java)
    }


    //BV10013454
    fun execute(observer: Observer<StationDetailInfo>, id : String) {
        val observable = mServletApi?.getStationInfo( id)?.map(HttpFunction())
        toSubscribe(observable, observer)
    }
}