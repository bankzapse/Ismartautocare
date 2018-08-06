package com.ismartautocare.MyFontsStyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by AndroidDeveloper on 9/27/16 AD.
 */
@SuppressLint("AppCompatCustomView")
public class MyTextViewFonts extends TextView {

    public MyTextViewFonts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewFonts(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setTextSize(20);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        setTypeface(tf);
    }

}