package com.calf.fragments.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.base.BackgroundBehavior;
import com.calf.fragments.base.BaseFragment;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class MineMainFragment extends BaseFragment<String> {

    public static MineMainFragment newInstance() {
        MineMainFragment fragment = new MineMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isShowTitleContainer() {
        return false;
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String s) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_mine_main, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        if (TextUtils.isEmpty(s)) {
            textView.setText("我的详情页");
        } else {
            textView.setText(s);
        }
        return child;
    }

    @Override
    protected Behavior<String> onBehaviorSetup() {
        BackgroundBehavior behavior = new BackgroundBehavior() {
            @Override
            protected String onBackgroundLoading() throws Exception {
                Thread.sleep(1000);
                return "我的详情页,来自本地行为";
            }
        };
        behavior.setLoadingContent("本地加载中...");
        return behavior;
    }

}
