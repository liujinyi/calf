package com.calf.mode;

import com.calf.player.activitys.MainActivity;

/**
 * Created by JinYi Liu on 16-11-13.
 */

public interface Mode {

    public void release();

    public void init(MainActivity activity);

}
