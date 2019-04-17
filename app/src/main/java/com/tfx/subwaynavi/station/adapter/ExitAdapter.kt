package com.tfx.subwaynavi.station.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.station.bean.ExitInfo
import org.jetbrains.anko.find

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/18
 *
 */
class ExitAdapter : RecyclerView.Adapter<ExitAdapter.ExitViewHolder>() {

    private var mData = arrayListOf<ExitInfo>()

    fun setData(data : ArrayList<ExitInfo>){
        if(data.isNotEmpty()){
            mData.clear()
            mData.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ExitViewHolder?, position: Int) {
        var model = mData[position]
        holder?.exitName?.text = model.chukou
        holder?.exitInfo?.text = model.chukoufangxiang
        if(!TextUtils.isEmpty(model.gongjiao)){
            holder?.exitBus?.text =  holder?.itemView?.context?.getString(R.string.bus_info,model.gongjiao)
            holder?.exitBus?.visibility = View.VISIBLE
        }else{
            holder?.exitBus?.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExitViewHolder {
        return ExitViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_eixt,parent,false))
    }

    class ExitViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val exitName : TextView = itemView.find(R.id.exitName)
        val exitInfo : TextView = itemView.find(R.id.exitInfo)
        val exitBus : TextView = itemView.find(R.id.exitBus)
    }
}