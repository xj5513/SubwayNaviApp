package com.tfx.subwaynavi.station.adapter

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.station.model.StationBaseModel
import com.tfx.subwaynavi.station.ui.StationDetailActivity
import org.jetbrains.anko.find

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/2
 *
 */
class StationListAdapter : RecyclerView.Adapter<StationListAdapter.StationViewHolder>(){

    companion object {
        const val TYPE_TITLE = 0x10001
        const val TYPE_CONTENT = 0x10002
    }

    private var mData = arrayListOf<StationBaseModel>()

    fun setData(data: ArrayList<StationBaseModel>?){
        if(data?.isNotEmpty() == true){
            mData.clear()
            mData.addAll(data)
            notifyDataSetChanged()
        }else{
            mData.clear()
            notifyDataSetChanged()
        }
    }

    fun getData() : ArrayList<StationBaseModel>{
        return mData
    }


    override fun onBindViewHolder(holder: StationViewHolder?, position: Int) {

        holder?.mStationItem?.text = mData[position].getContent()

        holder?.itemView?.setOnClickListener {
            if(!TextUtils.isEmpty(mData[position].getClickContent())){
                //点击条目返回数据
                val intent = Intent(holder?.itemView?.context,StationDetailActivity::class.java)
                intent.putExtra(StationDetailActivity.STATION_ID,mData[position].getClickContent())
                intent.putExtra(StationDetailActivity.STATION_NAME,mData[position].getContent())
                holder?.itemView?.context?.startActivity(intent)
            }
        }
        when(getItemViewType(position)){
            TYPE_TITLE -> {
                holder?.itemView?.isClickable = false
                //holder?.mStationLayout?.setBackgroundColor(ContextCompat.getColor(holder?.itemView?.context!!,R.color.black_a26))
            }
            TYPE_CONTENT -> {
                holder?.itemView?.isClickable = true
                //holder?.mStationLayout?.setBackgroundColor(ContextCompat.getColor(holder?.itemView?.context!!,R.color.white))
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return mData[position].getType()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StationViewHolder {
        return StationViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_station, parent, false))
    }


    class StationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val mStationItem : TextView = itemView.find(R.id.mStationItem)
        val mStationItemRight : LinearLayout = itemView.find(R.id.mStationItemRight)
    }
}