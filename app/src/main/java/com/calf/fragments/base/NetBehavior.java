package com.calf.fragments.base;

import android.os.Bundle;

/**
 * Created by JinYi Liu on 16-12-3.
 */

public abstract class NetBehavior<T> extends BaseFragment.Behavior {

    private int mCacheMinutes;
    private String mCachePath;
    private Decoder mDecoder;

    @Override
    protected void doInBackground(Bundle savedInstanceState) {

    }

    protected abstract String giveMeUrl(int start, int count);

    protected abstract T onBackgroundParser(String data);

    protected interface Decoder {
        byte[] decode(byte[] bytes);
    }

}
