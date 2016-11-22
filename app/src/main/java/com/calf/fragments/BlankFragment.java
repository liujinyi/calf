package com.calf.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by JinYi Liu on 16-11-22.
 */
public class BlankFragment extends BaseFragment {

    public String mStr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
