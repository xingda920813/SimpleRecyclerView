package com.xdandroid.sample.util;

import com.xdandroid.sample.*;

public class BannerProvider {

    public static String getMessage(int position) {
        switch (position % 4) {
            case 0:
                return "小米MAX超耐久直播";
            case 1:
                return "嘘——看我发现了什么";
            case 2:
                return "母亲，您辛苦了";
            case 3:
                return "哭过、笑过、真爱过。";
        }
        return null;
    }

    public static int getImageResId(int position) {
        switch (position % 4) {
            case 0:
                return R.mipmap.img1;
            case 1:
                return R.mipmap.img2;
            case 2:
                return R.mipmap.img3;
            case 3:
                return R.mipmap.img4;
        }
        return 0;
    }
}
