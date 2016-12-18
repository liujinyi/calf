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
import com.calf.frame.tool.Assert;
import com.calf.player.R;
import com.calf.player.manager.MainFragmentManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public abstract class BaseFragment<T> extends Fragment {

    protected static final String TAG = "BaseFragment";
    private static final String MESSAGE_PRELOAD_VIEW = "viewpager preload view create";
    private static final String MESSAGE_SET_CURRENT_STATE = "user call setInitState method";
    private static final String DEFAULT_EMPTY_CONTENT = "暂无内容!";
    private static final String DEFAULT_LOADING_CONTENT = "正在加载中...";
    private static final String DEFAULT_FAILURE_CONTENT = "加载失败,点击屏幕重试!";
    private static final String DEFAULT_NO_NET_CONTENT = "无可用网络连接,点击屏幕重试!";

    private T t;
    private String mSimpleName;
    private String mNoNetContent;
    private String mEmptyContent;
    private boolean mFailureFlag;
    private boolean mInViewPager;
    private Callback<T> mCallback;
    private Behavior<T> mBehavior;
    private boolean mVisibleToUser;
    private String mLoadingContent;
    private String mFailureContent;
    private boolean mHasOnCreateView;
    private boolean mRestoreFragment;
    private Bundle mSavedInstanceState;
    private boolean mHasOnCreateContentView;
    private State mCurrentState = State.INIT;

    private ViewGroup mRootContainer;
    private FrameLayout mTitleContainer;
    private FrameLayout mContentContainer;

    protected abstract ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, T t);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBehavior = onBehaviorSetup();
        this.mSavedInstanceState = savedInstanceState;
        if (mBehavior != null) {
            mCallback = new Callback<T>() {
                @Override
                public T onBackgroundParser(String data) throws Exception {
                    if (mBehavior instanceof NetPageBehavior) {
                        return (T) ((NetPageBehavior) mBehavior).onBackgroundParser(data);
                    }
                    return null;
                }

                @Override
                public void onState(State state, String message) {
                    showStateView(state, message);
                }

                @Override
                public void onSuccess(T t, Bundle savedInstanceState) {
                    showContentView(savedInstanceState, t);
                }
            };
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRestoreFragment = true;
        mHasOnCreateView = false;
        mCurrentState = State.INIT;
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
        this.mRootContainer = (ViewGroup) inflater.inflate(com.calf.player.R.layout.fragment_base, container, false);
        this.mTitleContainer = (FrameLayout) mRootContainer.findViewById(com.calf.player.R.id.title_container);
        this.mContentContainer = (FrameLayout) mRootContainer.findViewById(com.calf.player.R.id.content_container);
        if (isDisplayTitleContainer()) {
            ViewGroup titleContainer = onCreateTitleView(inflater, mTitleContainer);
            if (titleContainer != null) {
                mTitleContainer.addView(titleContainer);
            }
        }
        if (mBehavior != null) {
            mBehavior.init(mCallback, inflater, container);
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

    protected boolean isDisplayTitleContainer() {
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

    protected final void setLoadingContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            this.mLoadingContent = content;
        }
    }

    protected final void setFailureContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            this.mFailureContent = content;
        }
    }

    protected final void setNoNetContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            this.mNoNetContent = content;
        }
    }

    protected final void setEmptyContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            this.mEmptyContent = content;
        }
    }

    protected final String getEmptyContent() {
        return TextUtils.isEmpty(mEmptyContent) ? DEFAULT_EMPTY_CONTENT : mEmptyContent;
    }

    protected final String getNoNetContent() {
        return TextUtils.isEmpty(mNoNetContent) ? DEFAULT_NO_NET_CONTENT : mNoNetContent;
    }

    protected final String getLoadingContent() {
        return TextUtils.isEmpty(mLoadingContent) ? DEFAULT_LOADING_CONTENT : mLoadingContent;
    }

    protected final String getFailureContent() {
        return TextUtils.isEmpty(mFailureContent) ? DEFAULT_FAILURE_CONTENT : mFailureContent;
    }

    protected void onToolBarSetup(Toolbar toolbar) {
        toolbar.setTitle(getSimpleName());
    }

    protected final void onRetry() {
        if (mBehavior != null) {
            mBehavior.doInBackground(mSavedInstanceState);
        }
    }

    protected void beforeOnCreateStateView(State state) {
    }

    protected void beforeOnCreateContentView() {
    }

    protected void afterOnCreateStateView(State state, String message) {
        if (state == State.FAILURE) {
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

    protected ViewGroup onCreateLoadingView(LayoutInflater inflater, ViewGroup container) {
        return SimpleFactory.createStateView(inflater, container, getLoadingContent());
    }

    protected ViewGroup onCreateFailureView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup child = SimpleFactory.createStateView(inflater, container, getFailureContent());
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetry();
            }
        });
        return child;
    }

    protected ViewGroup onCreateNoNetView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup child = SimpleFactory.createStateView(inflater, container, getNoNetContent());
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetry();
            }
        });
        return child;
    }

    protected ViewGroup onCreateEmptyView(LayoutInflater inflater, ViewGroup container) {
        return SimpleFactory.createStateView(inflater, container, getEmptyContent());
    }

    private void addContentView(ViewGroup child) {
        if (mContentContainer.getChildCount() > 0) {
            mContentContainer.removeAllViews();
        }
        mContentContainer.addView(child);
    }

    private void showStateView(final State state, final String message) {
        MessageManager.post(new Runnable() {
            @Override
            public void run() {
                if (isFragmentAlive() && !mHasOnCreateContentView && state != mCurrentState) {
                    beforeOnCreateStateView(state);
                    addContentView(createStateView(state));
                    mCurrentState = state;
                    afterOnCreateStateView(state, message);
                }
            }
        });
    }

    private ViewGroup createStateView(State state) {
        ViewGroup child = null;
        switch (state) {
            case FAILURE:
                child = onCreateFailureView(getLayoutInflater(), getContentContainer());
                break;
            case LOADING:
                child = onCreateLoadingView(getLayoutInflater(), getContentContainer());
                break;
            case NO_NET:
                child = onCreateNoNetView(getLayoutInflater(), getContentContainer());
                break;
            default:
                break;
        }
        return child;
    }

    private void showContentView(final Bundle savedInstanceState, final T t) {
        MessageManager.post(new Runnable() {
            @Override
            public void run() {
                if (isFragmentAlive() && isCanDisplayContentView()) {
                    beforeOnCreateContentView();
                    addContentView(onCreateContentView(getLayoutInflater(), mContentContainer, savedInstanceState, t));
                    mHasOnCreateContentView = true;
                    afterOnCreateContentView(t);
                    BaseFragment.this.t = t;
                }
            }
        });
    }

    private boolean isCanDisplayContentView() {
        boolean flag = false;
        if ((mCurrentState == State.INIT || mCurrentState == State.LOADING) && !mHasOnCreateContentView) {
            flag = true;
        }
        return flag;
    }

    private void handlerPreloadInViewPager(Bundle savedInstanceState) {
        if (!isFragmentAlive()) {
            return;
        }
        if (mInViewPager && !isPreloadInViewPager() && !mVisibleToUser) {
            showStateView(State.LOADING, MESSAGE_PRELOAD_VIEW);
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
                    showStateView(State.FAILURE, MESSAGE_SET_CURRENT_STATE);
                } else {
                    mBehavior.doInBackground(savedInstanceState);
                }
            }
        }
    }

    public enum LaunchMode {
        STANDARD, SINGLE
    }

    protected enum State {
        INIT, LOADING, FAILURE, NO_NET
    }

    protected interface Callback<T> {
        T onBackgroundParser(String data) throws Exception;

        void onState(State state, String message);

        void onSuccess(T t, Bundle savedInstanceState);
    }

    protected interface Decoder {
        byte[] decode(byte[] bytes);
    }

    protected static abstract class Behavior<T> {

        private ViewGroup mContainer;
        private Callback<T> mCallback;
        private LayoutInflater mInflater;

        protected Callback<T> getCallback() {
            return mCallback;
        }

        protected final ViewGroup getContainer() {
            return mContainer;
        }

        protected final LayoutInflater getInflater() {
            return mInflater;
        }

        final void init(Callback<T> callback, LayoutInflater inflater, ViewGroup container) {
            if (callback == null || inflater == null || container == null) {
                Assert.classAssert(false, "Behavior [init] parameter is null");
            }
            this.mCallback = callback;
            this.mInflater = inflater;
            this.mContainer = container;
        }

        protected abstract void doInBackground(Bundle savedInstanceState);

    }

    protected static abstract class BaseTask implements Runnable {

        private final AtomicBoolean mAlive;
        private final AtomicBoolean mCancel;

        public BaseTask() {
            this.mAlive = new AtomicBoolean(true);
            this.mCancel = new AtomicBoolean(false);
        }

        public final void die() {
            this.mAlive.set(false);
        }

        public final void cancel() {
            this.mCancel.set(true);
        }

        public final boolean isAlive() {
            return mAlive.get();
        }

        public final boolean isCancel() {
            return mCancel.get();
        }

        public final boolean isCancelOrDie() {
            return isCancel() || !isAlive();
        }

    }

}
