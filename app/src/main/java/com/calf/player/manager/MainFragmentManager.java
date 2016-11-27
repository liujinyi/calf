package com.calf.player.manager;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.calf.R;
import com.calf.fragments.base.BaseFragment;
import com.calf.frame.tool.Assert;
import com.calf.player.activitys.MainActivity;

import java.util.Stack;

/**
 * Created by JinYi Liu on 16-11-13.
 */

public class MainFragmentManager {

    private static FragmentManager mFragmentManager;
    private static Stack<BaseFragment> mStacks = new Stack<>();

    private MainFragmentManager() {
    }

    public static void init(MainActivity activity) {
        if (mFragmentManager == null) {
            mFragmentManager = activity.getSupportFragmentManager();
        } else {
            Assert.classAssert(false, "MainFragmentManager [init] has execute");
        }
    }

    public static void release() {
        mFragmentManager = null;
    }

    public static void showFragment(BaseFragment f) {
        String tag = null;
        switch (f.giveMeLaunchMode()) {
            case SINGLE:
                tag = f.getSimpleName();
                navigateFragment(tag, f);
                break;
            default:
                tag = String.valueOf(f.hashCode());
                break;
        }
        showFragment(tag, R.id.fragment_container, f);
    }

    private static void showFragment(String tag, @IdRes int container, BaseFragment f) {
        BaseFragment topFragment = getTopFragment();
        if (topFragment != null) {
            topFragment.onFragmentInVisible();
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(container, f, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        mStacks.push(f);
    }

    public static void closeFragment() {
        if (mStacks.size() > 0) {
            mStacks.pop();
            mFragmentManager.popBackStackImmediate();
            BaseFragment topFragment = getTopFragment();
            if (topFragment != null) {
                topFragment.onFragmentVisible();
            }
        }
    }

    public static BaseFragment getTopFragment() {
        if (mStacks.size() == 0) {
            return null;
        }
        return mStacks.peek();
    }

    public static int getFragmentsSize() {
        return mFragmentManager.getBackStackEntryCount();
    }

    private static void navigateFragment(String tag, BaseFragment f) {
        if (isExist(tag)) {
            while (true) {
                BaseFragment topFragment = getTopFragment();
                if (topFragment == null) {
                    break;
                }
                mFragmentManager.popBackStackImmediate();
                mStacks.pop();
                if (topFragment.getClass() == f.getClass()) {
                    break;
                }
            }

        }
    }

    private static boolean isExist(String key) {
        return mFragmentManager.findFragmentByTag(key) != null;
    }


}
