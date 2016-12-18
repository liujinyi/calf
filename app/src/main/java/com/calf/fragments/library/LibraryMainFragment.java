package com.calf.fragments.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class LibraryMainFragment extends BaseFragment {

    public static LibraryMainFragment newInstance() {
        LibraryMainFragment fragment = new LibraryMainFragment();
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
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_library_main, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString(getSimpleName()));
        } else {
            textView.setText("乐库详情页");
        }
        return child;
    }

}
