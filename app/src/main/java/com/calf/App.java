package com.calf;

import android.app.Application;

import com.calf.frame.FrameCore;
import com.calf.frame.tool.Assert;
import com.calf.frame.log.Logger;
import com.calf.frame.message.MessageManager;

/**
 * Created by JinYi Liu on 16-7-26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FrameCore.init();
    }
}
