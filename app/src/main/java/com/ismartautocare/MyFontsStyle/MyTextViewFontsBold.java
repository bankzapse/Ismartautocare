package com.ismartautocare.MyFontsStyle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by programmer on 3/8/18.
 */

public class MyTextViewFontsBold extends TextView {

    public MyTextViewFontsBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewFontsBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewFontsBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setTextSize(20);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        setTypeface(tf);
    }

}