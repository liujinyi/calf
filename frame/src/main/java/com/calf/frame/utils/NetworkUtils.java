package com.calf.frame.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JinYi Liu on 16-11-30.
 */

public class NetworkUtils {

    public static boolean isAvailable(Context context) {
        boolean flag = false;
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isAvailable()) {
            flag = true;
        }
        return flag;
    }

    public static boolean isConnected(Context context) {
        boolean flag = false;
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isAvailable() && info.isConnected()) {
            flag = true;
        }
        return flag;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        Object manager = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return ((ConnectivityManager) manager).getActiveNetworkInfo();
    }

}
