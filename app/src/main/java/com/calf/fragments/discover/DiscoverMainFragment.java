package com.calf.fragments.discover;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-30.
 */

public class DiscoverMainFragment extends BaseFragment<String> {

    public static DiscoverMainFragment newInstance() {
        DiscoverMainFragment fragment = new DiscoverMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isDisplayTitleContainer() {
        return false;
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String s) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_discover_main, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        if (TextUtils.isEmpty(s)) {
            textView.setText("发现详情页");
        } else {
            textView.setText(s);
        }
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return child;
    }

}
