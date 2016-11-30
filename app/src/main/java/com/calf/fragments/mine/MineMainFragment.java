package com.calf.fragments.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.TestFragmentNoBehavior;
import com.calf.fragments.base.BaseFragment;
import com.calf.player.R;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class MineMainFragment extends BaseFragment<String> {

    public static MineMainFragment newInstance(int sectionNumber) {
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
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String data) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_mine, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        if (TextUtils.isEmpty(data)) {
            textView.setText("我的详情页");
        } else {
            textView.setText(data);
        }
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragmentManager.showFragment(new TestFragmentNoBehavior());
            }
        });
        return child;
    }

    @Override
    protected Behavior<String> onBehaviorSetup() {
        LocalBehavior behavior = new LocalBehavior() {
            @Override
            public String onBackgroundLoading() throws Exception {
                Thread.sleep(1000);
                return "我的详情页,来自本地行为";
            }
        };
        return behavior;
    }
}
