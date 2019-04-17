package com.tfx.subwaynavi.station.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.station.bean.StationListInfo
import com.tfx.subwaynavi.station.model.StationBaseModel
import org.jetbrains.anko.find

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/18
 *
 */
class SearchAdapter : BaseAdapter(){

    private var mData = arrayListOf<StationBaseModel>()

    fun setData(data : ArrayList<StationBaseModel>){
        if(data?.isNotEmpty()){
            mData.clear()
            mData.addAll(data)
            notifyDataSetChanged()
        }
    }

    fun getData() : ArrayList<StationBaseModel> {
        return mData
    }

    override fun getView(position: Int,convertView: View?, parent: ViewGroup?): View {
        var holder : SearchHolder
        var view = convertView
        if(convertView == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_search_result,null)
            holder = SearchHolder()
            holder.name = view.find(R.id.mSearchItem)
            view.tag = holder
        }else{
            holder = view?.tag as SearchHolder
        }
        holder?.name?.text = mData[position].getContent()
        return view!!
    }

    override fun getItem(position: Int): Any {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mData.size
    }

    inner class SearchHolder {
        var name : TextView ?= null
    }
}