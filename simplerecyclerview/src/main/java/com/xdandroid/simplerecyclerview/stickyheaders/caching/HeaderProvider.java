package com.xdandroid.simplerecyclerview.stickyheaders.caching;

import android.support.v7.widget.*;
import android.view.*;

/**
 * Implemented by objects that provide header views for decoration
 */
public interface HeaderProvider {

  /**
   * Will provide a header view for a given position in the RecyclerView
   *
   * @param recyclerView that will display the header
   * @param position     that will be headed by the header
   * @return a header view for the given position and mList
   */
  View getHeader(RecyclerView recyclerView, int position);

  void invalidate();
}
