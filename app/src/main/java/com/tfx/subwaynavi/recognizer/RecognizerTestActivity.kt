package com.tfx.subwaynavi.recognizer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.tfx.subwaynavi.R
import com.tfx.subwaynavi.libbase.base.BaseActivity
import kotlinx.android.synthetic.main.test_recognizer_activity.*
import java.util.ArrayList
import java.util.HashMap

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/4
 *
 */
class RecognizerTestActivity : BaseActivity() {


    private lateinit var mRecognizer : SubwayRecognizer

    private lateinit var mHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()


        mHandler = object : Handler(){
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                mResult.text = msg?.obj.toString()
            }
        }

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
                    mResult.text = results[0]
                }
            }

            Log.i("EventListener----", "asr name:" + name)
            if (name == SpeechConstant.CALLBACK_EVENT_ASR_FINISH) {
                Log.i("EventListener----", "asr finish:" + params)
                // 识别结束
                val recogResult = RecogResult.parseJson(params)
                if (recogResult.hasError()) {
                    val errorCode = recogResult.error
                    val subErrorCode = recogResult.subError
                    Log.e("EventListener----", "asr error:" + params)
                } else {
                    //mResult.text = recogResult.origalResult

                }
            }

        // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
        }

        val listener = MessageStatusRecogListener(mHandler)
        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        mRecognizer = SubwayRecognizer(this, mListener)

        initView()
    }

    private fun initView() {
        mStart.setOnClickListener { start() }
        mStop.setOnClickListener { stop() }
    }

    override fun getMainContentResId(): Int {
        return R.layout.test_recognizer_activity
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     * 基于DEMO集成2.1, 2.2 设置识别参数并发送开始事件
     */
    private fun start() {

        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        val params = fetchParams()

        // 复制此段可以自动检测常规错误
//        AutoCheck(applicationContext, object : Handler() {
//            override fun handleMessage(msg: Message) {
//                if (msg.what == 100) {
//                    val autoCheck = msg.obj as AutoCheck
//                    synchronized(autoCheck) {
//                        val message = autoCheck.obtainErrorMessage() // autoCheck.obtainAllMessage();
//                        mResult.text = message
//                        // Log.w("AutoCheckMessage", message);
//                    }// 可以用下面一行替代，在logcat中查看代码
//                }
//            }
//        }, false).checkAsr(params)
        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印
        Log.i("RecognizerTestActivity-", "----设置的start输入参数：" + params)
        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        mRecognizer.start(params)
    }

    private fun fetchParams(): Map<String, Any> {
        val map = HashMap<String, Any>()
        //{accept-audio-volume=false, pid=1536}
        map.put("accept-audio-volume",false)
        map.put("accept-audio-data",false)
        map.put("disable-punctuation",false)
        map.put("pid",1536)
        //map.put(SpeechConstant.DECODER, 2)
        //map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets:///baidu_speech_grammar.bsg")
        return map
    }

    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     * 基于DEMO集成4.1 发送停止事件 停止录音
     */
    private fun stop() {
        mRecognizer.stop()
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     * 基于DEMO集成4.2 发送取消事件 取消本次识别
     */
    private fun cancel() {

        mRecognizer.cancel()
    }

    /**
     * 销毁时需要释放识别资源。
     */
    override fun onDestroy() {

        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        // 基于DEMO5.1 卸载离线资源(离线时使用) release()方法中封装了卸载离线资源的过程
        // 基于DEMO的5.2 退出事件管理器
        mRecognizer.release()
        Log.i("RecognizerTestActivity-", "------onDestory")
        super.onDestroy()
    }



    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.

            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toTypedArray(), 123)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。

    }
}