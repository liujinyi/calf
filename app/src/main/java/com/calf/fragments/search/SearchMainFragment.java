package com.calf.fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;
import com.calf.fragments.base.TabFragment;
import com.calf.player.R;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class SearchMainFragment extends BaseFragment<String> {

    public static SearchMainFragment newInstance() {
        SearchMainFragment fragment = new SearchMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String s) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_search_main, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        textView.setText("搜索详情页");
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragmentManager.showFragment(new TabFragment());
            }
        });
        return child;
    }

}
