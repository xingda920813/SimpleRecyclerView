package com.xdandroid.simplerecyclerview;

import java.io.*;

/**
 * Created by xingda on 16-8-12.
 */

public class TitleChildItemBean<Title, ChildItem> implements Serializable {

    public TitleChildItemBean(Title title, int titleOrder, ChildItem childItem, int childOrder) {
        this.title = title;
        this.titleOrder = titleOrder;
        this.childItem = childItem;
        this.childOrder = childOrder;
    }

    public Title title;
    public int titleOrder;
    public ChildItem childItem;
    public int childOrder;
}
