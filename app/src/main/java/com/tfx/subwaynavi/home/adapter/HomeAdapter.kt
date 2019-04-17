package com.tfx.subwaynavi.home.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.home.model.HomeMenu
import com.tfx.subwaynavi.home.model.HomeMenuModel
import com.tfx.subwaynavi.libbase.utils.UiUtil
import com.tfx.subwaynavi.map.MetroMapActivity
import com.tfx.subwaynavi.station.map.ActivityStationExitMap
import com.tfx.subwaynavi.station.ui.StationListActivity
import org.jetbrains.anko.find

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/29
 *
 */
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    private var mData = arrayListOf<HomeMenuModel>()

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(data : ArrayList<HomeMenuModel>){
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        val menu = mData[position]
        holder?.mFuncListName?.text = menu.name
        holder?.itemView?.tag = position
        holder?.mFuncListIcon?.tag = menu.permission
        holder?.itemView?.layoutParams?.height = UiUtil.getWindowWidth(holder?.itemView?.context!!) / 4
        holder?.itemView?.setOnClickListener {
            it->
            onItemClick(mData[it.tag as Int] , holder?.itemView?.context)
        }
        setIcon(holder,mData[position])
    }

    private fun setIcon(holder: HomeViewHolder?,data : HomeMenuModel){
        when(data.permission){
            HomeMenu.LINE_SEARCH.permission -> {
                //线路查询
                holder?.mFuncListIcon?.setImageResource(R.mipmap.menu_inventoryquery)
            }
            HomeMenu.STATION_INFO.permission ->{
                //站点信息
                holder?.mFuncListIcon?.setImageResource(R.mipmap.menu_report_loss)
            }
            HomeMenu.STATION_GUIDE.permission ->{
                //站点信息
                holder?.mFuncListIcon?.setImageResource(R.mipmap.menu_move)
            }
        }
    }

    private fun onItemClick(data: HomeMenuModel,context : Context){
        val nextIntent = when(data.permission){
            HomeMenu.LINE_SEARCH.permission -> {
                //线路查询
                Intent(context,MetroMapActivity::class.java)

            }
            HomeMenu.STATION_INFO.permission ->{
                //站点信息
                Intent(context, StationListActivity::class.java)
            }
            HomeMenu.STATION_GUIDE.permission ->{
                //站点信息
                var exit = Intent(context, ActivityStationExitMap::class.java)
                //exit.putExtra("bdid",mStationId)
                exit.putExtra("bdid","B000A11DCV")
            }
            else -> Intent(context, StationListActivity::class.java)
        }
        UiUtil.startActivity(context,nextIntent)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeViewHolder {
        return  HomeViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_home,parent,false))
    }

    class HomeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val mFuncListName : TextView = itemView.find(R.id.mFuncListName)
        val mFuncListIcon : ImageView = itemView.find(R.id.mFuncListIcon)
    }

}