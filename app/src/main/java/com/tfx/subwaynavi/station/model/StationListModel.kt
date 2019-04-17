package com.tfx.subwaynavi.station.model

import com.tfx.subwaynavi.base.BaseModel
import com.tfx.subwaynavi.libbase.network.HttpFunction
import com.tfx.subwaynavi.station.bean.StationListInfo
import io.reactivex.Observer
import java.util.ArrayList

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/2
 *
 */
class StationListModel : BaseModel() {

    fun getInstance(): StationListModel? {
        return getPresent(StationListModel::class.java)
    }

    fun execute(observer: Observer<ArrayList<StationListInfo>>) {
        val observable = mServletApi?.getStationList()?.map(HttpFunction())
        toSubscribe(observable, observer)
    }

}