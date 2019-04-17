package com.tfx.subwaynavi.libbase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.LayoutParams
import android.support.v7.widget.RecyclerView.State
import android.view.View


/**
 * Author xia jie 2017/8/27.
 * Mail: xj5513@163.com
 * recyclerView的分割线
 */
class DividerItemDecoration(context: Context, orientation: Int) : ItemDecoration() {

    private var mOrientation = LinearLayoutManager.VERTICAL
    private val mDivider: Drawable
    private val attrs = intArrayOf(android.R.attr.listDivider)

    init {
        val typedArray = context.obtainStyledAttributes(attrs)
        mDivider = typedArray.getDrawable(0)
        typedArray.recycle()
        setOrientation(orientation)
    }

    private fun setOrientation(orientation: Int) {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw IllegalArgumentException("Invalid Orientation Type")
        }
        this.mOrientation = orientation
    }

    /**
     * 此方法在recycler view的item绘制完之后绘制
     */
    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: State?) {
        super.onDrawOver(c, parent, state)
        //2。调用这个绘制方法， RecyclerView会毁掉该绘制方法,需要你自己去绘制条目的间隔线
        if (mOrientation == LinearLayoutManager.VERTICAL) {//垂直
            drawVertical(c!!, parent!!)
        } else {//水平
            drawHorizontal(c!!, parent!!)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        (0 until (childCount -1)).map{
            val child = parent.getChildAt(it)
            val params = child.layoutParams as LayoutParams
            val left = child.right + params.rightMargin + Math.round(ViewCompat.getTranslationX(child))
            val right = left + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        // 画水平线
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        (0 until (childCount -1)).map{
            val child = parent.getChildAt(it)
            val params = child.layoutParams as LayoutParams
            val top = child.bottom + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child))
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: State?) {
        //1.调用此方法（首先会先获取条目之间的间隙宽度---Rect矩形区域）
        // 获得条目的偏移量(所有的条目都回调用一次该方法)
        if (mOrientation == LinearLayoutManager.VERTICAL) {//垂直
            outRect.set(0, 0, 0, mDivider.intrinsicHeight)
        } else {//水平
            outRect.set(0, 0, mDivider.intrinsicWidth, 0)
        }
    }

}
