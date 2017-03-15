package com.calf.player.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;

import com.calf.bean.MusicInfo;
import com.calf.frame.http.HttpSession;
import com.calf.frame.log.Logger;
import com.calf.utils.UrlFactory;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by JinYi Liu on 16-11-13.
 */

public class PlaybackService extends Service{

    private static final String TAG = "PlaybackService";

    private String mSimpleName;
    private MediaPlayer mMediaPlayer;
    private IPlayDelegate mPlayDelegate;

    public static IPlaybackService.Stub mBinder;

    @Override
    public void onCreate() {
        this.mSimpleName = getClass().getSimpleName();
        this.mMediaPlayer = new MediaPlayer();
        this.mBinder = new IPlaybackService.Stub() {

            @Override
            public void play(final MusicInfo musicInfo) throws RemoteException {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpSession session = new HttpSession();
                        Response response = session.get(UrlFactory.createConvertUrl(musicInfo.getId()));
                        if (response != null && response.isSuccessful()) {
                            try {
                                final String data = response.body().string();
                                String url = data.split("\r\n")[2].substring(4);
                                mMediaPlayer.reset();
                                mMediaPlayer.setDataSource(url);
                                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mMediaPlayer.prepare();
                                mMediaPlayer.start();
                                mPlayDelegate.onPlay();
                            } catch (Exception e) {
                                Logger.printStackTrace(e);
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void setPlayDelegate(IPlayDelegate delegate) throws RemoteException {
                PlaybackService.this.mPlayDelegate = delegate;
            }

        };
        Logger.i(TAG, mSimpleName + " [onCreate]");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i(TAG, mSimpleName + " [onStartCommand]");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i(TAG, mSimpleName + " [onBind]");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.i(TAG, mSimpleName + " [onBind]");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Logger.i(TAG, mSimpleName + " [onDestroy]");
    }

}
