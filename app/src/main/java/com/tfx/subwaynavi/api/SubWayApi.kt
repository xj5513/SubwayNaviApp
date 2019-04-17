package com.tfx.subwaynavi.api

import com.tfx.subwaynavi.libbase.network.model.BaseResponse
import com.tfx.subwaynavi.station.bean.StationDetailInfo
import com.tfx.subwaynavi.station.bean.StationListInfo
import com.tfx.subwaynavi.station.bean.TransformInfo
import io.reactivex.Observable
import retrofit2.http.*

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
interface SubWayApi {

    /**
     * 获取所有线zai
     */
    @GET("get_xianlu_info.php")
    fun getMetroList() : Observable<BaseResponse<ArrayList<StationListInfo>>>

    /**
     * 获取站台列表
     */
    @GET("get_platform_info.php")
    fun getStationList() : Observable<BaseResponse<ArrayList<StationListInfo>>>

    /**
     * 获取站台基础信息
     */
    @GET("get_xianlu_platform_output_info.php")
    fun getStationInfo(@Query("poiid") string : String) : Observable<BaseResponse<StationDetailInfo>>

    /**
     * 获取站台换成信息
     */
    @GET("get_transform_info.php")
    fun getTransformInfo(@Query("poiid") string : String) : Observable<BaseResponse<ArrayList<TransformInfo>>>



}