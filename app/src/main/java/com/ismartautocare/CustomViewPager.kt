package com.ismartautocare

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent



/**
 * Created by programmer on 10/9/17.
 */
internal class CustomViewPager(context: Context, attrs: AttributeSet) : android.support.v4.view.ViewPager(context, attrs) {
    var isPagingEnabled: Boolean = false

    init {
        this.isPagingEnabled = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isPagingEnabled) super.onTouchEvent(event) else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (isPagingEnabled) super.onInterceptTouchEvent(event) else false
    }

}