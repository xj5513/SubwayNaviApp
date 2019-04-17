package com.tfx.subwaynavi.libbase.utils

import android.app.Activity
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.github.promeg.pinyinhelper.Pinyin
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 * UI 工具类。
 */

object UiUtil {


    /**
     * 显示提示。
     *
     * @param text 提示的内容。如果为 null 或者空白则隐藏当前显示。
     * @since 1.0
     */
    fun showToast(text: CharSequence) {
        ToastUtil.showToast(text)
    }

    /**
     * 显示提示。
     *
     * @param text 提示的内容。如果为 0 则隐藏当前显示。
     * @since 1.0
     */
    fun showToast(text: Int) {
        ToastUtil.showToast(text)
    }


    /**
     * 解决初始化获取焦点失败的问题
     *
     * @param view
     */
    fun requestViewFocus(view: View?) {
        view?.postDelayed({ view.requestFocus() }, 200)
    }

    /**
     * 获取Activity的第一个view
     *
     * @param activity
     * @return
     */
    fun getFirstChildView(activity: Activity): View {
        return ((activity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(0)
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun isToday(msec: Long): Boolean {
        val today = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = msec

        return (today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && today.get(Calendar.DATE) == calendar.get(Calendar.DATE))
    }

    fun isTomorrow(msec: Long): Boolean {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DATE, 1)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = msec

        return (tomorrow.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && tomorrow.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && tomorrow.get(Calendar.DATE) == calendar.get(Calendar.DATE))
    }

    fun getDayOfWeek(msec: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = msec
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun msecToFromatDate(context: Context, msec: Long, split: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = msec
        val pattern = "yyyy" + split + "MM" + split + "dd"
        val format = SimpleDateFormat(pattern)
        return format.format(calendar.time)
    }

    fun msecToFromatDate(msec: Long, pattern: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = msec
        val format = SimpleDateFormat(pattern)
        return format.format(calendar.time)
    }

    fun formatFileSize(bytes: Long): String {
        try {
            val df = DecimalFormat("#0.00")
            var fileSizeString = "--"
            if (bytes < 0) {
                //
            } else if (bytes < 1024) {
                fileSizeString = df.format(bytes.toDouble()) + " B"
            } else if (bytes < 1024 * 1024) {
                fileSizeString = df.format(bytes.toDouble() / 1024) + " KB"
            } else if (bytes < 1024 * 1024 * 1024) {
                fileSizeString = df.format(bytes.toDouble() / (1024 * 1024)) + " MB"
            } else {
                fileSizeString = df.format(bytes.toDouble() / (1024 * 1024 * 1024)) + " GB"
            }
            return fileSizeString
        } catch (e: NumberFormatException) {
            return "--"
        }

    }

    /**
     * @param ms 毫秒
     * @return
     */
    fun Ms2String(ms: Long): String {
        if (ms < 0) {
            return "00:00:00.0"
        } else {
            val ss = 1000
            val mi = ss * 60
            val hh = mi * 60
            val dd = hh * 24

            val day = ms / dd
            var hour = (ms - day * dd) / hh
            val minute = (ms - day * dd - hour * hh) / mi
            val second = (ms - day * dd - hour * hh - minute * mi) / ss
            val milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss

            //    		   String strDay = day < 10 ? "0" + day : "" + day*24;
            hour = hour + day * 24
            val strHour = if (hour < 10) "0" + hour else "" + hour
            val strMinute = if (minute < 10) "0" + minute else "" + minute
            val strSecond = if (second < 10) "0" + second else "" + second
            val strMilliSecond = "" + milliSecond / 100
            //    		   String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
            //    		   strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
            return "$strHour:$strMinute:$strSecond.$strMilliSecond"
        }
    }


    fun startActivity(context: Context?, intent: Intent?): Boolean {
        if (context == null || intent == null) {
            return false
        }
        context.startActivity(intent)
        return true
    }

//    /**
//     * @param context
//     */
//    fun showPermissionWarningDialog(context: Context?) {
//        if (context == null) return
//        val permissNameID = "相关"
//        UiUtil.buildDialog(context).setMessage(context.getString(R.string.permission_warnning, permissNameID, permissNameID))
//                .setConfirm(context.getString(R.string.confirm)).setCancel(context.getString(R.string.cancel)).setOnComfirmClick(View.OnClickListener {
//            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//            intent.data = Uri.fromParts("package", context.packageName, null)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(intent)
//            dismissDialog()
//        }).show()
//    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    fun getWindowHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    fun getWindowWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 小数点后面有无效的0都不显示
     *
     * @param cents
     * @return
     */
    fun centToYuanDeleteZeroString(cents: Int): String {
        var result = (cents / 100f).toString()
        if (result.indexOf(".") > 0) {
            result = result.replace("0+?$".toRegex(), "")//去掉后面无用的零
            result = result.replace("[.]$".toRegex(), "")//如小数点后面全是零则去掉小数点
        }
        return result
    }

    /**
     * 小数点后面有无效的0都不显示
     *
     * @param result
     * @return
     */
    fun centToYuanDeleteZeroString(result: String): String {
        var result = result
        if (result.indexOf(".") > 0) {
            result = result.replace("0+?$".toRegex(), "")//去掉后面无用的零
            result = result.replace("[.]$".toRegex(), "")//如小数点后面全是零则去掉小数点
        }
        return result
    }

    /**
     * 保留小数点后两位
     */
    fun keepTwoDecimalPlaces(num: String): String {
        if (num.indexOf(".") > 0) {
            val bd = BigDecimal(num)
            return bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
        } else {
            return num
        }
    }

    /**
     * 处理 数据   为空 就返回  -
     * @param num
     * @return
     */
    fun detailNumNullForLine(num: String): String {
        return if (TextUtils.isEmpty(num)) {
            "-"
        } else {
            keepTwoDecimalPlaces(centToYuanDeleteZeroString(num))
        }
    }

    fun detailNum(num: String): String {
        return if (TextUtils.isEmpty(num)) {
            "0"
        } else {
            keepTwoDecimalPlaces(centToYuanDeleteZeroString(num))
        }
    }

    /**
     * 去除字符串中无效的0  （共16.000箱->共16箱）
     *
     * @param result
     * @return
     */
    fun centToDeleteZeroString(result: String): String {
        var result = result
        if (result.indexOf(".") > 0) {
            val compile = Pattern.compile("\\d+\\.\\d+")
            val matcher = compile.matcher(result)
            matcher.find()
            val number = matcher.group()
            val temp = centToYuanDeleteZeroString(number)
            result = result.replace(number, temp)
        }
        return result
    }


    fun repeatY(bitmap: Bitmap, alpha: Int): BitmapDrawable {
        val drawable = BitmapDrawable(bitmap)
        drawable.tileModeY = Shader.TileMode.REPEAT
        drawable.setDither(true)
        drawable.alpha = alpha
        return drawable
    }

    fun repeatX(bitmap: Bitmap, alpha: Int): BitmapDrawable {
        val drawable = BitmapDrawable(bitmap)
        drawable.tileModeX = Shader.TileMode.REPEAT
        drawable.setDither(true)
        drawable.alpha = alpha
        return drawable
    }

    fun getViewWidth(view: View): Int {
        val measure = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(measure, measure)
        return view.measuredWidth
    }

    fun getViewHeight(view: View): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
        val measure = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, measure)
        return view.measuredHeight
    }

    /**
     * 关闭键盘
     *
     * @param context
     * @param view
     */
    fun closeKeyBroad(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 唤起键盘
     *
     * @param context
     * @param editText
     */
    fun requestKeyBroad(context: Context, editText: EditText) {
        val imm = context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 去掉float最后多余的0
     *
     * @param s
     * @return
     */
    fun subZeroAndDot(s: String): String {
        var s = s
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "")//去掉多余的0
            s = s.replace("[.]$".toRegex(), "")//如最后一位是.则去掉
        }
        return s
    }


    /**
     * 整数格式化 不包含单位 + 截取两位小数（不四舍五入）
     *
     * @param value
     * @return
     */
    fun valueFormatNoUnit(value: Double): String {
        val dev = BigDecimal(100.0)//总价转BigDecimal数据
        val bd = BigDecimal(value).divide(dev, 2, RoundingMode.DOWN).setScale(2, BigDecimal.ROUND_DOWN)
        val df = DecimalFormat("##,###,##0.00")
        return df.format(bd)
    }


    /**
     * 整数格式化 不包含单位 + 取整（不四舍五入）
     *
     * @param value
     * @return
     */
    fun valueFormatNum(value: Double): String {
        val bd = BigDecimal(value)
        val decimal = BigDecimal(100)
        val df = DecimalFormat("##,###,##0")
        return df.format(bd.divideToIntegralValue(decimal))
    }

    /**
     * 文本设置前景色
     *
     * @param color
     * @param start  需要设置格式的子字符串的起始下标
     * @param source
     * @return
     */
    fun setForegroundColorSpan(color: Int, start: Int, source: CharSequence): SpannableString {
        val spannableString = SpannableString(source)
        if (spannableString != null && spannableString.isNotEmpty()
                && start > 0 && spannableString.length > start) {
            val colorSpan = ForegroundColorSpan(color)
            spannableString.setSpan(colorSpan, start, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    /**
     * 文本设置文字相对大小
     *
     * @param proportion
     * @param start      需要设置格式的子字符串的起始下标
     * @param source
     * @return
     */
    fun setRelativeSizeSpan(proportion: Float, start: Int, source: CharSequence): SpannableString {
        val spannableString = SpannableString(source)
        if (spannableString != null && spannableString.isNotEmpty()
                && start > 0 && spannableString.length > start) {
            val sizeSpan = RelativeSizeSpan(proportion)
            spannableString.setSpan(sizeSpan, start, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    /**
     * 删除末尾逗号
     */
    fun deleteLastComma(str: String): String {
        var str = str
        if (str.endsWith(",")) {
            str = str.substring(0, str.length - 1)
        }
        return str
    }


    fun setEditKeyStyle(editText: EditText?) {
        if (editText == null) return
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        val digists = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        editText.keyListener = DigitsKeyListener.getInstance(digists)
    }

    /**
     * 汉语转拼音--小写
     */
    fun getUpperCasePinYin(content: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.setLength(0)
        for (i in 0 until content.length) {
            stringBuilder.append(Pinyin.toPinyin(content[i]))
        }
        return stringBuilder.toString().toUpperCase()
    }

    /**
     * 返回拼音首字母大写
     *
     * @param str
     * @return
     */
    fun getPinYinFirstUpperCase(str: String): String {
        val pinyin = getUpperCasePinYin(str)
        return pinyin.substring(0, 1).toUpperCase()
    }
}
