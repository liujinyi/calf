package com.calf.fragments.base;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by JinYi Liu on 16-12-4.
 */

public class OnlineTask implements Runnable {

    private String mUrl;
    private int mCacheMinutes;
    private String mCachePath;
    private final AtomicBoolean mAlive;
    private BaseFragment.Decoder mDecoder;

    public void cancel() {
        mAlive.set(false);
    }

    public boolean isAlive() {
        return mAlive.get();
    }

    public OnlineTask(String url) {
        this.mUrl = url;
        this.mAlive = new AtomicBoolean();
        this.mAlive.set(true);
    }

    @Override
    public void run() {

    }

}
