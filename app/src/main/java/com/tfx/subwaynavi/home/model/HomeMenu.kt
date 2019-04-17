package com.tfx.subwaynavi.home.model

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/29
 *
 */
enum class HomeMenu(var permission: String) {
    LINE_SEARCH("metro:menu:lineSearch"),                    //线路查询
    STATION_INFO("metro:menu:stationInfo"),                  //站点信息
    STATION_GUIDE("metro:menu:stationGuide"),
}