package com.tfx.subwaynavi.libbase.base

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tfx.subwaynavi.libbase.R
import com.tfx.subwaynavi.libbase.mvp.BasePresenter
import com.tfx.subwaynavi.libbase.mvp.BasePresenterView
import com.tfx.subwaynavi.libbase.utils.StatusBarUtil
import com.tfx.subwaynavi.libbase.widget.WholeLoadingDialog

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/28
 *
 */
abstract class BaseActivity : AppCompatActivity(){

    private var mInflater: LayoutInflater? = null
    private var mToolbar: Toolbar? = null
    private var containerView: ViewGroup? = null
    private var mAppBarLayout: AppBarLayout? = null
    private var mHideNavigationIcon = true//true 隐藏 默认false不隐藏

    private var loadingDialog : WholeLoadingDialog ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.showLightStatusBarIcon(this)

        initBaseView()
    }

    private fun initBaseView(){
        setRootView()
        containerView = findViewById(R.id.main_view)
        mInflater = LayoutInflater.from(this)
        val mainView = mInflater?.inflate(getMainContentResId(), containerView, false)
        containerView?.addView(mainView)
        initToolbar()
    }

    private fun setRootView() {
        setContentView(R.layout.activity_base)
    }

    private fun initToolbar() {
        mToolbar = findViewById(R.id.toolbar)
        if (mToolbar != null) {
            if (getToolbarTitle() != -1) {
                mToolbar?.setTitle(getToolbarTitle())
            } else {
                mToolbar?.title = ""
            }
            mAppBarLayout = findViewById(R.id.common_view)
            if (mAppBarLayout != null) {
                mAppBarLayout?.setBackgroundResource(R.drawable.shape_base_gradient)
            }
            mToolbar?.setBackgroundResource(R.drawable.shape_base_gradient)
            setSupportActionBar(mToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(!mHideNavigationIcon)

//            val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
//            upArrow?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
//            supportActionBar?.setHomeAsUpIndicator(upArrow)

            mToolbar?.setNavigationOnClickListener(View.OnClickListener { onBackPressed() })
        }
    }

    fun hideToolbar(){
        mToolbar?.visibility = View.GONE
    }

    /**
     * 是否要隐藏返回键
     */
    fun hideNavigationIcon(hideNavigationIcon: Boolean): Boolean {
        this.mHideNavigationIcon = hideNavigationIcon
        supportActionBar?.setDisplayHomeAsUpEnabled(mHideNavigationIcon)
        return mHideNavigationIcon
    }

    /**
     * 设置Toolbar标题
     *
     * @return
     */
    @StringRes
    protected fun getToolbarTitle(): Int {
        return -1
    }

    @LayoutRes
    abstract fun getMainContentResId(): Int

    fun showLoadingDialog(){
        if(loadingDialog == null){
            loadingDialog = WholeLoadingDialog(this)
        }
        if(loadingDialog?.isShowing == false){
            loadingDialog?.show()
        }
    }

    fun closeLoadingDialog(){
        if (loadingDialog != null && loadingDialog?.isShowing ==true) {
            loadingDialog?.dismiss()
        }
    }
}
