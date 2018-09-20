package com.qsjt.qingshan.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TypeFaceTextView extends AppCompatTextView {

    public TypeFaceTextView(Context context) {
        this(context, null);
    }

    public TypeFaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypeFaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        this.setTypeface(typeface);
    }
}
