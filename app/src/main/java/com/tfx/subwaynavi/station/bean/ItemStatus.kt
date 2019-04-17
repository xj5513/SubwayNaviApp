package com.tfx.subwaynavi.station.bean

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/8
 *
 */
class ItemStatus {

    companion object {
        const val VIEW_TYPE_GROUP_ITEM = 0
        const val VIEW_TYPE_SUB_ITEM = 1

    }

    var mViewType : Int = VIEW_TYPE_SUB_ITEM
    var mGroupItemIndex : Int = -1
    var mSubItemIndex : Int = -1

}
