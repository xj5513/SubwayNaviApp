package com.tfx.subwaynavi.map

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.tfx.subwaynavi.base.BaseWebviewActivity
import com.tfx.subwaynavi.libbase.utils.UiUtil
import com.tfx.subwaynavi.recognizer.RecogResult
import com.tfx.subwaynavi.recognizer.SubwayRecognizer
import kotlinx.android.synthetic.main.activity_metro_map.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/11/26
 *
 */
class MetroMapActivity : BaseWebviewActivity(){

    private var mUrl = "http://47.254.42.238:81/metro_demo.html"
    private lateinit var mRecognizer : SubwayRecognizer
    private var mResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        showLoadingDialog()
        mMetroWebView.loadUrl(mUrl)
        mMetroFloatingBtn.show()
        mLineSearch.visibility = View.VISIBLE
        mMetroFloatingBtn.setOnClickListener { recognizer() }
        initRecognize()

        mLineSearchBtn.setOnClickListener {
            search()
        }
    }

    private fun search(){
        if(TextUtils.isEmpty(mSearchStart.text.toString())){
            UiUtil.showToast("请先输入起点位置")
            return
        }

        if(TextUtils.isEmpty(mSearchEnd.text.toString())){
            UiUtil.showToast("请先输入终点位置")
            return
        }
        setRoute(mMetroWebView,mSearchStart.text.toString()+"到"+mSearchEnd.text.toString())
    }


    /**
     * 弹框----语音录入
     */
    private fun recognizer(){
//        val dialog = RecognizerDialog()
//        dialog.setDataCallBack {
//            result ->
//            setRoute(mMetroWebView,result)
//        }
//        dialog.display(supportFragmentManager.beginTransaction())

        UiUtil.showToast("请语音输入线路起点和终点，如：安定门到新街口")
        mMetroFloatingBtn.postDelayed(Runnable {
            startRecognize()
        },300)

    }

    override fun onPause() {
        super.onPause()
        mMetroWebView.loadUrl("about:blank")
    }

    @JavascriptInterface
    fun showInfoFromJs(msg : String) {

    }

    fun setRoute(webView: WebView, routeParam : String) = webView.loadUrl(
            "javascript:javaShowRoute('$routeParam')"
    )

    private fun initRecognize(){
        val mListener = EventListener { name, params, data, offset, length ->
            if (name == SpeechConstant.CALLBACK_EVENT_ASR_READY) {
                // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
            }

            if(name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL){
                Log.i("EventListener----", "asr partial:" + params)
                val recogResult = RecogResult.parseJson(params)
                // 临时识别结果, 长语音模式需要从此消息中取出结果
                val results = recogResult.resultsRecognition
                if(recogResult.isFinalResult){

                    mResult = results[0]
                    if(mResult.contains("t")){
                        mResult = mResult.replace("t","T")
                    }
                    UiUtil.showToast("您的路线为$mResult")
                    setRoute(mMetroWebView,mResult)
                }
            }

            Log.i("EventListener----","name--------------" + name)
            if (name == SpeechConstant.CALLBACK_EVENT_ASR_VOLUME){
                Log.i("EventListener---- ","asr volume event:" + params)
                val vol = parseVolumeJson(params)
                //mVoiceView.setCurrentDBLevelMeter(vol.volume.toFloat())
            }
            if (name == SpeechConstant.CALLBACK_EVENT_ASR_FINISH) {
                // 识别结束
                val recogResult = RecogResult.parseJson(params)
                if (recogResult.hasError()) {
                    val errorCode = recogResult.error
                    val subErrorCode = recogResult.subError
                    Log.e("EventListener----", "asr error:" + params)
                }
            }
        }

        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        mRecognizer = SubwayRecognizer(this, mListener)
    }
    private fun parseVolumeJson(jsonStr: String): Volume {
        val vol = Volume()
        vol.origalJson = jsonStr
        try {
            val json = JSONObject(jsonStr)
            vol.volumePercent = json.getInt("volume-percent")
            vol.volume = json.getInt("volume")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return vol
    }

    private inner class Volume {
        var volumePercent = -1
        var volume = -1
        var origalJson: String? = null
    }

    private fun startRecognize(){
        val params = fetchParams()
        Log.i("RecognizerDialog-", "----设置的start输入参数：" + params)
        mRecognizer.start(params)
        //mVoiceView.startPreparingAnimation()
        //mVoiceView.startRecognizingAnimation()
        mResult = ""
    }

    private fun fetchParams(): Map<String, Any> {
        val map = HashMap<String, Any>()
        //{accept-audio-volume=false, pid=1536}
        map.put("accept-audio-volume",false)
        map.put("accept-audio-data",false)
        map.put("disable-punctuation",false)
        map.put("pid",1536)
        return map
    }
}