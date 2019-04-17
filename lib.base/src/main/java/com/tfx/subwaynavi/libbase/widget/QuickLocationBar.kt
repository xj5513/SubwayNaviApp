package com.tfx.subwaynavi.libbase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.tfx.subwaynavi.libbase.R

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/5/2
 *
 */
class QuickLocationBar : View {


    //    private var characters : ArrayList<String> = arrayListOf( "A", "B", "C", "D", "E", "F", "G", "H", "I",
//            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//            "W", "X", "Y", "Z")
    private var characters = arrayListOf<String>()
    private var choose = -1
    private var paint = Paint()
    private var mOnTouchLetterChangedListener : OnTouchLetterChangedListener ?= null
    private var mTextDialog : TextView ?= null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    fun setOnTouchLitterChangedListener(onTouchLetterChangedListener : OnTouchLetterChangedListener ) {
        this.mOnTouchLetterChangedListener = onTouchLetterChangedListener
    }

    fun setTextDialog(dialog : TextView) {
        this.mTextDialog = dialog
    }

    fun setData(list : ArrayList<String>?){
        if(list?.isNotEmpty() == true){
            characters = list
            invalidate()
            this.visibility = View.VISIBLE
        }else{
            this.visibility = View.GONE
        }
    }

    override fun onDraw(canvas : Canvas) {
        super.onDraw(canvas)
        if(characters.size>0){
            var width  = width
            var height = height
            var singleHeight = height / (characters.size+1)

            characters.forEachIndexed { index, s ->
                paint.color = resources.getColor(R.color.primary)
                paint.typeface = Typeface.DEFAULT_BOLD
                paint.isAntiAlias = true
                paint.textSize = 50.toFloat()
                if (index == choose) {// choose变量表示当前显示的字符位置,若没有触摸则为
                    paint.color = resources.getColor(R.color.black)
                    paint.isFakeBoldText = true
                }
                // 计算字符的绘制的位置
                var xPos : Float = width / 2 - paint.measureText(characters[index]) / 2
                var yPos : Float = singleHeight * index + singleHeight.toFloat()
                // 在画布上绘制字符
                canvas.drawText(characters[index], xPos, yPos, paint)
            }
        }
    }


    override fun dispatchTouchEvent( event : MotionEvent) : Boolean{
        if(characters.size > 0){
            var action = event.action
            var y : Float = event.y
            var c = (y / height * characters.size).toInt()
            when(action){
                MotionEvent.ACTION_UP ->{
                    choose = -1
                    setBackgroundColor(0x0000)
                    invalidate()
                    if (mTextDialog != null) {
                        mTextDialog?.visibility = View.GONE
                    }
                }
                MotionEvent.ACTION_MOVE ->{
                    if (choose != c) {

                        if (c >= 0 && c < characters.size) {
                            if (mOnTouchLetterChangedListener != null) {
                                mOnTouchLetterChangedListener
                                        ?.touchLetterChanged(characters[c])
                            }
                            if (mTextDialog != null) {
                                mTextDialog?.text = characters[c]
                                mTextDialog?.visibility = View.VISIBLE
                            }
                            choose = c
                            invalidate()
                        }
                    }
                }
            }
        }
        return true
    }


    interface OnTouchLetterChangedListener {
        fun touchLetterChanged(s : String)
    }

}