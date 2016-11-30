package com.calf.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.calf.fragments.base.BaseFragment;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-22.
 */
public class BlankFragment extends BaseFragment {

    public String mStr;

    public static BlankFragment newInstance(String str) {
        BlankFragment f = new BlankFragment();
        f.mStr = str;
        return f;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getSimpleName(), "我是销毁后保持的字符串");
        Toast.makeText(getActivity(), "BlankFragment [onSaveInstanceState]", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_blank_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Object o) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_blank, container, false);
        TextView textView = (TextView) child.findViewById(R.id.tv_blank);
        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString(getSimpleName()));
        } else if (TextUtils.isEmpty(mStr)) {
            textView.setText(getSimpleName());
        } else {
            textView.setText(mStr);
        }
        return child;
    }

    @Override
    public String toString() {
        return mStr;
    }

}
