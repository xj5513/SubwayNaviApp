package com.tfx.subwaynavi.libbase.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View



/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 *
 */
class VerticalViewPager : ViewPager {

    constructor(context : Context) : super(context){
        init(context)
    }

    constructor(context: Context , attrs : AttributeSet) : super(context,attrs){
        init(context)
    }


    private fun init(context: Context){
        //mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = OVER_SCROLL_NEVER
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        var intercept = super.onInterceptTouchEvent(swapXY(event))
        swapXY(event)
        return intercept
    }

    class VerticalPageTransformer : ViewPager.PageTransformer {
        override fun transformPage(view : View, position: Float) {
            when {
                position < -1 -> // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.alpha = 0.0f
                position <= 1 -> { // [-1,1]
                    view.alpha = 1.0f

                    // Counteract the default slide transition
                    view.translationX = view.width * -position

                    //set Y position to swipe in from top
                    val yPosition = position * view.height
                    view.translationY = yPosition

                }
                else -> // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.alpha = 0.0f
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private fun swapXY(ev : MotionEvent?) : MotionEvent? {
        val width = width
        val height = height

        val newX = ((ev?.y ?: 0.0f) / height) * width
        val newY = ((ev?.x ?: 0.0f) / width) * height

        ev?.setLocation(newX, newY)
        return ev
    }

}