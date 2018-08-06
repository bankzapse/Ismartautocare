package com.ismartautocare.MyFontsStyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyTextViewFontsGeneral extends TextView {

    public MyTextViewFontsGeneral(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewFontsGeneral(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewFontsGeneral(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        setTypeface(tf);
    }

}