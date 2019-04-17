package com.tfx.subwaynavi.station.adapter

import android.support.v4.util.ArrayMap
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.station.bean.PlatformInfo
import org.jetbrains.anko.find

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/18
 *
 */
class PlatformAdapter : RecyclerView.Adapter<PlatformAdapter.PlatformHolder>() {

    private var mData : PlatformInfo ?= null
    private var mMap  = ArrayMap<String,String>()
    private var mList = arrayListOf<String>()

    fun setData(data : PlatformInfo){
        mData = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PlatformHolder?, position: Int) {
        val model = mMap[mList[position]]

        when(mList[position]){
            "weishengjian" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_wc)
                holder?.mPlatformTitle?.text = "卫生间"
                holder?.mPlatformContent?.text = model
            }

            "jingwushi" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_police)
                holder?.mPlatformTitle?.text = "警务室"
                holder?.mPlatformContent?.text = model
            }

            "shoupiao" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_tickets)
                holder?.mPlatformTitle?.text = "售票处"
                holder?.mPlatformContent?.text = model
            }

            "zidongshoupiao" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_tickets)
                holder?.mPlatformTitle?.text = "自动售票口"
                holder?.mPlatformContent?.text = model
            }

            "hujiaoshebei" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_call)
                holder?.mPlatformTitle?.text = "呼叫设备"
                holder?.mPlatformContent?.text = model
            }

            "podao" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_ramp)
                holder?.mPlatformTitle?.text = "坡道"
                holder?.mPlatformContent?.text = model
            }

            "shengjiangpingtai" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_lift_float)
                holder?.mPlatformTitle?.text = "升降平台"
                holder?.mPlatformContent?.text = model
            }

            "mangdao" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_blind_road)
                holder?.mPlatformTitle?.text = "盲道"
                holder?.mPlatformContent?.text = model
            }

            "wzaweishengjian" ->{
                holder?.mPlatformIcon?.setImageResource(R.mipmap.icon_wc_no)
                holder?.mPlatformTitle?.text = "无障碍卫生间"
                holder?.mPlatformContent?.text = model
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlatformHolder {
        return PlatformHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_infrastructure,parent,false))
    }

    override fun getItemCount(): Int {
        if(mData != null){
            mList.clear()
            if(!TextUtils.isEmpty(mData?.weishengjian)){
                mList.add("weishengjian")
                mMap["weishengjian"] = mData?.weishengjian
            }

            if(!TextUtils.isEmpty(mData?.jingwushi)){
                mList.add("jingwushi")
                mMap["jingwushi"] = mData?.jingwushi
            }

            if(!TextUtils.isEmpty(mData?.shoupiao)){
                mList.add("shoupiao")
                mMap["shoupiao"] = mData?.shoupiao
            }

            if(!TextUtils.isEmpty(mData?.zidongshoupiao)){
                mList.add("zidongshoupiao")
                mMap["zidongshoupiao"] = mData?.zidongshoupiao
            }

            if(!TextUtils.isEmpty(mData?.hujiaoshebei)){
                mList.add("hujiaoshebei")
                mMap["hujiaoshebei"] = mData?.hujiaoshebei
            }

            //什么鬼  暂时忽略
//            if(!TextUtils.isEmpty(mData?.palouche)){
//                count++
//            }

            if(!TextUtils.isEmpty(mData?.podao)){
                mList.add("podao")
                mMap["podao"] = mData?.podao
            }

            if(!TextUtils.isEmpty(mData?.shengjiangpingtai)){
                mList.add("shengjiangpingtai")
                mMap["shengjiangpingtai"] = mData?.shengjiangpingtai
            }

            if(!TextUtils.isEmpty(mData?.mangdao)){
                mList.add("mangdao")
                mMap["mangdao"] = if((mData?.mangdao?:0) == 1) "有" else "无"
            }

            if(!TextUtils.isEmpty(mData?.wzaweishengjian)){
                mList.add("wzaweishengjian")
                mMap["wzaweishengjian"] = mData?.wzaweishengjian
            }

            return mList.size
        }
        return 0
    }


    class PlatformHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val mPlatformIcon : ImageView = itemView.find(R.id.mPlatformIcon)
        val mPlatformTitle : TextView = itemView.find(R.id.mPlatformTitle)
        val mPlatformContent : TextView = itemView.find(R.id.mPlatformContent)
    }
}