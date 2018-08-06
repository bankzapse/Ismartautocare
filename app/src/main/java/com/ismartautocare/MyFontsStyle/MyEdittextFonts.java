package com.ismartautocare.MyFontsStyle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by AndroidDeveloper on 9/27/16 AD.
 */
public class MyEdittextFonts extends EditText {

    public MyEdittextFonts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEdittextFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEdittextFonts(Context context) {
        super(context);
        init();
    }

    private void init() {
//        this.setTextSize(25);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 0, 0, 0);
        this.setLayoutParams(lp);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Light.ttf");
        setTypeface(tf);
    }

}