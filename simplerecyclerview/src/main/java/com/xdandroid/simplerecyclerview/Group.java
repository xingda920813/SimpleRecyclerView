package com.xdandroid.simplerecyclerview;

import java.io.*;
import java.util.*;

public class Group<Title, ChildItem> implements Serializable {

    public Group(Title title, List<ChildItem> childItemList) {
        this.title = title;
        this.childItemList = childItemList;
    }

    public Title title;
    public List<ChildItem> childItemList;
}
