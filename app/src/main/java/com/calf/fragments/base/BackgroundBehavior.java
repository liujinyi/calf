package com.calf.fragments.base;

import android.os.Bundle;

import com.calf.frame.log.Logger;

/**
 * Created by JinYi Liu on 16-12-3.
 */

public abstract class BackgroundBehavior<T> extends BaseFragment.Behavior {

    @Override
    protected void doInBackground(final Bundle savedInstanceState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getCallback().onState(BaseFragment.State.LOADING, "start onBackgroundLoading");
                    getCallback().onSuccess(onBackgroundLoading(), savedInstanceState);
                } catch (Exception e) {
                    Logger.printStackTrace(e);
                    getCallback().onState(BaseFragment.State.FAILURE, e.getMessage());
                }
            }
        }).start();
    }

    protected abstract T onBackgroundLoading() throws Exception;

    private static class BackgroundTask implements Runnable {

        @Override
        public void run() {

        }

    }


}
