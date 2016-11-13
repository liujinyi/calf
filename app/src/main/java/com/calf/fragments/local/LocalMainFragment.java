package com.calf.fragments.local;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.R;
import com.calf.fragments.BaseFragment;
import com.calf.fragments.search.SearchMainFragment;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class LocalMainFragment extends BaseFragment {

    public static LocalMainFragment newInstance(int sectionNumber) {
        LocalMainFragment fragment = new LocalMainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

}
