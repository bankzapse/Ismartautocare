package com.ismartautocare.MyFontsStyle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by AndroidDeveloper on 9/27/16 AD.
 */
public class MyButtonFonts extends Button {


    public MyButtonFonts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonFonts(Context context) {
        super(context);
        init();

    }

    private void init() {
//        this.setTextSize(25);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        setTypeface(tf);
    }

}
