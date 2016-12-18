package com.calf.fragments.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.bean.OnlineInfo;
import com.calf.fragments.base.BaseFragment;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class LibraryRecommendFragment extends BaseFragment<OnlineInfo> {

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, OnlineInfo o) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_library_recommend, container, false);
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

}
