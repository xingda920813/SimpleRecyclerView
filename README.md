# SimpleRecyclerView
###对RecyclerView和SwipeRefreshLayout的简单封装。固定Header部分使用了timehop/sticky-headers-recyclerview。

[https://github.com/timehop/sticky-headers-recyclerview](https://github.com/timehop/sticky-headers-recyclerview "timehop/sticky-headers-recyclerview")

![Alt text](https://raw.githubusercontent.com/xingda920813/SimpleRecyclerView/master/video.gif)

![Alt text](https://raw.githubusercontent.com/xingda920813/SimpleRecyclerView/master/videomaterial.gif)

![Alt text](https://raw.githubusercontent.com/xingda920813/SimpleRecyclerView/master/videoloading.gif)

###主要特性：
#### 1. 下拉刷新：
对SwipeRefreshLayout的封装。相对于原生的SwipeRefreshLayout，解决了2个问题：

- 在某些版本的android.support库中，SwipeRefreshLayout与AppbarLayout滑动冲突。向下拉RecyclerView时，会立即出现下拉刷新，而不能将列表往下拉。

- 不能在onCreate方法里调用setRefreshing(true)显示加载的转圈动画。

#### 2.加载更多

- 距离底部还有一定数量(THRESHOLD)的item时，自动加载更多的数据(THRESHOLD值可设置)

- 若滑到最底部，加载仍未完成，则显示加载的转圈动画

- 除了LinearLayoutManager，同样支持GridLayoutManager的加载更多动画

- 转圈SwipeRefreshLayout样式（仿知乎）和ProgressBar样式双样式可选（第一张图为ProgressBar样式，第二张图为SwipeRefreshLayout样式。SwipeRefreshLayout 样式与系统版本无关，可自定义颜色；ProgressBar样式因系统版本而异，仅在 API 21 以上的 Android 系统中具有 Material Design 风格。）

#### 3.加载中(Loading View)/空数据(Empty View)/错误页面(Error View)的显示
#### 4.onItemClickListener/onItemLongClickListener
#### 5.上下滑动时的固定Header

- 可设置任意数量的固定Header种类

#### 6.item分割线支持

- 可自定义分割线的Drawable，在Drawable XML里，可自定义粗细、颜色等参数

- 可自定义分割线的左侧起始点与RecyclerView左侧的距离等，共可自定义上、下、左、右4个offset

- 横向/纵向LinearLayoutManager均支持

#### 7.Item 动画

- 初始化RecyclerView、添加/修改/删除条目时具有Material Design动画

## 引入
### 1.添加二进制

build.gradle中添加

    compile 'com.xdandroid:simplerecyclerview:+'
	compile 'com.xdandroid:materialprogressview:+'
	compile 'com.android.support:recyclerview-v7:${latest.version}'
	compile 'com.android.support:design:${latest.version}'

### 2.基本用法与原生RecyclerView和SwipeRefreshLayout相同

- Simple* 类直接继承自相应的原生控件

- 用法可参考Demo

- 有多种viewType（Adapter继承com.xdandroid.simplerecyclerview.Adapter）时，为Adapter设置数据源的方法与原生RecyclerView.Adapter相同，可通过构造方法传入，也可在Adapter里建立自己的设置数据的方法，在方法里面为数据对象赋值并刷新UI。

- 只有1种viewType（Adapter继承SingleViewTypeAdapter<${JavaBean}>）时，为Adapter设置数据源list的方法为：

.

    recyclerView.setAdapter(adapter);				//先把前面构建好的adapter对象设置给RecyclerView
    void Adapter.setList(List<${JavaBean}> list);	//再调用setList设置数据

### 3.布局文件

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
    
### 4. 建立Adapter抽象类

#### 4.1 只有1种viewType时，继承SingleViewTypeAdapter<${JavaBean}>

- 重写onViewHolderCreate，对应于RecyclerView.Adapter中的onCreateViewHolder

- 重写onViewHolderBind，对应于RecyclerView.Adapter中的onBindViewHolder

- 建立ViewHolder

- 使用GridLayoutManager时，重写int getItemSpanSize(int position, int viewType, int spanCount)

- 不要重写onLoadMore和hasMoreElements，把他们交由Activity/Fragment在实例化Adapter时实现。

#### 4.2 有多种viewType时，继承Adapter

- 重写onViewHolderCreate，对应于RecyclerView.Adapter中的onCreateViewHolder

- 重写onViewHolderBind，对应于RecyclerView.Adapter中的onBindViewHolder

- 重写getViewType，对应于RecyclerView.Adapter中的getItemViewType

- 重写getCount，对应于RecyclerView.Adapter中的getItemCount

- 建立各个viewType对应的ViewHolder类

- 使用GridLayoutManager时，重写int getItemSpanSize(int position, int viewType, int spanCount)

- 不要重写onLoadMore和hasMoreElements，把他们交由Activity/Fragment在实例化Adapter时实现。

## 下拉刷新

解决与AppbarLayout的滑动冲突：为AppBarLayout指定id为appbar即可(android:id="@+id/appbar")

若要自动为Toolbar留出高度而不被Toolbar挡住，需要为CoordinatorLayout下的Layout或SwipeRefreshLayout添加 app:layout_behavior="@string/appbar_scrolling_view_behavior"

## 加载更多
### 实例化Adapter时要实现2个方法 : 

#### 1. void onLoadMore(Void please\_Make\_Your\_Adapter\_Class\_As\_Abstract\_Class) :

先将自己维护的pageIndex变量自增1，去API获取到更多的数据之后，只需调用adapter对象的void addAll(List<${JavaBean}>)一个方法即可。

注意不要调用数据集list的addAll方法，然后再手动刷新UI。这样做会丧失转圈消失时和项目添加时的动画效果，也使得加载状态得不到重置。

#### 2. boolean hasMoreElements(Void let\_Activity\_Or\_Fragment\_Implement\_These\_Methods) : 

告知Adapter是否还有更多的数据需要加载，只是这一批没加载完。

有需要时，可调用void Adapter.setLoadingFalse()恢复非加载更多时的状态。

### 样式选择：

	adapter.setUseMaterialProgress(true, new int[]{getResources().getColor(R.color.colorAccent)});

第一个参数(boolean useMaterialProgress)为false时，使用ProgressBar样式，为true时，使用SwipeRefreshLayout样式。第二个参数(int[] colors)仅在useMaterialProgress为true时有效，可设置SwipeRefreshLayout样式加载转圈的颜色。若int[]中的颜色值多于一个，将按顺序轮换显示不同颜色的转圈，每转一圈换一种颜色。

	adapter.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorAccent)});

随时改变转圈的颜色。

### 设置Threshold : void Adapter.setThreshold(int threshold);

#### 不需要加载更多功能时 , 可重写getItemSpanSize方法，使其始终返回-1.

### 对于GridLayoutManager : 

    GridLayoutManager gridLayoutManager = new GridLayoutManager(context,SPAN_SIZE);
    gridLayoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup(SPAN_SIZE));
    recyclerView.setLayoutManager(gridLayoutManager);

## 加载中/空数据/加载错误页面
### XML准备(详见引入-布局文件)

- 将LoadingView与SwipeRefreshLayout元素并列。

- 将ErrorView或EmptyView与SwipeRefreshLayout元素并列，visibility设置为gone。

### Java代码

    /**
     * 在调用setAdapter和notify*系列方法之前调用此方法来设置LoadingView。
     * LoadingView会在setAdapter和notify*系列方法调用时自动隐藏。
     * @param loadingView 通过findViewById找到的LoadingView.
     */
    void SimpleRecyclerView.setLoadingView(View loadingView);   //在调用setAdapter和notify*系列方法之前调用此方法
    //示例 : 
    recyclerView.setLoadingView(findViewById(R.id.loading_view));   //设置自定义LoadingView布局

    View SimpleRecyclerView.hideLoadingView();                  //手动控制LoadingView的隐藏，一般情况下无需调用此方法
    View SimpleRecyclerView.showLoadingView();                  //手动控制LoadingView的显示，一般情况下无需调用此方法

    recyclerView.setEmptyView(findViewById(R.id.empty_view)); 	//设置空数据View
    recyclerView.showErrorView(findViewById(R.id.error_view));	//设置并显示错误View
    recyclerView.hideErrorView();								//隐藏错误View

## onItemClickListener/OnItemLongClickListener

    adapter.setOnItemClickLitener(new OnItemClickLitener());
	adapter.setOnItemLongClickLitener(new OnItemLongClickLitener());

## 分割线

构建Divider : 

Divider(Context context, @Nullable @DrawableResId Integer dividerDrawableResId, boolean isHorizontal, int leftOffset, int topOffset, int rightOffset, int bottomOffset);

- 可自定义分割线的Drawable，在Drawable XML里，可自定义粗细、颜色等参数，然后将Drawable的资源ID传入构造方法的第二个参数即可使用自定义的分割线Drawable，传入null将使用默认分割线样式

rv_divider.xml为一般的line形状XML，示例：

    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android"
    	android:shape="line" >
    	<stroke
    		android:width="0.5dp"
    		android:color="#bdbdbd"/>
    	<size android:height="1dp"/>
    </shape>

- 横向列表需设置isHorizontal为true

- leftOffset为分割线的左侧起始点与RecyclerView左侧的距离，topOffset/rightOffset/bottomOffset同理，适用于条目左侧头像部分下面不画分割线的需求

使用：

    recyclerView.addItemDecoration(new Divider(this, R.drawable.rv_divider, false, 0, 0, 0, 0));
	recyclerView.addItemDecoration(new Divider(this, null, false, 0, 0, 0, 0));

## 初始化RecyclerView、添加/修改/删除Item时的动画

Adapter/SingleViewTypeAdapter封装了对数据集操作的常用方法，使用这些方法，将获得动画效果和正确的加载状态设置。

Adapter : 

- void onAdded();
- void onAddedAll(int newDataSize);
- void onRemovedLast(); / void onRemoved();

先对数据集进行增删操作，再调用上面的方法。

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

若所需的对数据集操作的方法没有在上面列出，可直接对数据集合List<${JavaBean}>进行操作后，调用adapter对象的notifyItem* 系列方法刷新UI，并调用void setLoadingFalse()恢复非加载更多时的状态。

## 上下滑动时的固定Header
Adapter类实现StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>接口，创建自己的Header ViewHolder，重写接口里的3个方法：

- long getHeaderId(int position);	//根据position返回headerId，返回headerId的数量就是固定Header的种类数

- RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

- void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

示例：

    @Override
    public long getHeaderId(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.header_chart, parent, false);
        HeaderVH headerVH = new HeaderVH(view);
        headerVH.ll_header_chart = (LinearLayout) view.findViewById(R.id.ll_header_chart);
        headerVH.iv_header_chart = (ImageView) view.findViewById(R.id.iv_header_chart);
        AutoUtils.auto(view);
        return headerVH;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((HeaderVH) holder).ll_header_chart.setVisibility(View.GONE);
            ((HeaderVH) holder).iv_header_chart.setVisibility(View.GONE);
        } else {
            ((HeaderVH) holder).ll_header_chart.setVisibility(View.VISIBLE);
            ((HeaderVH) holder).iv_header_chart.setVisibility(View.VISIBLE);
        }
    }

    public class HeaderVH extends RecyclerView.ViewHolder {
        public HeaderVH(View itemView) {
            super(itemView);
        }
        LinearLayout ll_header_chart;
        ImageView iv_header_chart;
    }

