package com.calf.utils;

/**
 * Created by JinYi Liu on 16-12-18.
 */

public class UrlFactory {

    public static String createLibraryMainUrl(int num) {
        StringBuilder sb = new StringBuilder("http://rcm.kuwo.cn/rec.s?cmd=rcm_feed");
        sb.append("&uid=").append(0);
        sb.append("&num=").append(num);
        sb.append("&platform=ar");
        sb.append("&devid=").append("75194338");
        sb.append("&prod=").append("kwplayer_ar_8.2.7.0");
        sb.append("&corp=kuwo&flush=1&nettype=WIFI");
        sb.append("&source=").append("kwplayer_ar_8.2.7.0_kwdebug.apk");
        return sb.toString();
    }

}
