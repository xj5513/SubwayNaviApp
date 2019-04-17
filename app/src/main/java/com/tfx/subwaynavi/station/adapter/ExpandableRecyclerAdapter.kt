package com.tfx.subwaynavi.station.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.station.bean.ItemStatus
import com.tfx.subwaynavi.station.bean.StationDataListTree
import com.tfx.subwaynavi.station.model.StationBaseModel
import com.tfx.subwaynavi.station.ui.StationDetailActivity
import org.jetbrains.anko.find



/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/8
 *
 */
class ExpandableRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData : ArrayList<StationDataListTree<String, StationBaseModel>> ?= null   //数据源

    private var mGroupItemStatus : ArrayList<Boolean> = arrayListOf()//用来保存一级标题的开关状态

    fun setData(data : ArrayList<StationDataListTree<String,StationBaseModel>>){
        mData = data
        initGroupItemStatus()
        notifyDataSetChanged()
    }

    private fun initGroupItemStatus(){
        mGroupItemStatus = arrayListOf()
        mData?.map {
            mGroupItemStatus.add(false)
        }
    }

    /**
     * 当前position可能是 group item 也可能是sub item
     */
    private fun getItemStatusByPosition(position : Int) : ItemStatus{
        var itemStatus = ItemStatus()
        //轮询 groupItem 的开关状态
        var itemCount = 0
        var a = 0
        for (i in 0 until mGroupItemStatus.size) {
            if (itemCount == position) { //position刚好等          于计数时，item为groupItem
                itemStatus.mViewType = ItemStatus.VIEW_TYPE_GROUP_ITEM
                itemStatus.mGroupItemIndex = i
                break
            } else if (itemCount > position) { //position大于计数时，item为groupItem(index - 1)中的某个subItem
                itemStatus.mViewType = ItemStatus.VIEW_TYPE_SUB_ITEM
                itemStatus.mGroupItemIndex = i - 1 // 指定的position组索引
                // 计算指定的position前，统计的列表项和
                var temp = itemCount - (mData?.get(i - 1)?.mSubItem?.size?:0)
                // 指定的position的子项索引：即为position-之前统计的列表项和
                itemStatus.mSubItemIndex = position - temp
                break
            }

            itemCount++
            if (mGroupItemStatus[i]) {
                itemCount += mData?.get(i)?.mSubItem?.size?:0
            }
        }

//        mGroupItemStatus.mapIndexed { index, b ->
//            if (itemCount == position) { //position刚好等          于计数时，item为groupItem
//                itemStatus.mViewType = ItemStatus.VIEW_TYPE_GROUP_ITEM
//                itemStatus.mGroupItemIndex = index
//                return@mapIndexed
//            } else if (itemCount > position) { //position大于计数时，item为groupItem(index - 1)中的某个subItem
//                itemStatus.mViewType = ItemStatus.VIEW_TYPE_SUB_ITEM
//                itemStatus.mGroupItemIndex = index - 1 // 指定的position组索引
//                // 计算指定的position前，统计的列表项和
//                var temp = itemCount - (mData?.get(index - 1)?.mSubItem?.size?:0)
//                // 指定的position的子项索引：即为position-之前统计的列表项和
//                itemStatus.mSubItemIndex = position - temp
//                return@mapIndexed
//            }
//
//
//            a = index
//            itemCount++
//            if (mGroupItemStatus[index]) {
//                itemCount += mData?.get(index)?.mSubItem?.size?:0
//            }
//        }


        // 轮询到最后一组时，未找到对应位置
        if (a ==  mGroupItemStatus.size -1) {
            itemStatus.mViewType = ItemStatus.VIEW_TYPE_SUB_ITEM  // 设置为二级标签类型
            itemStatus.mGroupItemIndex = a - 1 // 设置一级标签为最后一组
            itemStatus.mSubItemIndex = (position - (itemCount - (mData?.get(a - 1)?.mSubItem?.size?:0)))
        }
        return itemStatus
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        if(mGroupItemStatus.size == 0){
            return itemCount
        }

        mData?.map {
            it->
            itemCount++
            itemCount += it.mSubItem.size
        }
        Log.i("size--------" , "s" + itemCount)
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return getItemStatusByPosition(position).mViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder{
        var view : View
        var viewHolder : RecyclerView.ViewHolder
        when (viewType) {
            ItemStatus.VIEW_TYPE_GROUP_ITEM -> {
                // parent需要传入对应的位置，否则列表项触发不了点击事件
                view = LayoutInflater.from(parent?.context).inflate(R.layout.item_group, parent, false);
                viewHolder = GroupItemViewHolder(view)
            }
            ItemStatus.VIEW_TYPE_SUB_ITEM -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.item_station, parent, false);
                viewHolder = SubItemViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.item_station, parent, false);
                viewHolder = SubItemViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var itemStatus = getItemStatusByPosition(position) // 获取列表项状态
        var data = mData?.get(itemStatus.mGroupItemIndex)
        if (itemStatus.mViewType == ItemStatus.VIEW_TYPE_GROUP_ITEM) { // 组类型
            var groupHolder = holder as GroupItemViewHolder
            groupHolder.mStationGroupItem.text = data?.mGroupItem?:""


            val groupIndex = itemStatus.mGroupItemIndex // 组索引

            groupHolder.itemView.setOnClickListener {
                if(mGroupItemStatus[groupIndex]){
                    // 一级标题打开状态
                    mGroupItemStatus[groupIndex] =  false
                    notifyItemRangeRemoved(groupHolder.adapterPosition + 1, data?.mSubItem?.size?:0)
                }else{
                    // 一级标题关闭状态
                    initGroupItemStatus() // 1. 实现只展开一个组的功能，缺点是没有动画效果
                    mGroupItemStatus[groupIndex] = true
                    notifyDataSetChanged()  // 1. 实现只展开一个组的功能，缺点是没有动画效果
                    //notifyItemRangeInserted(groupHolder.adapterPosition + 1, data?.mSubItem?.size?:0) //2. 实现展开可多个组的功能，带动画效果
                }
            }
        }else if (itemStatus.mViewType == ItemStatus.VIEW_TYPE_SUB_ITEM) {
            // 子项类型
            var subItemViewHolder = holder as SubItemViewHolder
            subItemViewHolder.mStationItem.text = data?.mSubItem?.get(itemStatus.mSubItemIndex)?.getContent()

            subItemViewHolder.itemView.setOnClickListener {
                //点击条目返回数据
                val intent = Intent(holder?.itemView?.context, StationDetailActivity::class.java)
                intent.putExtra(StationDetailActivity.STATION_ID,data?.mSubItem?.get(itemStatus.mSubItemIndex)?.getClickContent())
                intent.putExtra(StationDetailActivity.STATION_NAME,data?.mSubItem?.get(itemStatus.mSubItemIndex)?.getContent())
                holder?.itemView?.context?.startActivity(intent)
            }
        }
    }


    //
    class GroupItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val mStationGroupItem : TextView = itemView.find(R.id.mStationGroupItem)
    }

    class SubItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mStationItem : TextView = itemView.find(R.id.mStationItem)
        val mStationItemRight : LinearLayout = itemView.find(R.id.mStationItemRight)
    }
}