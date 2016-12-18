package com.calf.player;

import android.content.Context;

import com.calf.frame.FrameCore;
import com.calf.player.activitys.MainActivity;
import com.calf.player.manager.FolderManager;
import com.calf.player.manager.MainFragmentManager;
import com.calf.player.manager.NetworkManager;

/**
 * Created by JinYi Liu on 16-12-18.
 */

public class Init {

    public static void initInApplication(Context context) {
        FrameCore.init();
        FolderManager.init(context);
        NetworkManager.init(context);
    }

    public static void initInMainActivity(MainActivity activity) {
        MainFragmentManager.init(activity);
    }

}
