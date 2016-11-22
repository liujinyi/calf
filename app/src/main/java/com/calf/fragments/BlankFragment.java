package com.calf.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;

/**
 * Created by JinYi Liu on 16-11-22.
 */
public class BlankFragment extends BaseFragment {

    public String mStr;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setGravity(Gravity.CENTER);
        TextView tv = new TextView(getActivity());
        tv.setText(mStr);
        linearLayout.addView(tv);
        return linearLayout;
    }

    @Override
    public String toString() {
        return mStr;
    }

}
