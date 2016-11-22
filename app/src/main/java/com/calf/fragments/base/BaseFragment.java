package com.calf.fragments.base;

import android.os.Bundle;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onBackPressed() {
        MainFragmentManager.closeFragment();
    }

    public void onFragmentVisible() {
        Logger.w(TAG, getSimpleName() + " [onFragmentVisible]");
    }

    public void onFragmentInVisible() {
        Logger.w(TAG, getSimpleName() + " [onFragmentInVisible]");
    }

    public LaunchMode getLaunchMode() {
        return LaunchMode.STANDARD;
    }

    public enum LaunchMode {
        STANDARD, SINGLE
    }

}
