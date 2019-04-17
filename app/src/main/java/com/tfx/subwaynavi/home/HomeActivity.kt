package com.tfx.subwaynavi.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.home.adapter.HomeAdapter
import com.tfx.subwaynavi.home.adapter.HomePagerAdapter
import com.tfx.subwaynavi.home.model.HomeMenu
import com.tfx.subwaynavi.home.model.HomeMenuModel
import com.tfx.subwaynavi.libbase.base.BaseActivity
import com.tfx.subwaynavi.libbase.utils.UiUtil
import com.tfx.subwaynavi.map.MetroMapActivity
import com.tfx.subwaynavi.station.map.ActivityStationExitMap
import com.tfx.subwaynavi.station.ui.StationDetailActivity
import com.tfx.subwaynavi.station.ui.StationListActivity
import com.tfx.subwaynavi.station.ui.SurroundActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    private lateinit var mAdapter : HomeAdapter
    private var mMenuData = arrayListOf<HomeMenuModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        initView()
        initData()
    }

    private fun initData() {
        mMenuData.add(HomeMenuModel("线路查询",HomeMenu.LINE_SEARCH.permission))
        mMenuData.add(HomeMenuModel("站点信息",HomeMenu.STATION_INFO.permission))
        mMenuData.add(HomeMenuModel("站内向导",HomeMenu.STATION_GUIDE.permission))

        //mAdapter.setData(mMenuData)

    }

    private fun initView(){
        //mHomeList.layoutManager = GridLayoutManager(this, 4)
        //mAdapter = HomeAdapter()
        //mHomeList.adapter = mAdapter


        mHomeStationList.setOnClickListener {
            startActivity(Intent(this,StationListActivity::class.java))
        }

        mHomeStationTime.setOnClickListener {
            //startActivity(Intent(this,StationListActivity::class.java))
        }

        mHomeLine.setOnClickListener {
            startActivity(Intent(this,MetroMapActivity::class.java))
        }

        mHomeStationMap.setOnClickListener {
            //站内导航
            var exit = Intent(this, ActivityStationExitMap::class.java)
            //exit.putExtra("bdid",mStationId)
            exit.putExtra("bdid","B000A11DCV")
            startActivity(exit)
        }

        mHomeVoice.setOnClickListener {
            UiUtil.showToast("敬请期待")
        }

        mHomeUpdate.setOnClickListener {
            UiUtil.showToast("敬请期待")
        }


    }

    override fun getMainContentResId(): Int = R.layout.activity_home

}
