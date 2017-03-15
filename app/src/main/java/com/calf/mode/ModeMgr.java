package com.calf.mode;

import android.support.v4.util.ArrayMap;

import com.calf.frame.log.Logger;
import com.calf.frame.tool.Assert;
import com.calf.player.activitys.MainActivity;

/**
 * Created by JinYi Liu on 16-11-13.
 */

public final class ModeMgr {

    private static MainActivity sActivity;
    private static ArrayMap<Class<? extends Mode>, Mode> sModes = new ArrayMap<>();

    public static void init(MainActivity activity) {
        sActivity = activity;
    }

    public static PlaybackMgr getPlaybackMgr(){
        return loadMode(PlaybackMgr.class);
    }

    public static <T extends Mode> T loadMode(Class<T> cls) {
        return getMode(cls);
    }

    private static <T extends Mode> T getMode(Class<T> cls) {
        T mode = null;
        if (sModes.containsKey(cls)) {
            mode = (T) sModes.get(cls);
        } else {
            try {
                mode = cls.newInstance();
                mode.init(sActivity);
                sModes.put(cls, mode);
            } catch (Exception e) {
                Assert.classAssert(false, e.getMessage());
                Logger.printStackTrace(e);
            }
        }
        return mode;
    }


}
