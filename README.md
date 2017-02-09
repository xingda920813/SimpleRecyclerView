# SimpleRecyclerView

[中文 README](https://github.com/xingda920813/SimpleRecyclerView/blob/master/README_zh.md)

### An enhancement to RecyclerView and SwipeRefreshLayout. Integrated with timehop/sticky-headers-recyclerview for sticky headers.

[https://github.com/timehop/sticky-headers-recyclerview](https://github.com/timehop/sticky-headers-recyclerview "timehop/sticky-headers-recyclerview")

![Alt text](https://raw.githubusercontent.com/xingda920813/SimpleRecyclerView/master/video.gif)

### Nain Characters:

#### 1. Pull-To-Refresh

An enhancement to SwipeRefreshLayout in 2 aspects:

- In some versions of android.support library, SwipeRefreshLayout has
sliding conflict with AppbarLayout. When you pull down RecyclerView, SwipeRefreshLayout will appear instantly, preventing you from pulling down the list.

- Now you can invoke setRefreshing(true) to show loading progress in onCreate() while the official's can not.

#### 2.Load more

- Automatically load more data when there are still ${THRESHOLD} items to the bottom (THRESHOLD customizable)

- If user slides to the bottom and the loading process is still incomplete, the loading progress animation will be displayed.

- LayoutManager irrelevant

- 2 indicator styles available: ProgressBar and SwipeRefreshLayout (The SwipeRefreshLayout style is independent of the Android version, with indicator color and background color Customizable. The ProgressBar style is dependent on the Android version. Its Material Design style is only available in API 21+.)

#### 3.Loading View / Empty View / Error View

#### 4.OnItemClickListener / OnItemLongClickListener

#### 5.Sticky headers

- Supports any number of fixed header types

#### 6.Item divider support

- Customizable divider width and color

- You can customize the length of the blank area on the left / top / right / bottom of the divider (divider will not draw in blank area)

- Supports horizontal / vertical LinearLayoutManager

#### 7.Item animation

- Material Design animation when initing RecyclerView, adding / modifying / deleting items

#### 8.Support for group display

#### 9.Get scrolled distance and distance to end

SimpleRecyclerView added 2 methods to get these distances;

Meanwhile, these two distances are passed as parameters in SimpleOnScrollListener.onScrolled.

```
//Scrolled distance (px)
int SimpleRecyclerView.getScrolledDistance();

//Distance to end (px)
int SimpleRecyclerView.getDistanceToEnd();

abstract class SimpleOnScrollListener extends RecyclerView.OnScrollListener {
  abstract void onScrollStateChanged(int scrolledDistance, int distanceToEnd, int newState);
  /**
  * @param scrolledDistance Scrolled distance (px)
  * @param distanceToEnd Distance to end (px)
  * @param velocity Current scroll velocity (positive or negative indicates the direction)
  */
  abstract void onScrolled(int scrolledDistance, int distanceToEnd, int velocity);
}
```

## Import

### 1.Add binary

In build.gradle, add

    compile 'com.xdandroid:simplerecyclerview:+'

### 2.Basic usage is the same as official RecyclerView and SwipeRefreshLayout

- Simple* classes inherit from the official widgets

- Please refer to the demo for usage

- If you have multiple viewType (Adapter inherits com.xdandroid.simplerecyclerview.Adapter), The ways of setting data to Adapter are the same as RecyclerView.Adapter. You can pass the data set by the constructor, or you can create a method called setList or so, setting data set to the Adapter and refresh UI using notifyDataSetChanged().

- If there is only one viewType (Adapter inherits SingleViewTypeAdapter<${JavaBean}>), the method of setting data list to Adapter is:

.

    recyclerView.setAdapter(adapter);
    void Adapter.setList(List<${JavaBean}> list);

### 3.Layout XML

	<com.xdandroid.simplerecyclerview.SimpleSwipeRefreshLayout
        tools:context="com.xdandroid.sample.MainActivity"
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        app:layout_behavior="@string/appbar_scrolling_view_behavior"
        tools:showIn="@layout/activity_main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/swipe_container">

        <com.xdandroid.simplerecyclerview.SimpleRecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/recycler_view"/>

    </com.xdandroid.simplerecyclerview.SimpleSwipeRefreshLayout>

    <!--Empty View-->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/empty_view"
        android:visibility="gone"/>

    <!--Error View-->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/error_view"
        android:visibility="gone"/>

    <!--Loading View-->
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/loading_view">

        <ProgressBar
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_marginBottom="20dp"/>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:text="Hello Loading View"
            android:layout_marginTop="20dp"
            android:textSize="20sp"
            android:textColor="@android:color/black"/>
    </FrameLayout>

### 4. Define Adapter abstract class

#### 4.1 When you only have one viewType, subclass SingleViewTypeAdapter<${JavaBean}>

- Override onViewHolderCreate, corresponds to onCreateViewHolder in RecyclerView.Adapter

- Override onViewHolderBind, corresponds to onBindViewHolder in RecyclerView.Adapter

- Create ViewHolder

- Override int getItemSpanSize(int position, int viewType, int spanCount) if you use GridLayoutManager

- Do not override onLoadMore or hasMoreElements, instead implement them when instantiat Adapter in Activity / Fragment.

#### 4.2 Multiple viewType, subclass Adapter

- Override onViewHolderCreate, corresponds to onCreateViewHolder in RecyclerView.Adapter

- Override onViewHolderBind, corresponds to onBindViewHolder in RecyclerView.Adapter

- Override getViewType, corresponds to getItemViewType in RecyclerView.Adapter

- Override getCount, corresponds to getItemCount in RecyclerView.Adapter

- Create ViewHolder classes for each viewType

- Override int getItemSpanSize(int position, int viewType, int spanCount) if you use GridLayoutManager

- Do not override onLoadMore or hasMoreElements, instead implement them when instantiat Adapter in Activity / Fragment.

## Pull-To-Refresh

Resolve a sliding conflict with AppbarLayout: Assign an id to AppBarLayout named "appbar" (android:id="@+id/appbar")

To automatically leave the height for Toolbar and prevent from being blocked by the Toolbar, add app:layout_behavior="@string/appbar_scrolling_view_behavior" to SwipeRefreshLayout or the layout under CoordinatorLayout.

## Load more

### Implement 2 methods when instantiate Adapter:

#### 1. void onLoadMore(Void please\_make\_your\_adapter\_class\_as\_abstract\_class) :

First self-increment pageIndex varible by one, then invoke API to get more data. After new piece of data is parsed:

**For SingleViewTypeAdapter**, Do NOT call List[E].addAll(Collection[? extends E]) on your data set, instead call void SingleViewTypeAdapter.addAll(List[E]) directly. SingleViewTypeAdapter will update the underlying data set automatically.

**For Adapter**, first update your data set using List[E].addAll(Collection[? extends E]), then call void Adapter.onAddedAll(int newDataSize) to notify Adapter that some data has been added to the data set.

#### 2. boolean hasMoreElements(Void let\_activity\_or\_fragment\_implement\_these\_methods) :

Tell the Adapter whether there is more data to load.

if needed, you can call void Adapter.setLoadingFalse() to restore the status of not loading more.

### Customize style:

	adapter.setUseMaterialProgress(true, new int[]{getResources().getColor(R.color.colorAccent)});

When the first parameter (boolean useMaterialProgress) is true, SwipeRefreshLayout style is used, otherwise ProgressBar style is used. The second parameter (int[] colors) is available only if useMaterialProgress is true. You can pass an int[] in the method to set the color set of the loading indicator. If there are more than one color in the int[], the indicator will iterate the colors at the frequency of one color per turn.

	adapter.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorAccent)});

Call the method above to change the indicator color at any time.

```
progressView.setProgressBackgroundColor(Color.parseColor("#FAFAFA"));
```

Set the background color of indicator.

### Set threshold: void Adapter.setThreshold(int threshold);

### For GridLayoutManager:

    GridLayoutManager gridLayoutManager = new GridLayoutManager(context,SPAN_SIZE);
    gridLayoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup(SPAN_SIZE));
    recyclerView.setLayoutManager(gridLayoutManager);

Please refer to GridFragment and GridAdapter in the demo.

## Loading View / Empty View / Error View

### XML prepare (Please refer to Import - Layout XML)

- Place the LoadingView in parallel with the SwipeRefreshLayout element.

- Place the ErrorView / EmptyView in parallel with the SwipeRefreshLayout element. Set the visibility of ErrorView / EmptyView to "gone".

### Java Code

    /**
     * Call this method to set the LoadingView before calling the setAdapter or notify* methods.
     * LoadingView will automatically hide when the setAdapter or notify* methods is called.
     * @param loadingView LoadingView got by findViewById.
     */
    void SimpleRecyclerView.setLoadingView(View loadingView);   //Call this method to set the LoadingView before calling the setAdapter or notify* methods.
    //Example:
    recyclerView.setLoadingView(findViewById(R.id.loading_view));   //Set custom LoadingView layout.

    View SimpleRecyclerView.hideLoadingView();                  //Manually hide LoadingView. Generally needn't.
    View SimpleRecyclerView.showLoadingView();                  //Manually show LoadingView. Generally needn't.

    recyclerView.setEmptyView(findViewById(R.id.empty_view)); 	//Set EmptyView
    recyclerView.showErrorView(findViewById(R.id.error_view));	//Set and show ErrorView
    recyclerView.hideErrorView();								//Hide ErrorView

## OnItemClickListener/OnItemLongClickListener

    adapter.setOnItemClickListener(new OnItemClickListener());
	adapter.setOnItemLongClickListener(new OnItemLongClickListener());

To implement ripple effect on item clicks, add the code below to the root element of the item layout XML:

```
android:foreground="?android:attr/selectableItemBackground"(for CardView, SimpleDraweeView)
android:background="?android:attr/selectableItemBackground"(for general View)
```

For CardView, add the code above to the element of CardView, instead of adding to the root element.

## Divider

Instantiate Divider:

```
public Divider(
  @Px int width,           //The width of the divider
  @ColorInt int color,     //The color of the divider
  boolean isHorizontalList,   //Whether the LinearLayoutManager is horizontal
  @Px int leftOffset, @Px int topOffset, @Px int rightOffset, @Px int bottomOffset);
```

- leftOffset is the length of the blank area on the left of the divider (divider will not draw in blank area).

- topOffset / rightOffset / bottomOffset are the same.

Usage：

    mRecyclerView.addItemDecoration(Divider divider);

## Animation when initing RecyclerView, adding / modifying / deleting items

The Adapter / SingleViewTypeAdapter encapsulates common methods for manipulating data sets. Using these methods, you will get animation effects and the correct loading state settings.

Adapter:

- void onAdded();
- void onAdded(int position);
- void onAddedAll(int newDataSize);
- void onAddedAll(int position, int newDataSize);
- void onListSet();
- void onRemovedLast(); / void onRemoved();
- void onRemoved(int position);
- void onRemoveAll(int positionStart, int itemCount);
- void onSet(int position);
- void onSetAll(int positionStart, int itemCount);

When you are using Adapter, you should update your data set first, then call the above methods.

SingleViewTypeAdapter :

- void setList(List<${JavaBean}> list);
- void add(${JavaBean} javaBean);
- void add(int position, ${JavaBean} javaBean);
- void remove(int position);
- void removeLast(); / void remove();
- void removeAll(int positionStart, int itemCount);
- void set(int position,${JavaBean} javaBean);
- void setAll(int positionStart, int itemCount, ${JavaBean} javaBean);
- void addAll(int position, List<${JavaBean}> newList);
- void addAll(List<${JavaBean}> newList);

When you are using SingleViewTypeAdapter, you should NOT update your data set, instead call the above methods directly.

You only need to call the methods above, SingleViewTypeAdapter will update the underlying data set automatically.

If the required method of operation on the data set is not listed above, you can operate on the data set List[${JavaBean}] first, then call adapter.notifyItem* mmethods to refresh UI, finally call setLoadingFalse() to restore the status of not loading more.

## Sticky headers

1.Make your Adapter class implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> interface;

2.Create your Header ViewHolder;

3.Implement 3 methods in the interface:

- long getHeaderId(int position);

This method determines which header the item in ${position} is displayed under. One header corresponds to one headerId, so, For items that want to be displayed under the same header, this method should return the same headerId for the items' positions. The number of headerIds the method possibility returns is the number of fixed headers.

You can call this method to get the headerId for the current adapterPosition in onViewHolderBind and onBindHeaderViewHolder.

- RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

- void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int adapterPosition);

Example:

```
@Override
public long getHeaderId(int position) {
    //This example takes the elements 0-9 as a group, the 10-19 elements as a group, and so on, with every 10 elements belonging to the same group.
    //For actual use, you can base on position and the data got from List.get(position) to judge the fields inside, to decide which elements belong to which groups.
    return position / 10;
}

@Override
public HeaderVH onCreateHeaderViewHolder(ViewGroup parent) {
    return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
}

@Override
public void onBindHeaderViewHolder(HeaderVH holder, int position) {
    holder.tvHeader.setText("Group " + getHeaderId(position) /* The group the current header is in. */ +
        ": Adapter Position " + String.valueOf(position + 1) + " - " + String.valueOf(position + 10));
}
```

Please refer to PinnedFragment and PinnedAdapter in the demo.

## Group display

Type Group[Title, ChildItem] is required to represent a group. Each group contains one title and a number of subitems.

```
<Title, ChildItem> Group<Title, ChildItem> {
    Title title;  //Title of one group
    List<ChildItem> childItemList;  //Subitem list under one group
}
```

Therefore, you need to convert the data to List[Group[Title, ChildItem]], that is, the list of groups.

You need to subclass GroupAdapter, and override the following 4 methods:

```
ViewHolder onTitleVHCreate(ViewGroup parent);

ViewHolder onChildItemVHCreate(ViewGroup parent);

/**
* @param adapterPos The absolute position of the Title in the Adapter ( = holder.getAdapterPosition()).
* @param titleOrderInAllTitles The relative position of the Group of the current Title in List[Group].
*/
void onTitleVHBind(ViewHolder holder, int adapterPos, Title title, int titleOrderInAllTitles);

/**
* @param adapterPos The absolute position of the ChildItem in the Adapter ( = holder.getAdapterPosition()).
* @param titleOrderInAllTitles The relative position of the Group of the current ChildItem in List[Group].
* @param childOrderInCurrentGroup The relative position of the current ChildItem in the Group.
* (The childOrder of the first ChildItem is 0, that is, the childOrder does not include the position of the Title.)
*/
void onChildItemVHBind(ViewHolder holder, int adapterPos, Title title,
    int titleOrderInAllTitles, ChildItem childItem, int childOrderInCurrentGroup);
```

The data List[Group] is passed in through the GroupAdapter.setList(List[Group] groupList) method.

If you know the absolute position of the Title or ChildItem in the Adapter, and you need to get the Title object, the relative position of the Group of the current Title in List[Group], the ChildItem object and the relative position of the current ChildItem in the Group, You can use 2 methods below on the GroupAdapter:

```
/**
* According to the absolute position of the Title in the Adapter,
* get the Title object and the relative position of the Group of the current Title in List[Group].
* @param positionInRV_viewType_title The absolute position of the Title in the Adapter，
* @return TitleChildItemBean {Title title;  int titleOrder;}
*/
TitleChildItemBean<Title, Void> getTitleWithOrder(int positionInRV_viewType_title)

/**
* According to the absolute position of the ChildItem in the Adapter,
* get the Title object, the relative position of the Group of the current Title in List[Group],
* the ChildItem object and the relative position of the current ChildItem in the Group.
* @param positionInRV_viewType_childItem The absolute position of the ChildItem in the Adapter.
* @return TitleChildItemBean {Title title;  int titleOrder;
    ChildItem childItem;  int childOrder;}
*/
TitleChildItemBean<Title, ChildItem> getTitleAndChildItem(int positionInRV_viewType_childItem)
```

Set OnGroupItemClickListener and OnGroupItemLongClickListener:

```
GroupAdapter.setOnGroupItemClickListener(OnGroupItemClickListener l);

void onGroupItemClick(ViewHolder holder, View v, int adapterPos, int viewType,
   Title title, int titleOrder, ChildItem childItem, int childOrder);

GroupAdapter.setOnGroupItemLongClickListener(OnGroupItemLongClickListener l);

boolean onGroupItemLongClick(ViewHolder holder, View v, int adapterPos, int viewType,
   Title title, int titleOrder, ChildItem childItem, int childOrder);
```

As with the SingleViewTypeAdapter, the GroupAdapter provides a set of methods for easily manipulating data sets held by the Adapter:

```
- void setList(List<Group<Title, ChildItem>> groupList);
- void add(Group<Title, ChildItem> group);
- void add(int position, Group<Title, ChildItem> group);
- void remove(int position);
- void removeLast(); / void remove();
- void removeAll(int positionStart, int itemCount);
- void set(int position, Group<Title, ChildItem> group);
- void setAll(int positionStart, int itemCount, Group group);
- void addAll(int position, List<Group> newGroupList);
- void addAll(List<Group<Title, ChildItem>> newGroupList);
```

Please refer to the codes and comments of GroupAdapter. There is also an example usage in GroupFragment and GroupRVAdapter of the demo.
