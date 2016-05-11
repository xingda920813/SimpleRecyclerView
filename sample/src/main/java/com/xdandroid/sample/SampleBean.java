package com.xdandroid.sample;

import java.io.Serializable;

/**
 * Created by XingDa on 2016/5/11.
 */
public class SampleBean implements Serializable {

    public SampleBean() {

    }

    public SampleBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
