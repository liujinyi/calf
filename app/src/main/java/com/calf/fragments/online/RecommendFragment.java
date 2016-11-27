package com.calf.fragments.online;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.R;
import com.calf.bean.OnlineInfo;
import com.calf.fragments.base.BaseFragment;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class RecommendFragment extends BaseFragment<OnlineInfo> {

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, OnlineInfo o) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_recommend, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString(getSimpleName()));
        } else {
            textView.setText("推荐详情页");
        }
        return child;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getSimpleName(), "销毁后重新创建");
    }

    @Override
    protected Behavior onBehaviorSetup() {
        LocalBehavior behavior = new LocalBehavior() {
            @Override
            public OnlineInfo onBackgroundLoading() throws Exception {
                Thread.sleep(5000);
                return null;
            }
        };
        behavior.setLoadingContent("自定义Loading .... ");
        return behavior;
    }

}
