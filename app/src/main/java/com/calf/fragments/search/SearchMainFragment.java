package com.calf.fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.R;
import com.calf.fragments.base.BaseFragment;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class SearchMainFragment extends BaseFragment {

    public static SearchMainFragment newInstance(int sectionNumber) {
        SearchMainFragment fragment = new SearchMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isShowTitleContainer() {
        return false;
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Object o) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        textView.setText("发现详情页");
        return child;
    }

}
