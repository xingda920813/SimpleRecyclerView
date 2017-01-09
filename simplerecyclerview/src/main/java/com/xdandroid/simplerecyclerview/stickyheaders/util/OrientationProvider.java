package com.xdandroid.simplerecyclerview.stickyheaders.util;

import android.support.v7.widget.*;

/**
 * Interface for getting the orientation of a RecyclerView from its LayoutManager
 */
public interface OrientationProvider {

  int getOrientation(RecyclerView recyclerView);

  boolean isReverseLayout(RecyclerView recyclerView);
}
