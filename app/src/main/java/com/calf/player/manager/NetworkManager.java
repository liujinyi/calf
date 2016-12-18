package com.calf.player.manager;

import android.content.Context;

import com.calf.frame.utils.NetworkUtils;

/**
 * Created by JinYi Liu on 16-12-18.
 */

public class NetworkManager {

    private static Context sContext;

    public static void init(Context context) {
        NetworkManager.sContext = context;
    }

    public static boolean isAvailable() {
        return NetworkUtils.isAvailable(sContext);
    }

    public static boolean isConnected() {
        return NetworkUtils.isConnected(sContext);
    }

}
