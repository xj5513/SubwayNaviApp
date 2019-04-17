package com.tfx.subwaynavi.map

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDialogFragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.libbase.utils.UiUtil
import com.tfx.subwaynavi.libbase.widget.IconFont
import com.tfx.subwaynavi.recognizer.RecogResult
import com.tfx.subwaynavi.recognizer.SubwayRecognizer
import com.tfx.subwaynavi.widget.SDKAnimationView
import org.jetbrains.anko.find
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/6
 *
 */
class RecognizerDialog : AppCompatDialogFragment() {

    private lateinit var mRootView : View
    private lateinit var mSpeechBack : IconFont
    private lateinit var mRecognizerResult : TextView
    private lateinit var mVoiceView : SDKAnimationView
    private lateinit var mSpeechOver : TextView
    private lateinit var mSpeechConfirm : TextView
    private lateinit var mSpeechRetry : TextView
    private lateinit var mBottomResult : LinearLayout
    private lateinit var mRecognizer : SubwayRecognizer

    private var mDataCallBack : ((String) -> Unit)? = null
    private var mResult = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(dialogResourceId(), container, false)
        initView()
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPermission()
        start()
    }

    private fun initView(){
        mSpeechBack = mRootView.find(R.id.mSpeechBack)
        mRecognizerResult = mRootView.find(R.id.mRecognizerResult)
        mVoiceView = mRootView.find(R.id.mVoiceView)
        mSpeechOver = mRootView.find(R.id.mSpeechOver)
        mSpeechConfirm = mRootView.find(R.id.mSpeechConfirm)
        mSpeechRetry = mRootView.find(R.id.mSpeechRetry)
        mBottomResult = mRootView.find(R.id.mBottomResult)

//        var hue = 0F
//        val cm = ColorMatrix()
//        ColorFilterGenerator.adjustColor(cm, 0F, 0F, 0F, hue)
//        val filter = ColorMatrixColorFilter(cm)
//        mVoiceView.setHsvFilter(filter)
        //mVoiceView.startInitializingAnimation()
        initListener()
        initRecognize()
    }

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
                    mRecognizerResult.text = mResult
                    mBottomResult.visibility = View.VISIBLE
                    mSpeechOver.visibility = View.GONE
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
                    mBottomResult.visibility = View.VISIBLE
                    mSpeechOver.visibility = View.GONE
                }
            }
        }

        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        mRecognizer = SubwayRecognizer(context!!, mListener)
    }

    /**
     * 是否首字母是英文字母
     */
    private fun isUpperOrLowerCase(str : String) : Boolean{
        val char = str.toCharArray()[0]
        //大写字母
        return char.isUpperCase() || char.isLowerCase()
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

    private fun initListener() {
        mSpeechBack.setOnClickListener { cancel() }
        mSpeechConfirm.setOnClickListener { confirm() }
        //重试
        mSpeechRetry.setOnClickListener { start() }

        mSpeechOver.setOnClickListener { stop() }
    }

    private fun confirm(){
        if((!TextUtils.isEmpty(mResult)) && mDataCallBack!= null){
            mDataCallBack?.invoke(mResult)
            cancel()
        }else{
            UiUtil.showToast("请录入线路起点和终点，如：安定门到新街口")
        }
    }

    fun setDataCallBack(callBack : (String) -> Unit){
        this.mDataCallBack = callBack
    }

    private fun start(){
        val params = fetchParams()
        Log.i("RecognizerDialog-", "----设置的start输入参数：" + params)
        mRecognizer.start(params)
        //mVoiceView.startPreparingAnimation()
        //mVoiceView.startRecognizingAnimation()
        mResult = ""
        mRecognizerResult.text = ""
        mBottomResult.visibility = View.GONE
        mSpeechOver.visibility = View.VISIBLE
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

    override fun onDismiss(dialog: DialogInterface?) {
        mRecognizer.release()
        super.onDismiss(dialog)
    }

    private fun stop(){
        mRecognizer.stop()
        //mVoiceView.resetAnimation()
    }

    private fun cancel(){
        //mVoiceView.resetAnimation()
        mRecognizer.cancel()
        dismiss()
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(context!!, perm)) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.

            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(activity!!, toApplyList.toTypedArray(), 123)
        }
    }

    fun display(transaction : FragmentTransaction){
        show(transaction,RecognizerDialog::javaClass.name)
    }

    private fun showDialog(transaction: FragmentTransaction, tag: String) {
        if (isAdded) {
            transaction.show(this)
        } else {
            transaction.add(this, tag)
            transaction.commitAllowingStateLoss()
        }
    }

    private fun dialogResourceId(): Int {
        return R.layout.dialog_speech
    }
}