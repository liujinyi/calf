package com.calf.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calf.frame.log.Logger;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public abstract class BaseFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    private static final String TAG = "BaseFragment";

    private boolean mInViewPager;
    private boolean mVisibleToUser;
    private String mSimpleName;
    private Behavior mBehavior;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void onBackPressed() {
        MainFragmentManager.closeFragment();
    }

    /**
     * true : 在ViewPager中,Fragment会预加载 <br>
     * false: 在ViewPager中,Fragment不会预加载
     */
    protected boolean isPreloadInViewPager() {
        return false;
    }

    protected final boolean isFragmentAlive() {
        return (getActivity() != null && !getActivity().isFinishing() && !isDetached());
    }

    public void onFragmentVisible() {
        Logger.w(TAG, getSimpleName() + " [onFragmentVisible]");
    }

    public void onFragmentInVisible() {
        Logger.w(TAG, getSimpleName() + " [onFragmentInVisible]");
    }

    protected Behavior giveMeBehavior() {
        return null;
    }

    public LaunchMode giveMeLaunchMode() {
        return LaunchMode.STANDARD;
    }

    public enum LaunchMode {
        STANDARD, SINGLE
    }

    enum State {
        SUCCESS, FAILURE, LOADING, NO_NET
    }

    public interface Behavior {

        public void doInBackground();

        public void refresh(State state);

    }


}
