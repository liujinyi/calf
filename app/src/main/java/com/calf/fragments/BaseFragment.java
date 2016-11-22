package com.calf.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.calf.frame.log.Logger;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public abstract class BaseFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    private static final String TAG = "BaseFragment";

    private String mSimpleName;

    public final String getSimpleName() {
        if (mSimpleName == null) {
            mSimpleName = getClass().getSimpleName();
        }
        return mSimpleName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onBackPressed() {
        MainFragmentManager.closeFragment();
    }

    /**
     * 如果该Fragment在ViewPager里面,当Fragment可见的时候自动被回调
     */
    public void onVisibleInViewPager() {
        Logger.w(TAG, getSimpleName() + " [onVisibleInViewPager]");
    }

    /**
     * 如果该Fragment在ViewPager里面,当Fragment不可见的时候自动被回调
     */
    public void onInVisibleInViewPager() {
        Logger.w(TAG, getSimpleName() + " [onInVisibleInViewPager]");
    }

    public LaunchMode getLaunchMode() {
        return LaunchMode.STANDARD;
    }

    public enum LaunchMode {
        STANDARD, SINGLE
    }
}
