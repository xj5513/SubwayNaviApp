package com.tfx.subwaynavi.libbase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*

/**
 * Author xia jie 2017/8/27.
 * Mail: xj5513@163.com
 * recyclerView的表格分割线
 */
class DividerGridViewItemDecoration(context: Context) : ItemDecoration(){

    private val mDivider: Drawable
    private val attrs = intArrayOf(android.R.attr.listDivider)

    init {
        val typedArray = context.obtainStyledAttributes(attrs)
        mDivider = typedArray.getDrawable(0)
        typedArray.recycle()
    }

    /**
     * 此方法在recycler view的item绘制完之后绘制
     */
    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: State?) {
        super.onDrawOver(c, parent, state)
        drawVertical(c!!, parent!!)
        drawHorizontal(c!!, parent!!)
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        // 绘制水平间隔线
        val childCount = parent.childCount
        (0 until (childCount -1)).map{
            val child = parent.getChildAt(it)
            val params = child.layoutParams as LayoutParams
            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        //绘制垂直间隔线(垂直的矩形)
        val childCount = parent.childCount
        (0 until (childCount)).map{
            val child = parent.getChildAt(it)
            val params = child.layoutParams as LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDivider.intrinsicWidth
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    @Deprecated("")
    override fun getItemOffsets(outRect: Rect, itemPosition: Int,
                                parent: RecyclerView?) {
        // 四个方向的偏移值
        var right = mDivider.intrinsicWidth
        var bottom = mDivider.intrinsicHeight
        if (isLastColumn(itemPosition, parent)) {//是否是最后一列
            //			outRect.set(0, 0, 0, bottom);
            right = 0
        }
        if (isLastRow(itemPosition, parent)) {//是最后一行
            //			outRect.set(0, 0, right, 0);
            bottom = 0
        }
        outRect.set(0, 0, right, bottom)
    }

    /**
     * 是否是最后一行
     * @param itemPosition
     * @param parent
     * @return
     */
    private fun isLastRow(itemPosition: Int, parent: RecyclerView?): Boolean {
        val spanCount = getSpanCount(parent)
        val layoutManager = parent?.layoutManager
        //有多少列
        if (layoutManager is GridLayoutManager) {
            val childCount = parent.adapter.itemCount
            val lastRowCount = childCount % spanCount
            //最后一行的数量小于spanCount
            if (lastRowCount == 0 || lastRowCount < spanCount) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否是最后一列
     * @param itemPosition
     * @param parent
     * @return
     */
    private fun isLastColumn(itemPosition: Int, parent: RecyclerView?): Boolean {
        val layoutManager = parent?.layoutManager
        //有多少列
        if (layoutManager is GridLayoutManager) {
            val spanCount = getSpanCount(parent)
            if ((itemPosition + 1) % spanCount == 0) {
                return true
            }
        }
        return false
    }

    private fun getSpanCount(parent: RecyclerView?): Int {
        val layoutManager = parent?.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            return spanCount
        }
        return 0
    }
}
