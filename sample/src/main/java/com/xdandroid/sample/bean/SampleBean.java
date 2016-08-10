package com.xdandroid.sample.bean;

import java.io.*;

/**
 * Created by XingDa on 2016/5/11.
 */
public class SampleBean implements Serializable {

    public SampleBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String title;
    public String content;
}
