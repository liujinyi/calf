package com.calf.mode;

import android.os.RemoteException;

import com.calf.bean.MusicInfo;
import com.calf.frame.log.Logger;
import com.calf.player.activitys.MainActivity;
import com.calf.player.services.IPlaybackService;

/**
 * Created by JinYi Liu on 17-3-14.
 */

public class PlaybackMgr implements Mode {

    private IPlaybackService mBinder;

    @Override
    public void init() {
        mBinder = MainActivity.mBinder;
    }

    @Override
    public void release() {

    }

    public void play(MusicInfo musicInfo) {
        try {
            mBinder.play(musicInfo);
        } catch (RemoteException e) {
            Logger.printStackTrace(e);
        }
    }

}
