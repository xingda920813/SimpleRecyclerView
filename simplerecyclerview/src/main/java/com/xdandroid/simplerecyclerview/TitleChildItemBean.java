package com.xdandroid.simplerecyclerview;

import java.io.*;

/**
 * Created by xingda on 16-8-12.
 */

public class TitleChildItemBean<Title, ChildItem> implements Serializable {
    public TitleChildItemBean(Title title, ChildItem childItem) {
        this.title = title;
        this.childItem = childItem;
    }
    public Title title;
    public ChildItem childItem;
}
