package com.tfx.subwaynavi.libbase.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tfx.subwaynavi.libbase.utils.AppHelper;


/**
 * Created by freedy on 1/17/17.
 */

public class IconFont extends AppCompatTextView {
    public IconFont(Context context) {
        this(context , null) ;
    }

    public IconFont(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFont() ;
    }

    private void initFont() {
        Typeface typeface = AppHelper.getInstance().getTypeface(getContext()) ;
        setTypeface(typeface);
    }
}
