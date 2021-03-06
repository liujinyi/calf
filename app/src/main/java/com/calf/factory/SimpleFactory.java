package com.calf.factory;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;
import com.calf.fragments.discover.DiscoverMainFragment;
import com.calf.fragments.mine.MineMainFragment;
import com.calf.fragments.library.LibraryMainFragment;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class SimpleFactory {

    public static BaseFragment createMainTabs(int position) {
        BaseFragment f = null;
        switch (position) {
            case 0:
                f = MineMainFragment.newInstance();
                break;
            case 1:
                f = LibraryMainFragment.newInstance();
                break;
            case 2:
                f = DiscoverMainFragment.newInstance();
                break;
            default:
                break;
        }
        return f;
    }

    public static ViewGroup createStateView(LayoutInflater inflater, ViewGroup container, String content) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.layout_common_state, container, false);
        TextView textView = (TextView) child.findViewById(R.id.tv_common_content);
        textView.setText(content);
        return child;
    }
}
