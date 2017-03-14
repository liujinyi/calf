package com.calf.mode;

import android.support.v4.util.ArrayMap;

import com.calf.frame.log.Logger;
import com.calf.frame.tool.Assert;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 16-11-13.
 */

public final class ModeMgr {

    private static ArrayMap<Class<? extends Mode>, Mode> mModes = new ArrayMap<>();

    static {

    }

    public static <T extends Mode> T getMode(Class<T> cls) {
        return createManager(cls);
    }

    private static <T extends Mode> T createManager(Class<T> cls) {
        T mode = null;
        if (mModes.containsKey(cls)) {
            mode = (T) mModes.get(cls);
        } else {
            try {
                mode = cls.newInstance();
                mode.init();
                mModes.put(cls, mode);
            } catch (Exception e) {
                Assert.classAssert(false, e.getMessage());
                Logger.printStackTrace(e);
            }
        }
        return mode;
    }


}
