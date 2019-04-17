package com.tfx.subwaynavi.station.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.libbase.base.BaseActivity
import com.tfx.subwaynavi.libbase.network.observer.SafeObserver
import com.tfx.subwaynavi.station.adapter.ExitAdapter
import com.tfx.subwaynavi.station.adapter.PlatformAdapter
import com.tfx.subwaynavi.station.bean.ExitInfo
import com.tfx.subwaynavi.station.bean.PlatformInfo
import com.tfx.subwaynavi.station.bean.StationDetailInfo
import com.tfx.subwaynavi.station.bean.TransformInfo
import com.tfx.subwaynavi.station.map.ActivityStationExitMap
import com.tfx.subwaynavi.station.model.StationDetailModel
import kotlinx.android.synthetic.main.activity_station_detail.*

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/3
 *
 */
class StationDetailActivity : BaseActivity() {

    private var mStationId = ""
    private var mStationName = ""
    companion object {
        val STATION_ID = "STATION_ID"
        val STATION_NAME = "STATION_NAME"
    }

    private lateinit var mExitAdapter : ExitAdapter
    private lateinit var mPlatformAdapter : PlatformAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideToolbar()
        initView()
        initData()
    }

    private fun initView(){
        mStationDetailBack.setOnClickListener { finish() }
        mExitAdapter = ExitAdapter()
        mExitInfo.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mExitInfo.adapter = mExitAdapter

        mPlatformAdapter = PlatformAdapter()
        mPlatformInfo.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mPlatformInfo.adapter = mPlatformAdapter

        mStationExitMap.setOnClickListener { setExitMap() }
    }

    private fun initData(){
        if(intent?.hasExtra(STATION_ID) == true){
            mStationId = intent?.getStringExtra(STATION_ID) ?: ""
        }else{
            finish()
        }

        if(intent?.hasExtra(STATION_NAME) == true){
            mStationName = intent?.getStringExtra(STATION_NAME) ?: ""
            mStationDetailName.text = mStationName

            //TODO   记得删除
            mStationDetailName.text = "T2-航站楼"
        }else{
            finish()
        }
        showLoadingDialog()
        StationDetailModel().execute(object : SafeObserver<StationDetailInfo>(){
            override fun onBaseError(t: Throwable) {
                closeLoadingDialog()
            }

            override fun onBaseNext(data: StationDetailInfo) {
                closeLoadingDialog()
                if(data.output.isNotEmpty()){
                    setExitInfo(data.output,data.transform)
                }

                if(data.platformdetail.isNotEmpty()){
                    setPlatformInfo(data.platformdetail[0])
                }
            }
        },"BV10013428")
    }

    /**
     * 设置基础信息
     */
    private fun setPlatformInfo(info : PlatformInfo){
        mPlatformAdapter.setData(info)
    }

    /**
     * 设置出入口信息
     */
    private fun setExitInfo(info : ArrayList<ExitInfo> , transforn : ArrayList<TransformInfo>){
        info.map { it->
            transforn.map { form->
                if(it.chukou.equals(form.chukou)){
                    it.gongjiao = form.gongjiao
                }
            }
        }
        mExitAdapter.setData(info)
    }

    /**
     * 设置出入口地图
     */
    private fun setExitMap(){
        var exit = Intent(this,SurroundActivity::class.java)
        startActivity(exit)
    }

    override fun getMainContentResId(): Int = R.layout.activity_station_detail

}