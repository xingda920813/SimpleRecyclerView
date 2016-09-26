package com.xdandroid.simplerecyclerview;

import android.graphics.*;
import android.support.annotation.*;

public class Divider extends AbsDivider {

    @PxValue protected int mWidth;
    @ColorInt protected int mColor;
    @PxValue protected int mLeftOffset;
    @PxValue protected int mTopOffset;
    @PxValue protected int mRightOffset;
    @PxValue protected int mBottomOffset;

    public Divider(@PxValue int width, @ColorInt int color, boolean isHorizontalList, @PxValue int leftOffset, @PxValue int topOffset, @PxValue int rightOffset, @PxValue int bottomOffset) {
        mWidth = width;
        mColor = color;
        mIsHorizontalList = isHorizontalList;
        mLeftOffset = leftOffset;
        mTopOffset = topOffset;
        mRightOffset = rightOffset;
        mBottomOffset = bottomOffset;
    }

    @Override
    protected int getWidth(int position) {
        return mWidth;
    }

    @Override
    protected int getColor(int position) {
        return mColor;
    }

    @Override
    protected void getOffsets(int position, @PxValue @NonNull Rect outRect, Void left_top_right_bottom) {
        outRect.set(mLeftOffset, mTopOffset, mRightOffset, mBottomOffset);
    }
}
