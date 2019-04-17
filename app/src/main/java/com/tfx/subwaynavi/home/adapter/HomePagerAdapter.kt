package com.tfx.subwaynavi.home.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup



/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 *
 */
class HomePagerAdapter :  PagerAdapter(){

    private var mData : ArrayList<View> = arrayListOf()

    fun setData(data : ArrayList<View>){
        mData = data
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(mData[position])
        return mData[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mData[position])
    }

}