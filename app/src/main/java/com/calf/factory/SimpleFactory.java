package com.calf.factory;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;
import com.calf.fragments.mine.MineMainFragment;
import com.calf.fragments.online.OnlineMainFragment;
import com.calf.fragments.search.SearchMainFragment;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class SimpleFactory {

    public static BaseFragment createMainTabs(int position) {
        BaseFragment f = null;
        switch (position) {
            case 0:
                f = MineMainFragment.newInstance(position);
                break;
            case 1:
                f = OnlineMainFragment.newInstance(position);
                break;
            case 2:
                f = SearchMainFragment.newInstance(position);
                break;
            default:
                f = OnlineMainFragment.newInstance(position);
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
