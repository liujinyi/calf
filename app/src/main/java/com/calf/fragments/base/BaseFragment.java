package com.calf.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.calf.factory.SimpleFactory;
import com.calf.frame.log.Logger;
import com.calf.frame.message.MessageManager;
import com.calf.player.R;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public abstract class BaseFragment<T> extends Fragment {

    public static final int STATE_NO_NET = 1001;
    public static final int STATE_FAILURE = 1002;
    public static final int STATE_LOADING = 1003;

    private static final String TAG = "BaseFragment";
    private static final String MESSAGE_PRELOAD_VIEW = "viewpager preload view create";
    private static final String MESSAGE_SET_CURRENT_STATE = "user call setInitState method";

    private int mCurrentState;
    private String mSimpleName;
    private boolean mFailureFlag;
    private boolean mInViewPager;
    private boolean mVisibleToUser;
    private boolean mHasOnCreateView;
    private boolean mRestoreFragment;
    private boolean mHasOnCreateContentView;

    private T t;
    private Callback<T> mCallback;
    private Behavior<T> mBehavior;
    private Bundle mSavedInstanceState;

    private ViewGroup mRootContainer;
    private FrameLayout mTitleContainer;
    private FrameLayout mContentContainer;

    protected abstract ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, T t);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBehavior = onBehaviorSetup();
        if (mBehavior != null) {
            mCallback = new Callback<T>() {
                @Override
                public void onState(int state, String message) {
                    showStateView(state, mBehavior, message);
                }

                @Override
                public void onSuccess(T t, Bundle savedInstanceState) {
                    BaseFragment.this.t = t;
                    showContentView(savedInstanceState, t);
                }
            };
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCurrentState = 0;
        mRestoreFragment = true;
        mHasOnCreateView = false;
        mHasOnCreateContentView = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!mInViewPager) {
            mInViewPager = true;
        }
        mVisibleToUser = isVisibleToUser;
        if (!mHasOnCreateView) {
            return;
        }
        if (isVisibleToUser && !mHasOnCreateContentView) {
            handlerPreloadInViewPager(mSavedInstanceState);
        } else if (isVisibleToUser) {
            onFragmentVisible();
        } else {
            onFragmentInVisible();
        }
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
        this.mRootContainer = (ViewGroup) inflater.inflate(com.calf.player.R.layout.fragment_base, container, false);
        this.mTitleContainer = (FrameLayout) mRootContainer.findViewById(com.calf.player.R.id.title_container);
        this.mContentContainer = (FrameLayout) mRootContainer.findViewById(com.calf.player.R.id.content_container);
        if (isShowTitleContainer()) {
            ViewGroup titleContainer = onCreateTitleView(inflater, mTitleContainer);
            if (titleContainer != null) {
                mTitleContainer.addView(titleContainer);
            }
        }
//        if (mBehavior != null) {
//            mBehavior.initBehavior(inflater, mContentContainer, mCallback);
//        }
        handlerPreloadInViewPager(savedInstanceState);
        this.mHasOnCreateView = true;
        return this.mRootContainer;
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

    public final String getSimpleName() {
        if (mSimpleName == null) {
            mSimpleName = getClass().getSimpleName();
        }
        return mSimpleName;
    }

    public LaunchMode giveMeLaunchMode() {
        return LaunchMode.STANDARD;
    }

    public final boolean isFragmentAlive() {
        return (getActivity() != null && !getActivity().isFinishing() && !isDetached());
    }

    public final void setFailureFlag() {
        this.mFailureFlag = true;
    }

    public final void setSuccessFlag(T t) {
        this.t = t;
    }

    protected Behavior<T> onBehaviorSetup() {
        return null;
    }

    /**
     * true : 在ViewPager中,Fragment会预加载 <br>
     * false: 在ViewPager中,Fragment不会预加载
     */
    protected boolean isPreloadInViewPager() {
        return false;
    }

    protected boolean isShowTitleContainer() {
        return getParentFragment() == null;
    }

    protected final LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getActivity());
    }

    protected final FrameLayout getContentContainer() {
        return mContentContainer;
    }

    protected final void showEmptyView() {
        if (isFragmentAlive() && mHasOnCreateContentView) {
            int count = mContentContainer.getChildCount();
            if (count == 1) {
                mContentContainer.addView(onCreateEmptyView(getLayoutInflater(), mContentContainer));
            }
        }
    }

    protected final void removeEmptyView() {
        if (isFragmentAlive() && mHasOnCreateContentView) {
            int count = mContentContainer.getChildCount();
            if (count == 2) {
                mContentContainer.removeViewAt(count - 1);
            }
        }
    }

    public ViewGroup onCreateEmptyView(LayoutInflater inflater, ViewGroup container) {
        return SimpleFactory.createStateView(inflater, container, "暂无内容");
    }

    protected ViewGroup onCreatePreloadView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup child;
        if (mBehavior == null) {
            child = SimpleFactory.createStateView(inflater, container, AbstractStateViewFactory.DEFAULT_LOADING_CONTENT);
        } else {
            child = mBehavior.onCreateStateView(STATE_LOADING);
        }
        return child;
    }

    protected void onToolBarSetup(Toolbar toolbar) {
        toolbar.setTitle(getSimpleName());
    }

    protected void beforeOnCreateStateView(int state) {

    }

    protected void beforeOnCreateContentView() {

    }

    protected void afterOnCreateStateView(int state, String message) {
        if (state == STATE_FAILURE) {
            Logger.e(TAG, getSimpleName() + " failure message:" + message);
        }
    }

    protected void afterOnCreateContentView(T t) {

    }

    protected ViewGroup onCreateTitleView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup child = (ViewGroup) inflater.inflate(com.calf.player.R.layout.layout_common_title, container, false);
        Toolbar toolbar = (Toolbar) child.findViewById(com.calf.player.R.id.common_toolbar);
        toolbar.setNavigationIcon(R.drawable.selector_common_back);
        onToolBarSetup(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return child;
    }

    private void addContentView(ViewGroup child) {
        if (mContentContainer.getChildCount() > 0) {
            mContentContainer.removeAllViews();
        }
        mContentContainer.addView(child);
    }

    private void showPreloadView(final int state, final String message) {
        MessageManager.post(new Runnable() {
            @Override
            public void run() {
                if (isFragmentAlive() && !mHasOnCreateContentView && state != mCurrentState) {
                    beforeOnCreateStateView(state);
                    addContentView(onCreatePreloadView(getLayoutInflater(), mContentContainer));
                    mCurrentState = state;
                    afterOnCreateStateView(state, message);
                }
            }
        });
    }

    private void showStateView(final int state, final Behavior behavior, final String message) {
        MessageManager.post(new Runnable() {
            @Override
            public void run() {
                if (isFragmentAlive() && !mHasOnCreateContentView && state != mCurrentState) {
                    beforeOnCreateStateView(state);
                    addContentView(behavior.onCreateStateView(state));
                    mCurrentState = state;
                    afterOnCreateStateView(state, message);
                }
            }
        });
    }

    private void showContentView(final Bundle savedInstanceState, final T t) {
        MessageManager.post(new Runnable() {
            @Override
            public void run() {
                if (isFragmentAlive() && !mHasOnCreateContentView) {
                    beforeOnCreateContentView();
                    addContentView(onCreateContentView(getLayoutInflater(), mContentContainer, savedInstanceState, t));
                    mHasOnCreateContentView = true;
                    afterOnCreateContentView(t);
                }
            }
        });
    }

    private void handlerPreloadInViewPager(Bundle savedInstanceState) {
        if (!isFragmentAlive()) {
            return;
        }
        if (mInViewPager && !isPreloadInViewPager() && !mVisibleToUser) {
            showPreloadView(STATE_LOADING, MESSAGE_PRELOAD_VIEW);
        } else {
            if (mRestoreFragment) {
                mRestoreFragment = false;
                if (t != null) {
                    showContentView(savedInstanceState, t);
                    return;
                }
            }
            if (mBehavior == null) {
                showContentView(savedInstanceState, t);
            } else {
                if (t != null) {
                    showContentView(savedInstanceState, t);
                } else if (mFailureFlag) {
                    showStateView(STATE_FAILURE, mBehavior, MESSAGE_SET_CURRENT_STATE);
                } else {
                    mBehavior.doInBackground(savedInstanceState);
                }
            }
        }
    }

    public enum LaunchMode {
        STANDARD, SINGLE
    }

    interface StateViewFactory {

        ViewGroup onCreateLoadingView(LayoutInflater inflater, ViewGroup container);

        ViewGroup onCreateFailureView(LayoutInflater inflater, ViewGroup container);

    }

    protected abstract class Behavior<T> {

        protected abstract ViewGroup onCreateStateView(int state);

        protected abstract void doInBackground(Bundle savedInstanceState);

    }

    protected interface Callback<T> {

        void onState(int state, String message);

        void onSuccess(T t, Bundle savedInstanceState);

    }

    protected interface OnRetryListener {
        void onRetry();
    }

    protected abstract class AbstractBehavior extends Behavior<T> {

        private OnRetryListener mListener;
        private AbstractStateViewFactory mFactory;

        protected AbstractBehavior(AbstractStateViewFactory factory) {
            this.mListener = new OnRetryListener() {
                @Override
                public void onRetry() {
                    if (mCallback != null) {
                        doInBackground(mSavedInstanceState);
                    }
                }
            };
            this.mFactory = factory;
            this.mFactory.setListener(mListener);
        }

        public final void setFailureContent(String content) {
            mFactory.setFailureContent(content);
        }

        public final void setLoadingContent(String content) {
            mFactory.setLoadingContent(content);
        }

        public final OnRetryListener getListener() {
            return mListener;
        }

        protected final AbstractStateViewFactory getFactory() {
            return mFactory;
        }

    }

    protected abstract class BackgroundBehavior extends AbstractBehavior {

        protected BackgroundBehavior() {
            super(new BackgroundStateViewFactory());
        }

        @Override
        protected ViewGroup onCreateStateView(int state) {
            switch (state) {
                case STATE_LOADING:
                    return getFactory().onCreateLoadingView(getLayoutInflater(), getContentContainer());
                case STATE_FAILURE:
                    return getFactory().onCreateFailureView(getLayoutInflater(), getContentContainer());
                default:
                    return null;
            }
        }

        @Override
        protected void doInBackground(final Bundle savedInstanceState) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mCallback.onState(STATE_LOADING, "start onBackgroundLoading");
                        mCallback.onSuccess(onBackgroundLoading(), savedInstanceState);
                    } catch (Exception e) {
                        Logger.printStackTrace(e);
                        mCallback.onState(STATE_FAILURE, e.getMessage());
                    }
                }
            }).start();
        }

        protected abstract T onBackgroundLoading() throws Exception;

    }

    protected abstract class NetBehavior extends AbstractBehavior {

        protected NetBehavior() {
            super(new NetStateViewFactory());
        }

        protected abstract String giveMeUrl();

        protected abstract T onBackgroundParser(String message);

    }

    static abstract class AbstractStateViewFactory implements StateViewFactory {

        static final String DEFAULT_LOADING_CONTENT = "正在加载中...";
        static final String DEFAULT_FAILURE_CONTENT = "加载失败,点击屏幕重试!";
        static final String DEFAULT_NO_NET_CONTENT = "无可用网络连接,点击屏幕重试!";

        private String mLoadingContent;
        private String mFailureContent;
        private OnRetryListener mListener;

        void setLoadingContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mLoadingContent = content;
            }
        }

        void setFailureContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mFailureContent = content;
            }
        }

        String getLoadingContent() {
            return mLoadingContent;
        }

        String getFailureContent() {
            return mFailureContent;
        }

        void setListener(OnRetryListener listener) {
            this.mListener = listener;
        }

        OnRetryListener getListener() {
            return mListener;
        }

    }

    static class BackgroundStateViewFactory extends AbstractStateViewFactory {

        @Override
        public ViewGroup onCreateLoadingView(LayoutInflater inflater, ViewGroup container) {
            String content = getLoadingContent();
            content = TextUtils.isEmpty(content) ? DEFAULT_LOADING_CONTENT : content;
            return SimpleFactory.createStateView(inflater, container, content);
        }

        @Override
        public ViewGroup onCreateFailureView(LayoutInflater inflater, ViewGroup container) {
            String content = getFailureContent();
            content = TextUtils.isEmpty(content) ? DEFAULT_FAILURE_CONTENT : content;
            ViewGroup child = SimpleFactory.createStateView(inflater, container, content);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getListener().onRetry();
                }
            });
            return child;
        }

    }

    static class NetStateViewFactory extends BackgroundStateViewFactory {
        private String mNoNetContent;

        public final void setNoNetContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mNoNetContent = content;
            }
        }

        public ViewGroup onCreateNoNetView(LayoutInflater inflater, ViewGroup container) {
            String content = mNoNetContent;
            content = TextUtils.isEmpty(content) ? DEFAULT_NO_NET_CONTENT : content;
            return SimpleFactory.createStateView(inflater, container, content);
        }

    }

}
