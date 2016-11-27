package com.calf.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;

/**
 * Created by JinYi Liu on 16-11-27.
 */

public class TestFragmentNoBehavior extends BaseFragment<String> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitState(STATE_EMPTY);
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String s) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setBackgroundColor(Color.WHITE);
        TextView textView = new TextView(getActivity());
        if (TextUtils.isEmpty(s)) {
            textView.setText(getSimpleName());
        } else {
            textView.setText(s);
        }
        linearLayout.addView(textView);
        return linearLayout;
    }

    @Override
    protected Behavior onBehaviorSetup() {
        LocalBehavior behavior = new LocalBehavior() {
            @Override
            public String onBackgroundLoading() throws Exception {
                Thread.sleep(1000);
                Object o = null;
                o.equals("");
                return "我是流氓我是谁";
            }
        };
        behavior.setLoadingContent("自定义Loading....");
        return behavior;
    }

    @Override
    protected void beforeOnCreateStateView(int state) {
        super.beforeOnCreateStateView(state);
    }

    @Override
    protected void afterOnCreateStateView(int state, String message) {
        super.afterOnCreateStateView(state, message);
    }
}
