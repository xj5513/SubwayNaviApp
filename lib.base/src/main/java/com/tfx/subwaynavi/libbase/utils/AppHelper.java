package com.tfx.subwaynavi.libbase.utils;

import android.content.Context;
import android.graphics.Typeface;


public class AppHelper {

    private static AppHelper sInstance ;
    private Typeface mTypeface ;

    private AppHelper() {

    }

    public static AppHelper getInstance() {
        if (sInstance == null) {
            sInstance = new AppHelper() ;
        }
        return sInstance ;
    }

    public Typeface getTypeface(Context context) {
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/wmsfont.ttf");
        }
        return mTypeface ;
    }
}
