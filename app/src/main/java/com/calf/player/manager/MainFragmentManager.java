package com.calf.player.manager;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;

import com.calf.fragments.BaseFragment;
import com.calf.frame.tool.Assert;
import com.calf.mode.Mode;
import com.calf.player.activitys.MainActivity;

/**
 * Created by JinYi Liu on 16-11-13.
 */

public class MainFragmentManager {

    private static FragmentManager mFragmentManager;
    private static ArrayMap<String, BaseFragment> mFragments = new ArrayMap<>();

    private MainFragmentManager() {
    }

    public static void showFragment(@NonNull BaseFragment f) {
        showFragment(String.valueOf(f.hashCode()), 0, f);
    }

    public static void showFragment(String tag, @IdRes int container, @NonNull BaseFragment f) {

    }

    public static void closeFragment() {

    }


    public static BaseFragment getTopFragment() {

        return null;
    }

    public static void navigateFragment(BaseFragment f) {
        navigateFragment(f, false);
    }

    public static void navigateFragment(BaseFragment f, boolean flag) {
        if (isExistFragment(f)) {
            while (true) {
                BaseFragment topFragment = getTopFragment();
                String key = String.valueOf(topFragment);
                if (topFragment == f) {
                    if (flag) {
                        mFragments.remove(key);
                    }
                    break;
                } else {
                    mFragments.remove(key);
                }
            }

        }
    }

    public static boolean isExistFragment(BaseFragment f) {
        return mFragments.containsKey(String.valueOf(f.hashCode()));
    }

    public static int getFragmentsSize() {
        return mFragments.size();
    }

    public static void init(MainActivity activity) {
        if (mFragmentManager == null) {
            mFragmentManager = activity.getSupportFragmentManager();
        } else {
            Assert.classAssert(false, "MainFragmentManager [init] has execute");
        }
    }

    private static void popFragment() {

    }
}
