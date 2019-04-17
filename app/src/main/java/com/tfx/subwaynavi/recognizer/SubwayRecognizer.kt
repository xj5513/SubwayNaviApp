package com.tfx.subwaynavi.recognizer

import android.content.Context
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import org.json.JSONObject

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/4
 *
 */
class SubwayRecognizer {

    //百度语音 内部核心 EventManager
    private var asr : EventManager ?= null

    //内部核心 事件回掉类，用于开发写自己的识别回掉逻辑
    private lateinit var eventListener : EventListener

    companion object {

        // 是否加载离线资源
        @Volatile private var isOfflineEngineLoaded = false

        // 未release前，只能new一个
        @Volatile private var isInited = false


        private val TAG = "Subway-Recognizer"
    }

    constructor(context: Context , recognitionListener: IRecogListener) : this(context,RecogEventAdapter(recognitionListener))

    constructor(context: Context , eventListener: EventListener){
        if (isInited) {
            Log.e(TAG, "还未调用release()，请勿新建一个新类")
            throw RuntimeException("还未调用release()，请勿新建一个新类")
        }
        isInited = true
        this.eventListener = eventListener
        // SDK集成步骤 初始化asr的EventManager示例，多次得到的类，只能选一个使用
        asr = EventManagerFactory.create(context, "asr")
        // SDK集成步骤 设置回调event， 识别引擎会回调这个类告知重要状态和识别结果
        asr?.registerListener(eventListener)
    }

    /**
     * 离线命令词，在线不需要调用
     *
     * @param params 离线命令词加载参数，见文档“ASR_KWS_LOAD_ENGINE 输入事件参数”
     */
    fun loadOfflineEngine(params: Map<String, Any>) {
        val json = JSONObject(params).toString()
        Log.i(TAG + ".Debug", "离线命令词初始化参数（反馈请带上此行日志）:" + json)
        // SDK集成步骤（可选）加载离线命令词(离线时使用)
        asr?.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0)
        isOfflineEngineLoaded = true
        // 没有ASR_KWS_LOAD_ENGINE这个回调表试失败，如缺少第一次联网时下载的正式授权文件。
    }

    /**
     * @param params
     */
    fun start(params: Map<String, Any>) {
        // SDK集成步骤 拼接识别参数
        val json = JSONObject(params).toString()
        Log.i(TAG + ".Debug", "识别参数（反馈请带上此行日志）" + json)
        asr?.send(SpeechConstant.ASR_START, json, null, 0, 0)
    }

    /**
     * 提前结束录音等待识别结果。
     */
    fun stop() {
        Log.i(TAG, "停止录音")
        // SDK 集成步骤（可选）停止录音
        if (!isInited) {
            throw RuntimeException("release() was called")
        }
        asr?.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0)
    }

    /**
     * 取消本次识别，取消后将立即停止不会返回识别结果。
     * cancel 与stop的区别是 cancel在stop的基础上，完全停止整个识别流程，
     */
    fun cancel() {
        Log.i(TAG, "取消识别")
        if (!isInited) {
            throw RuntimeException("release() was called")
        }
        // SDK集成步骤 (可选） 取消本次识别
        asr?.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
    }

    fun release() {
        if (asr == null) {
            return
        }
        cancel()
        if (isOfflineEngineLoaded) {
            // SDK集成步骤 如果之前有调用过 加载离线命令词，这里要对应释放
            asr?.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0)
            isOfflineEngineLoaded = false
        }
        // SDK 集成步骤（可选），卸载listener
        asr?.unregisterListener(eventListener)
        asr = null
        isInited = false
    }

    fun setEventListener(recogListener: IRecogListener) {
        if (!isInited) {
            throw RuntimeException("release() was called")
        }
        this.eventListener = RecogEventAdapter(recogListener)
        asr?.registerListener(eventListener)
    }

}