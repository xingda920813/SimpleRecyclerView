package com.xdandroid.simplerecyclerview;

import android.graphics.*;
import android.graphics.drawable.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;

public abstract class AbsDivider extends RecyclerView.ItemDecoration {

    protected ColorDrawable mDivider = new ColorDrawable();
    protected Boolean mIsHorizontalList = null;

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (isHorizontalList(parent)) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    protected void drawVertical(Canvas c, RecyclerView parent) {
        Rect r = new Rect();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount() - 2;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + getWidth(i);
            getOffsets(i, r, null);
            mDivider.setBounds(left + r.left, top + r.top, right + r.right, bottom + r.bottom);
            mDivider.setColor(getColor(i));
            mDivider.draw(c);
        }
    }

    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        Rect r = new Rect();
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount() - 2;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + getWidth(i);
            getOffsets(i, r, null);
            mDivider.setBounds(left + r.left, top + r.top, right + r.right, bottom + r.bottom);
            mDivider.setColor(getColor(i));
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position >= parent.getChildCount() - 2) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (isHorizontalList(parent)) {
            outRect.set(0, 0, getWidth(position), 0);
        } else {
            outRect.set(0, 0, 0, getWidth(position));
        }
    }

    protected boolean isHorizontalList(RecyclerView parent) {
        if (mIsHorizontalList == null) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (!(layoutManager instanceof LinearLayoutManager)) return false;
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            mIsHorizontalList = linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL;
        }
        return mIsHorizontalList;
    }

    @PxValue protected abstract int getWidth(int position);

    @ColorInt protected abstract int getColor(int position);

    @PxValue protected abstract void getOffsets(int position, @PxValue @NonNull Rect outRect, Void left_top_right_bottom);
}
