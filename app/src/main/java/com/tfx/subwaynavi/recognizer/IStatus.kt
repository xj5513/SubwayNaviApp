package com.tfx.subwaynavi.recognizer

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/5
 *
 */
interface IStatus {
    companion object {

        const val STATUS_NONE = 2

        const val STATUS_READY = 3
        const val STATUS_SPEAKING = 4
        const val STATUS_RECOGNITION = 5

        const val STATUS_FINISHED = 6
        const val STATUS_LONG_SPEECH_FINISHED = 7
        const val STATUS_STOPPED = 10

        const val STATUS_WAITING_READY = 8001
        const val WHAT_MESSAGE_STATUS = 9001

        const val STATUS_WAKEUP_SUCCESS = 7001
        const val STATUS_WAKEUP_EXIT = 7003
    }
}