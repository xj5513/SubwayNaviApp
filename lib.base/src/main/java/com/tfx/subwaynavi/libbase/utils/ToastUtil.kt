package com.tfx.subwaynavi.libbase.utils

import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.tfx.subwaynavi.libbase.BaseApplication

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */

object ToastUtil {
    /**
     * 正在显示的提示。
     */
    private var showingToast: Toast? = null


    /**
     * 显示提示。
     *
     * @param text 提示的内容。如果为 null 或者空白则隐藏当前显示。
     * @since 1.0
     */
    fun showToast(text: CharSequence?) {
        /*
         * 检查是否需要显示。
         */
        if (TextUtils.isEmpty(text)) {
            if (null != showingToast) {
                showingToast?.cancel()
            }
            return
        }

        /*
         * 根据文本长短决定显示的时间长度。
         */
        val duration: Int = if (text?.length?:0 <= 15) {
            Toast.LENGTH_SHORT
        } else {
            Toast.LENGTH_LONG
        }

        /*
         * 如果存在实例则更改文本继续显示，否则创建新的对象。
         */
        if (null != showingToast) {
            showingToast?.setText(text)
            showingToast?.duration = duration
        } else {
            showingToast = Toast.makeText(BaseApplication.getInstance(), text, duration)
        }
        if (showingToast != null && UiUtil.getWindowWidth(BaseApplication.getInstance()) >= 720) {
            val group = showingToast?.view as ViewGroup
            val messageTextView = group.getChildAt(0) as TextView
            messageTextView.textSize = 18f
        }
        showingToast!!.setGravity(Gravity.CENTER, 0, 0)
        showingToast!!.show()
    }

    /**
     * 显示提示。
     *
     * @param text 提示的内容。如果为 0 则隐藏当前显示。
     * @since 1.0
     */
    fun showToast(text: Int) {
        if (0 == text) {
            showToast(null)
        } else {
            showToast(BaseApplication.getInstance().getText(text))
        }
    }
}
