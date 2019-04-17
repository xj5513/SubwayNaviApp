package com.tfx.subwaynavi.libbase.widget

import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.view.LayoutInflater
import android.view.Window
import com.tfx.subwaynavi.libbase.R

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/4
 *
 */
class WholeLoadingDialog : Dialog{

    constructor(context: Context) : this(context, R.style.dialog_whole_loading)

    constructor(context: Context , themeResId : Int) : super(context,themeResId){
        initDialog()
    }

    private fun initDialog(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(false)
        setCancelable(true)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_whole_loading, null)
        setContentView(view)
    }



}