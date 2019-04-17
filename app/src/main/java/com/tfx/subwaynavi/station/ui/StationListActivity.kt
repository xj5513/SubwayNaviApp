package com.tfx.subwaynavi.station.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ListPopupWindow
import android.widget.TextView
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.libbase.base.BaseActivity
import com.tfx.subwaynavi.libbase.network.observer.SafeObserver
import com.tfx.subwaynavi.libbase.utils.UiUtil
import com.tfx.subwaynavi.libbase.widget.QuickLocationBar
import com.tfx.subwaynavi.station.adapter.ExpandableRecyclerAdapter
import com.tfx.subwaynavi.station.adapter.SearchAdapter
import com.tfx.subwaynavi.station.adapter.StationListAdapter
import com.tfx.subwaynavi.station.bean.StationDataListTree
import com.tfx.subwaynavi.station.model.StationBaseModel
import com.tfx.subwaynavi.station.bean.StationListInfo
import com.tfx.subwaynavi.station.model.StationListModel
import kotlinx.android.synthetic.main.activity_station_list.*
import java.util.*

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/1
 *
 */
class StationListActivity : BaseActivity() , QuickLocationBar.OnTouchLetterChangedListener {

    private var mAdapter : StationListAdapter ?= null
    private var mFirstList : ArrayList<String> ?= null
    private var mStationMap = ArrayMap<String,ArrayList<StationBaseModel>>()
    private var mStationList = arrayListOf<StationBaseModel>()   //封装好的展示列表数据(包括首字母)
    private var move = false
    private var index = 0
    private lateinit var mSearchAdapter : SearchAdapter
    private lateinit var mListPopupWindow : ListPopupWindow
    private lateinit var mExpandableAdapter : ExpandableRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        initView()
        initListener()
        initData()
    }

    private fun initView() {
        mAdapter = StationListAdapter()
        //mExpandableAdapter = ExpandableRecyclerAdapter()
        mStationListContent.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mStationListContent.adapter = mAdapter
        //mStationListContent.adapter = mExpandableAdapter

        mListPopupWindow = ListPopupWindow(this)
        mSearchAdapter = SearchAdapter()
        mListPopupWindow.setAdapter(mSearchAdapter)
        mListPopupWindow.setOnItemClickListener { parent, view, position, id ->
            mListPopupWindow.dismiss()
            val intent = Intent(this,StationDetailActivity::class.java)
            intent.putExtra(StationDetailActivity.STATION_ID,mSearchAdapter.getData()[position].getClickContent())
            startActivity(intent)
        }
        mListPopupWindow.anchorView = mLLSearchView
        mListPopupWindow.height = UiUtil.getWindowHeight(this)/2
    }

    private fun initData() {
        showLoadingDialog()
        StationListModel().execute(object : SafeObserver<ArrayList<StationListInfo>>(){
            override fun onBaseError(t: Throwable) {
                Log.e("caterpillar",t.message)
                closeLoadingDialog()
            }

            override fun onBaseNext(data: ArrayList<StationListInfo>) {
                dealData(data)
                closeLoadingDialog()
            }
        })
    }

    private fun initListener() {
        mBtnBack.setOnClickListener { finish() }
        mBtnSearch.setOnClickListener { searchStation() }
        mInputSearch.setOnEditorActionListener(editorActionListener)
        mStationLocationBar.setOnTouchLitterChangedListener(this)
        mStationLocationBar.setTextDialog(mStationLocationType)

        //监听recycleview的滑动
        mStationListContent.addOnScrollListener(mOnScrollListener)
    }

    private var mOnScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            //在这里进行第二次滚动
            if (move ){
                move = false
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                var n = index - (mStationListContent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if ( 0 <= n && n < mStationListContent.childCount){
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    var top = mStationListContent.getChildAt(n).top
                    //最后的移动
                    mStationListContent.scrollBy(0, top)
                }
            }
        }
    }

    private fun dealData(list: ArrayList<StationListInfo>){
        if(list.isNotEmpty()){
            //首字母
            var first = arrayListOf<String>()
            list.map {
                if(!first.contains(UiUtil.getPinYinFirstUpperCase(it.name))){
                    first.add(UiUtil.getPinYinFirstUpperCase(it.name))
                }
            }
            //排序
            Collections.sort(first)

            mFirstList = first

            first.map {
                val mapList = arrayListOf<StationBaseModel>()
                list.map { departData ->
                    if(UiUtil.getPinYinFirstUpperCase(departData.name) == it){
                        mapList.add(departData)
                    }
                }
                mStationMap.put(it,mapList)
            }

            var dataTree = arrayListOf<StationDataListTree<String,StationBaseModel>>()
            first.map {
                mStationList.add(object : StationBaseModel(){
                    override fun getType(): Int {
                        return StationListAdapter.TYPE_TITLE
                    }
                    override fun getContent(): String = it
                    override fun getClickContent(): String? = ""
                })

                var stationList = arrayListOf<StationBaseModel>()
                for((key,value) in mStationMap){
                    if (key == it){
                        value.map {
                            mStationList.add(it)
                            stationList.add(it)
                        }
                    }
                }
                dataTree.add(StationDataListTree(it,stationList))
            }

            mAdapter?.setData(mStationList)
            mStationLocationBar.setData(first)
        }
    }

    private var editorActionListener: TextView.OnEditorActionListener = TextView.OnEditorActionListener { textView, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchStation()
            return@OnEditorActionListener true
        }
        false
    }

    override fun touchLetterChanged(str: String) {
        mAdapter?.getData()?.forEachIndexed { index, stationBean ->
            if(stationBean.getContent() == str){
                scrollToPosition(index)
            }
        }
    }

    /**
     * 滑动到指定的item
     */
    private fun scrollToPosition(n : Int) {
        index = n //记录一下 在第三种情况下会用到
        //拿到当前屏幕可见的第一个position跟最后一个postion
        val manager = mStationListContent.layoutManager as LinearLayoutManager
        var firstItem = manager.findFirstVisibleItemPosition()
        var lastItem = manager.findLastVisibleItemPosition()
        //区分情况
        if (n <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            mStationListContent.scrollToPosition(n)
        }else if ( n <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            var top = mStationListContent.getChildAt(n - firstItem).top
            mStationListContent.scrollBy(0,top)
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            mStationListContent.scrollToPosition(n)
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true
        }
    }


    private fun searchStation(){
        if(TextUtils.isEmpty(mInputSearch.text)){
            UiUtil.showToast("请输入站点")
        }else{
            val search = mInputSearch.text.toString()
            val length = search.length
            val data = arrayListOf<StationBaseModel>()
            if(length == 1 && isUpperOrLowerCase(search)) run {
                //大写字母
                mFirstList?.map {
                    if(search.toUpperCase() == it){
                        for((key,value) in mStationMap){
                            if (key == it){
                                value.map {
                                    data.add(it)
                                }
                            }
                        }
                    }
                }
            }else{
                mStationList.map {
                    if(it.getContent().contains(search)){
                        data.add(it)
                    }
                }
            }

            if(data.size > 0){
                mSearchAdapter.setData(data)
                if(!mListPopupWindow.isShowing){
                    mListPopupWindow.show()
                }
            }else{
                UiUtil.showToast("请输入正确的站点名称")
            }
        }
    }

    /**
     * 判断是否是中文字符
     */
    private fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
    }

    /**
     * 是否首字母是英文字母
     */
    private fun isUpperOrLowerCase(str : String) : Boolean{
        val char = str.toCharArray()[0]
        //大写字母
        return char.isUpperCase() || char.isLowerCase()
    }

    override fun getMainContentResId(): Int = R.layout.activity_station_list

}