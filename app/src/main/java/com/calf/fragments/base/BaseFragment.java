package com.calf.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.R;
import com.calf.frame.log.Logger;
import com.calf.frame.message.MessageManager;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public abstract class BaseFragment<T> extends Fragment {

    public static final int STATE_EMPTY = 1001;
    public static final int STATE_NO_NET = 1002;
    public static final int STATE_FAILURE = 1003;
    public static final int STATE_LOADING = 1004;
    public static final int STATE_SUCCESS = 1005;

    private static final String TAG = "BaseFragment";
    private static final String MESSAGE_PRELOAD_VIEW = "viewpager preload view create";
    private static final String MESSAGE_SET_CURRENT_STATE = "user call setCurrentState method";

    private int mCurrentState;
    private String mSimpleName;
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
    private ViewGroup mTitleContainer;
    private ViewGroup mContentContainer;

    protected abstract ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, T t);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG, getSimpleName() + " [onCreate]");
        mBehavior = onBehaviorSetup();
        if (mBehavior != null) {
            mCallback = new Callback<T>() {
                @Override
                public void onState(final int state, final ViewGroup child, final String message) {
                    if (isFragmentAlive()) {
                        MessageManager.post(new Runnable() {
                            @Override
                            public void run() {
                                beforeOnCreateStateView(state);
                                showStateView(state, child);
                                afterOnCreateStateView(state, message);
                            }
                        });
                    }
                }

                @Override
                public void onSuccess(final T t, final Bundle savedInstanceState) {
                    BaseFragment.this.t = t;
                    if (isFragmentAlive()) {
                        MessageManager.post(new Runnable() {
                            @Override
                            public void run() {
                                showContentView(onCreateContentView(getLayoutInflater(), mContentContainer, savedInstanceState, t));
                            }
                        });
                    }
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
        Logger.w(TAG, getSimpleName() + " [onDestroyView]");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e(TAG, getSimpleName() + " [onDestroy]");
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!mInViewPager) {
            mInViewPager = true;
        }
        mVisibleToUser = isVisibleToUser;
        if (!mHasOnCreateView) {
            //Logger.e(TAG, getSimpleName() + " [setUserVisibleHint] mHasOnCreateView is false");
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
        Logger.w(TAG, getSimpleName() + " [onCreateView]");
        this.mSavedInstanceState = savedInstanceState;
        this.mRootContainer = (ViewGroup) inflater.inflate(R.layout.fragment_base, container, false);
        this.mTitleContainer = (ViewGroup) mRootContainer.findViewById(R.id.title_container);
        this.mContentContainer = (ViewGroup) mRootContainer.findViewById(R.id.content_container);
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

    public final void setCurrentState(int state) {
        if (state >= STATE_EMPTY && state <= STATE_SUCCESS) {
            this.mCurrentState = state;
        }
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
        boolean flag = false;
        Fragment parent = getParentFragment();
        if (parent == null) {
            Logger.e(TAG, getSimpleName() + " [isShowTitleContainer] parent fragment is null");
            flag = true;
        } else {
            Logger.e(TAG, getSimpleName() + " [isShowTitleContainer] parent fragment is :" + parent.toString());
        }
        return flag;
    }

    protected final LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getActivity());
    }

    protected final ViewGroup getContentContainer() {
        return mContentContainer;
    }

    protected final void showEmptyView() {
        if (mHasOnCreateContentView) {
            int count = mContentContainer.getChildCount();
            if (count == 1) {
                mContentContainer.addView(mBehavior.onCreateStateView(STATE_EMPTY));
            }
        }
    }

    protected final void removeEmptyView() {
        if (mHasOnCreateContentView) {
            int count = mContentContainer.getChildCount();
            if (count == 2) {
                mContentContainer.removeViewAt(count - 1);
            }
        }
    }

    protected ViewGroup onCreatePreloadView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup child = null;
        if (mBehavior == null) {
            child = (ViewGroup) inflater.inflate(R.layout.layout_common_state, container, false);
            TextView textView = (TextView) child.findViewById(R.id.tv_common_content);
            textView.setText("正在加载中...");
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

    protected void afterOnCreateStateView(int state, String message) {
        if (state == STATE_FAILURE) {
            Logger.e(TAG, getSimpleName() + " failure message:" + message);
        } else {
            //Logger.e(TAG, getSimpleName() + " [afterOnCreateStateView] state :" + state);
        }
    }

    protected ViewGroup onCreateTitleView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.layout_common_title, container, false);
        Toolbar toolbar = (Toolbar) child.findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onToolBarSetup(toolbar);
        return child;
    }

    private void showStateView(int state, ViewGroup child) {
        if (!mHasOnCreateContentView && state != mCurrentState) {
            if (mContentContainer.getChildCount() > 0) {
                mContentContainer.removeAllViews();
            }
            mContentContainer.addView(child);
            mCurrentState = state;
        }
    }

    private void showContentView(ViewGroup child) {
        if (!mHasOnCreateContentView) {
            if (mContentContainer.getChildCount() > 0) {
                mContentContainer.removeAllViews();
            }
            mContentContainer.addView(child);
            mHasOnCreateContentView = true;
        }
    }

    private void handlerPreloadInViewPager(Bundle savedInstanceState) {
        if (!isFragmentAlive()) {
            return;
        }
        if (mInViewPager && !isPreloadInViewPager() && !mVisibleToUser) {
            beforeOnCreateStateView(mCurrentState);
            showStateView(STATE_LOADING, onCreatePreloadView(getLayoutInflater(), mContentContainer));
            afterOnCreateStateView(mCurrentState, MESSAGE_PRELOAD_VIEW);
        } else {
            if (mRestoreFragment && t != null) {
                showContentView(onCreateContentView(getLayoutInflater(), mContentContainer, savedInstanceState, t));
                mRestoreFragment = false;
                return;
            }
            if (mBehavior == null) {
                showContentView(onCreateContentView(getLayoutInflater(), mContentContainer, savedInstanceState, null));
            } else {
                switch (mCurrentState) {
                    case STATE_EMPTY:
                    case STATE_FAILURE:
                        beforeOnCreateStateView(mCurrentState);
                        showStateView(mCurrentState, mBehavior.onCreateStateView(mCurrentState));
                        afterOnCreateStateView(mCurrentState, MESSAGE_SET_CURRENT_STATE);
                        break;
                    case STATE_SUCCESS:
                        showContentView(onCreateContentView(getLayoutInflater(), mContentContainer, savedInstanceState, null));
                        break;
                    default:
                        mBehavior.doInBackground(savedInstanceState);
                        break;
                }
            }
        }
    }

    public enum LaunchMode {
        STANDARD, SINGLE
    }


    public interface Behavior<T> {

        public ViewGroup onCreateStateView(int state);

        public void doInBackground(Bundle savedInstanceState);

        public void initBehavior(LayoutInflater inflater, ViewGroup container, Callback<T> callback);

    }

    public interface Callback<T> {

        public void onSuccess(T t, Bundle savedInstanceState);

        public void onState(int state, ViewGroup child, String message);

    }

    public abstract class LocalBehavior implements Behavior<T> {

        private ViewGroup mContainer;
        private Callback<T> mCallback;
        private LayoutInflater mInflater;
        private OnRetryListener mListener;
        private Bundle mSavedInstanceState;
        private LocalStateViewFactory mFactory;

        public LocalBehavior() {
            this.mListener = new OnRetryListener() {
                @Override
                public void onRetry() {
                    if (mCallback != null) {
                        doInBackground(mSavedInstanceState);
                    }
                }
            };
        }

        public final void setEmptyContent(String content) {
            getFactory().setEmptyContent(content);
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

        public final void setFactory(LocalStateViewFactory factory) {
            if (factory != null) {
                this.mFactory = factory;
            }
        }

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
                        mCallback.onState(STATE_LOADING, onCreateStateView(STATE_LOADING), "start onBackgroundLoading");
                        mCallback.onSuccess(onBackgroundLoading(), mSavedInstanceState);
                    } catch (Exception e) {
                        Logger.printStackTrace(e);
                        mCallback.onState(STATE_FAILURE, onCreateStateView(STATE_FAILURE), e.getMessage());
                    }
                }
            }).start();
        }

        @Override
        public final ViewGroup onCreateStateView(int state) {
            StateViewFactory factory = getFactory();
            switch (state) {
                case STATE_EMPTY:
                    return factory.onCreateEmptyView(mInflater, mContainer);
                case STATE_LOADING:
                    return factory.onCreateLoadingView(mInflater, mContainer);
                case STATE_FAILURE:
                    return factory.onCreateFailureView(mInflater, mContainer);
                default:
                    return null;
            }
        }
    }

    public interface OnlineBehavior<T> extends Behavior<T> {

        public String giveMeUrl();

        public T onBackgroundParser(String data);
    }

    public interface OnRetryListener {
        public void onRetry();
    }

    public interface StateViewFactory {

        public ViewGroup onCreateEmptyView(LayoutInflater inflater, ViewGroup container);

        public ViewGroup onCreateLoadingView(LayoutInflater inflater, ViewGroup container);

        public ViewGroup onCreateFailureView(LayoutInflater inflater, ViewGroup container);
    }

    public class LocalStateViewFactory implements StateViewFactory {

        private OnRetryListener mListener;
        private String mEmptyContent = "暂无内容";
        private String mFailureContent = "加载失败,点击屏幕重试!";
        private String mLoadingContent = "正在加载中...";

        public LocalStateViewFactory(OnRetryListener listener) {
            this.mListener = listener;
        }

        public final void setEmptyContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mEmptyContent = content;
            }
        }

        public final void setFailureContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mFailureContent = content;
            }
        }

        public final void setLoadingContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                this.mLoadingContent = content;
            }
        }

        public final OnRetryListener getListener() {
            return mListener;
        }

        public final String getLoadingContent() {
            return mLoadingContent;
        }

        public final String getFailureContent() {
            return mFailureContent;
        }

        public final String getEmptyContent() {
            return mEmptyContent;
        }

        @Override
        public ViewGroup onCreateEmptyView(LayoutInflater inflater, ViewGroup container) {
            ViewGroup child = (ViewGroup) inflater.inflate(R.layout.layout_common_state, container, false);
            TextView textView = (TextView) child.findViewById(R.id.tv_common_content);
            textView.setText(getEmptyContent());
            return child;
        }

        @Override
        public ViewGroup onCreateLoadingView(LayoutInflater inflater, ViewGroup container) {
            ViewGroup child = (ViewGroup) inflater.inflate(R.layout.layout_common_state, container, false);
            TextView textView = (TextView) child.findViewById(R.id.tv_common_content);
            textView.setText(getLoadingContent());
            return child;
        }

        @Override
        public ViewGroup onCreateFailureView(LayoutInflater inflater, ViewGroup container) {
            ViewGroup child = (ViewGroup) inflater.inflate(R.layout.layout_common_state, container, false);
            TextView textView = (TextView) child.findViewById(R.id.tv_common_content);
            textView.setText(getFailureContent());
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getListener().onRetry();
                }
            });
            return child;
        }

    }
}
