package com.calf.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.calf.R;
import com.calf.factory.SimpleFactory;
import com.calf.frame.log.Logger;
import com.calf.frame.message.MessageManager;
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
    private Behavior mBehavior;
    private Callback<T> mCallback;
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
                public void onState(final int state, final String message) {
                    showStateView(state, mBehavior, message);
                }

                @Override
                public void onSuccess(final T t, final Bundle savedInstanceState) {
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
        this.mRootContainer = (ViewGroup) inflater.inflate(R.layout.fragment_base, container, false);
        this.mTitleContainer = (FrameLayout) mRootContainer.findViewById(R.id.title_container);
        this.mContentContainer = (FrameLayout) mRootContainer.findViewById(R.id.content_container);
        if (isShowTitleContainer()) {
            ViewGroup titleContainer = onCreateTitleView(inflater, mTitleContainer);
            if (titleContainer != null) {
                mTitleContainer.addView(titleContainer);
            }
        }
        if (mBehavior != null) {
            mBehavior.initBehavior(inflater, mContentContainer, mCallback);
        }
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

    protected Behavior onBehaviorSetup() {
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
            child = SimpleFactory.createStateView(inflater, container, StateViewFactory.DEFAULT_LOADING_CONTENT);
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
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.layout_common_title, container, false);
        Toolbar toolbar = (Toolbar) child.findViewById(R.id.common_toolbar);
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


    protected interface Behavior<T> {

        ViewGroup onCreateStateView(int state);

        void doInBackground(Bundle savedInstanceState);

        void initBehavior(LayoutInflater inflater, ViewGroup container, Callback<T> callback);

    }

    interface Callback<T> {

        void onState(int state, String message);

        void onSuccess(T t, Bundle savedInstanceState);

    }

    protected abstract class LocalBehavior implements Behavior<T> {

        private ViewGroup mContainer;
        private Callback<T> mCallback;
        private LayoutInflater mInflater;
        private OnRetryListener mListener;
        private Bundle mSavedInstanceState;
        private LocalStateViewFactory mFactory;

        protected LocalBehavior() {
            this.mListener = new OnRetryListener() {
                @Override
                public void onRetry() {
                    if (mCallback != null) {
                        doInBackground(mSavedInstanceState);
                    }
                }
            };
        }

        public final void setFailureContent(String content) {
            getFactory().setFailureContent(content);
        }

        public final void setLoadingContent(String content) {
            getFactory().setLoadingContent(content);
        }

        public final OnRetryListener getListener() {
            return mListener;
        }

        public abstract T onBackgroundLoading() throws Exception;

        private LocalStateViewFactory getFactory() {
            if (mFactory == null) {
                mFactory = new LocalStateViewFactory(mListener);
            }
            return mFactory;
        }

        @Override
        public void initBehavior(LayoutInflater inflater, ViewGroup container, Callback<T> callback) {
            if (inflater == null || container == null || callback == null) {
                throw new RuntimeException("LocalBehavior [doInBackground] has null parameter");
            }
            this.mCallback = callback;
            this.mInflater = inflater;
            this.mContainer = container;
        }

        @Override
        public final void doInBackground(Bundle savedInstanceState) {
            this.mSavedInstanceState = savedInstanceState;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mCallback.onState(STATE_LOADING, "start onBackgroundLoading");
                        mCallback.onSuccess(onBackgroundLoading(), mSavedInstanceState);
                    } catch (Exception e) {
                        Logger.printStackTrace(e);
                        mCallback.onState(STATE_FAILURE, e.getMessage());
                    }
                }
            }).start();
        }

        @Override
        public final ViewGroup onCreateStateView(int state) {
            StateViewFactory factory = getFactory();
            switch (state) {
                case STATE_LOADING:
                    return factory.onCreateLoadingView(mInflater, mContainer);
                case STATE_FAILURE:
                    return factory.onCreateFailureView(mInflater, mContainer);
                default:
                    return null;
            }
        }
    }

    protected abstract class OnlineBehavior implements Behavior<T> {

        public abstract String giveMeUrl();

        public abstract T onBackgroundParser(String data);

        private ViewGroup mContainer;
        private Callback<T> mCallback;
        private LayoutInflater mInflater;
        private OnRetryListener mListener;
        private Bundle mSavedInstanceState;
        private OnlineStateViewFactory mFactory;

        protected OnlineBehavior() {
            this.mListener = new OnRetryListener() {
                @Override
                public void onRetry() {
                    if (mCallback != null) {
                        doInBackground(mSavedInstanceState);
                    }
                }
            };
        }

        @Override
        public final ViewGroup onCreateStateView(int state) {
            switch (state) {
                case STATE_NO_NET:
                    return mFactory.onCreateNoNetView(mInflater, mContainer);
                case STATE_LOADING:
                    return mFactory.onCreateLoadingView(mInflater, mContainer);
                case STATE_FAILURE:
                    return mFactory.onCreateFailureView(mInflater, mContainer);
                default:
                    return null;
            }
        }

    }

    protected interface OnRetryListener {
        void onRetry();
    }

    interface StateViewFactory {

        String DEFAULT_LOADING_CONTENT = "正在加载中...";
        String DEFAULT_FAILURE_CONTENT = "加载失败,点击屏幕重试!";
        String DEFAULT_NO_NET_CONTENT = "无可用网络连接,点击屏幕重试!";

        ViewGroup onCreateLoadingView(LayoutInflater inflater, ViewGroup container);

        ViewGroup onCreateFailureView(LayoutInflater inflater, ViewGroup container);
    }

    static class LocalStateViewFactory implements StateViewFactory {

        private String mFailureContent;
        private String mLoadingContent;
        private OnRetryListener mListener;

        LocalStateViewFactory(OnRetryListener listener) {
            this.mListener = listener;
        }

        final void setFailureContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mFailureContent = content;
            }
        }

        final void setLoadingContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mLoadingContent = content;
            }
        }

        final OnRetryListener getListener() {
            return mListener;
        }

        @Override
        public ViewGroup onCreateLoadingView(LayoutInflater inflater, ViewGroup container) {
            return SimpleFactory.createStateView(inflater, container, TextUtils.isEmpty(mLoadingContent) ? DEFAULT_LOADING_CONTENT : mLoadingContent);
        }

        @Override
        public ViewGroup onCreateFailureView(LayoutInflater inflater, ViewGroup container) {
            ViewGroup child = SimpleFactory.createStateView(inflater, container, TextUtils.isEmpty(mFailureContent) ? DEFAULT_FAILURE_CONTENT : mFailureContent);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getListener().onRetry();
                }
            });
            return child;
        }

    }

    static class OnlineStateViewFactory extends LocalStateViewFactory {

        private String mNoNetContent;

        public OnlineStateViewFactory(OnRetryListener listener) {
            super(listener);
        }

        public final void setNoNetContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mNoNetContent = content;
            }
        }

        public ViewGroup onCreateNoNetView(LayoutInflater inflater, ViewGroup container) {
            return SimpleFactory.createStateView(inflater, container, TextUtils.isEmpty(mNoNetContent) ? DEFAULT_NO_NET_CONTENT : mNoNetContent);
        }

    }

}
