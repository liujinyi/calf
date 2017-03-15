package com.calf.mode;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.calf.bean.MusicInfo;
import com.calf.frame.log.Logger;
import com.calf.player.activitys.MainActivity;
import com.calf.player.services.IPlayDelegate;
import com.calf.player.services.IPlaybackService;
import com.calf.player.services.PlaybackService;

/**
 * Created by JinYi Liu on 17-3-14.
 */

public class PlaybackMgr implements Mode {

    private boolean mInitSuccess;
    private IPlaybackService mBinder;

    private IPlayDelegate mDelegate = new IPlayDelegate.Stub() {
        @Override
        public void onPlay() {
            Logger.e("calf", "PlaybackMgr IPlayDelegate [onPlay]");
        }
    };

    @Override
    public void init(final MainActivity activity) {
        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Logger.e("swallow", "PlaybackMgr ServiceConnection [onServiceConnected]");
                mBinder = (IPlaybackService) IPlaybackService.Stub.asInterface(service);
                try {
                    mBinder.setPlayDelegate(mDelegate);
                    mInitSuccess = true;
                } catch (RemoteException e) {
                    Logger.printStackTrace(e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Logger.e("swallow", "PlaybackMgr ServiceConnection [onServiceDisconnected]");
                mInitSuccess = false;
            }
        };
        Intent intent = new Intent(activity, PlaybackService.class);
        activity.bindService(intent, conn, Activity.BIND_AUTO_CREATE);
        activity.startService(intent);
    }

    @Override
    public void release() {

    }

    public boolean isReady() {
        return mInitSuccess;
    }

    public void play(MusicInfo musicInfo) {
        try {
            mBinder.play(musicInfo);
        } catch (RemoteException e) {
            Logger.printStackTrace(e);
        }
    }

}
