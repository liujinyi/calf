package com.calf.player;

import android.app.Application;

import com.calf.frame.FrameCore;
import com.calf.player.manager.FolderManager;

/**
 * Created by JinYi Liu on 16-7-26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FrameCore.init();
        FolderManager.init(this);
    }
}
