package com.xdandroid.simplerecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by XingDa on 2016/05/29.
 */

public class Divider extends RecyclerView.ItemDecoration {

    protected Drawable mDivider;
    protected boolean mIsHorizontal;
    protected int leftOffset,topOffset,rightOffset,bottomOffset;

    public Divider(Context context, @Nullable Integer dividerDrawableResId, boolean isHorizontal, int leftOffset, int topOffset, int rightOffset, int bottomOffset) {
        if (dividerDrawableResId != null && dividerDrawableResId > 0) {
            this.mDivider = context.getResources().getDrawable(dividerDrawableResId);
        } else {
            final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
            this.mDivider = a.getDrawable(0);
            a.recycle();
        }
        this.mIsHorizontal = isHorizontal;
        this.leftOffset = leftOffset;
        this.topOffset = topOffset;
        this.rightOffset = rightOffset;
        this.bottomOffset = bottomOffset;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mIsHorizontal) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    protected void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left + leftOffset, top + topOffset, right + rightOffset, bottom + bottomOffset);
            mDivider.draw(c);
        }
    }

    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left + leftOffset, top + topOffset, right + rightOffset, bottom + bottomOffset);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mIsHorizontal) {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        } else {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}
