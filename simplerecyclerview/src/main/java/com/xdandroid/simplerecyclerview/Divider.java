package com.xdandroid.simplerecyclerview;

import android.graphics.*;
import android.graphics.drawable.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;

public class Divider extends RecyclerView.ItemDecoration {

    protected Drawable mDivider;
    protected boolean mIsHorizontalList;
    @Px protected int mWidth;
    @Px protected int mLeftOffset;
    @Px protected int mTopOffset;
    @Px protected int mRightOffset;
    @Px protected int mBottomOffset;

    public Divider(@Px int width, @ColorInt int color, boolean isHorizontalList, @Px int leftOffset, @Px int topOffset, @Px int rightOffset, @Px int bottomOffset) {
        mWidth = width;
        mDivider = new ColorDrawable(color);
        mIsHorizontalList = isHorizontalList;
        mLeftOffset = leftOffset;
        mTopOffset = topOffset;
        mRightOffset = rightOffset;
        mBottomOffset = bottomOffset;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mIsHorizontalList) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    protected void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount() - 2;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mWidth;
            mDivider.setBounds(left + mLeftOffset, top + mTopOffset, right + mRightOffset, bottom + mBottomOffset);
            mDivider.draw(c);
        }
    }

    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount() - 2;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mWidth;
            mDivider.setBounds(left + mLeftOffset, top + mTopOffset, right + mRightOffset, bottom + mBottomOffset);
            mDivider.draw(c);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mIsHorizontalList) {
            outRect.set(0, 0, mWidth, 0);
        } else {
            outRect.set(0, 0, 0, mWidth);
        }
    }
}
