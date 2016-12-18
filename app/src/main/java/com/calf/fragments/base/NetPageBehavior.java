package com.calf.fragments.base;

import android.os.Bundle;
import android.text.TextUtils;

import com.calf.frame.log.Logger;

import okhttp3.Response;

/**
 * Created by JinYi Liu on 16-12-3.
 */

public abstract class NetPageBehavior<T> extends BaseFragment.Behavior {

    private CacheParameter mParameter;
    private BaseFragment.BaseTask mPageTask;
    private HttpPageStatistics mHttpPageStatistics;

    public NetPageBehavior() {
        this.mParameter = new CacheParameter();
    }

    protected abstract T onBackgroundParser(String data);

    protected abstract String giveMeUrl(int start, int count);

    @Override
    protected void doInBackground(Bundle savedInstanceState) {
        String url = giveMeUrl(0, 30);
        BaseFragment.Callback callback = getCallback();
        if (TextUtils.isEmpty(url)) {
            callback.onState(BaseFragment.State.FAILURE, "NetPageBehavior [giveMeUrl] isEmpty");
            return;
        }
        if (mPageTask != null && !mPageTask.isCancelOrDie()) {
            Logger.e(BaseFragment.TAG, "NetPageBehavior [doInBackground] mPageTask is loading");
        } else {
            mPageTask = null;
            mPageTask = createPageTask(savedInstanceState, url);
            new Thread(mPageTask).start();
        }
    }

    protected abstract BaseFragment.BaseTask createPageTask(Bundle savedInstanceState, String url);

    public CacheParameter getParameter() {
        return mParameter;
    }

    public void setHttpPageStatistics(HttpPageStatistics mHttpPageStatistics) {
        this.mHttpPageStatistics = mHttpPageStatistics;
    }

    public HttpPageStatistics getHttpPageStatistics() {
        return mHttpPageStatistics;
    }

    public void setCachePath(String path) {
        this.mParameter.setCachePath(path);
    }

    public void setCacheMillis(long millis) {
        this.mParameter.setCacheMillis(millis);
    }

    public void setDecoder(BaseFragment.Decoder decoder) {
        this.mParameter.setDecoder(decoder);
    }

    public static class CacheParameter {
        private long mCacheMillis;
        private String mCachePath;
        private BaseFragment.Decoder mDecoder;

        public long getCacheMillis() {
            return mCacheMillis;
        }

        public void setCacheMillis(long millis) {
            this.mCacheMillis = millis;
        }

        public String getCachePath() {
            return TextUtils.isEmpty(mCachePath) ? "" : mCachePath;
        }

        public void setCachePath(String path) {
            this.mCachePath = path;
        }

        public BaseFragment.Decoder getDecoder() {
            return mDecoder;
        }

        public void setDecoder(BaseFragment.Decoder decoder) {
            this.mDecoder = decoder;
        }
    }

    public interface HttpPageStatistics {
        public void onHttpRequestStart(String url);

        public void onHttpRequestFailure(String url, long cost, Response response);

        public void onHttpRequestSuccess(String url, long cost, Response response);
    }

}
