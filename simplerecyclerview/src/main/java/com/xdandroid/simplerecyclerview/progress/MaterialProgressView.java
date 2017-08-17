package com.xdandroid.simplerecyclerview.progress;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.simplerecyclerview.Adapter;
import com.xdandroid.simplerecyclerview.*;

public class MaterialProgressView extends com.xdandroid.materialprogressview.MaterialProgressView {

    public MaterialProgressView(Context context) {
        super(context);
    }

    public MaterialProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaterialProgressView init(Adapter a) {
        if (a.mColorSchemeColors == null || a.mColorSchemeColors.length <= 0) {
            int colorAccentId = getContext().getResources().getIdentifier("colorAccent", "color", getContext().getPackageName());
            int color;
            if (colorAccentId > 0) {
                color = getContext().getResources().getColor(colorAccentId);
            } else {
                color = Color.parseColor("#607D8B");
            }
            setColorSchemeColors(new int[]{color});
        } else {
            setColorSchemeColors(a.mColorSchemeColors);
        }
        if (a.mProgressBackgroundColor != 0) {
            setProgressBackgroundColor(a.mProgressBackgroundColor);
        }
        FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerParams.setMargins(0, UIUtils.dp2px(getContext(), 6), 0, UIUtils.dp2px(getContext(), 6));
        innerParams.gravity = Gravity.CENTER;
        setLayoutParams(innerParams);
        setId(android.R.id.secondaryProgress);
        return this;
    }
}
