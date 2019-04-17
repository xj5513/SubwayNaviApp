package com.tfx.subwaynavi.libbase;

import android.app.Application;

/**
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/2
 */

public class BaseApplication extends Application {
    private static BaseApplication sInstance;

    public BaseApplication() {
        sInstance = this;
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }
}
