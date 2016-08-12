package com.xdandroid.sample.bean;

import java.io.*;

/**
 * Created by XingDa on 2016/5/11.
 */
public class SampleBean implements Serializable {

    public SampleBean(int type, String title, String content, String message, int imageResId) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.message = message;
        this.imageResId = imageResId;
    }

    //Banner类型
    public static final int TYPE_BANNER = 100;
    //文本类型
    public static final int TYPE_TEXT = 200;

    //条目类型的标记位
    public int type;

    //文本类型条目使用的2个字段
    public String title;
    public String content;

    //Banner类型条目使用的2个字段
    public String message;
    public int imageResId;
}
