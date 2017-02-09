package com.calf.utils;

import android.util.Base64;

/**
 * Created by JinYi Liu on 16-12-18.
 */

public class UrlFactory {

    private static final String HOST = "http://mobi.kuwo.cn/mobi.s?f=kuwo&q=";

    private static StringBuilder createCommonParams() {
        StringBuilder sb = new StringBuilder();
        sb.append("user=869336028818969&p2p=1&uid=77479930&vipsec=1&vipver=8.2.7.0");
        sb.append("&prod=kwplayer_ar_8.2.7.0&corp=kuwo&source=kwplayer_ar_8.2.7.0_kwdebug.apk");
        return sb;
    }

    public static String createAlbumMusic(int id, int start, int count) {
        StringBuilder sb = createCommonParams();
        sb.append("&id=").append(id);
        sb.append("&start=").append(start);
        sb.append("&count=").append(count);
        sb.append("&type=music_list&key=album&hasmv=1&hasinner=1");
        return HOST + encryptParams(sb.toString());
    }

    private static String encryptParams(String str) {
        return encryptParams(str.getBytes());
    }

    private static String encryptParams(byte[] bytes) {
        byte[] encryptBytes = DES.encrypt(bytes);
        return new String(Base64.encode(encryptBytes, encryptBytes.length));
    }

}
