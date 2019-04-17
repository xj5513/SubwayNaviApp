package com.tfx.subwaynavi.recognizer

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/4
 *
 */
interface IRecogListener {

    /**
     * CALLBACK_EVENT_ASR_READY
     * ASR_START 输入事件调用后，引擎准备完毕
     */
    abstract fun onAsrReady()

    /**
     * CALLBACK_EVENT_ASR_BEGIN
     * onAsrReady后检查到用户开始说话
     */
    abstract fun onAsrBegin()

    /**
     * CALLBACK_EVENT_ASR_END
     * 检查到用户开始说话停止，或者ASR_STOP 输入事件调用后，
     */
    abstract fun onAsrEnd()

    /**
     * CALLBACK_EVENT_ASR_PARTIAL resultType=partial_result
     * onAsrBegin 后 随着用户的说话，返回的临时结果
     *
     * @param results     可能返回多个结果，请取第一个结果
     * @param recogResult 完整的结果
     */
    abstract fun onAsrPartialResult(results: Array<String>, recogResult: RecogResult)

    /**
     * 语音的在线语义结果
     *
     * CALLBACK_EVENT_ASR_PARTIAL resultType=nlu_result
     * @param nluResult
     */
    abstract fun onAsrOnlineNluResult(nluResult: String)

    /**
     * CALLBACK_EVENT_ASR_PARTIAL resultType=final_result
     * 最终的识别结果
     *
     * @param results     可能返回多个结果，请取第一个结果
     * @param recogResult 完整的结果
     */
    abstract fun onAsrFinalResult(results: Array<String>, recogResult: RecogResult)

    /**
     * CALLBACK_EVENT_ASR_FINISH
     * @param recogResult 完整的结果
     */
    abstract fun onAsrFinish(recogResult: RecogResult)

    /**
     * CALLBACK_EVENT_ASR_FINISH error!=0
     *
     * @param errorCode
     * @param subErrorCode
     * @param descMessage
     * @param recogResult
     */
    abstract fun onAsrFinishError(errorCode: Int, subErrorCode: Int, descMessage: String,
                                  recogResult: RecogResult)

    /**
     * 长语音识别结束
     */
    abstract fun onAsrLongFinish()

    /**
     * CALLBACK_EVENT_ASR_VOLUME
     * 音量回调
     *
     * @param volumePercent 音量的相对值，百分比，0-100
     * @param volume 音量绝对值
     */
    abstract fun onAsrVolume(volumePercent: Int, volume: Int)

    /**
     * CALLBACK_EVENT_ASR_AUDIO
     * @param data pcm格式，16bits 16000采样率
     *
     * @param offset
     * @param length
     */
    abstract fun onAsrAudio(data: ByteArray, offset: Int, length: Int)

    /**
     * CALLBACK_EVENT_ASR_EXIT
     * 引擎完成整个识别，空闲中
     */
    abstract fun onAsrExit()

    /**
     * CALLBACK_EVENT_ASR_LOADED
     * 离线命令词资源加载成功
     */
    abstract fun onOfflineLoaded()

    /**
     * CALLBACK_EVENT_ASR_UNLOADED
     * 离线命令词资源释放成功
     */
    abstract fun onOfflineUnLoaded()
}