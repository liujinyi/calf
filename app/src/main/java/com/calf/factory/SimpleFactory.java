package com.calf.factory;

import com.calf.fragments.base.BaseFragment;
import com.calf.fragments.mine.MineMainFragment;
import com.calf.fragments.online.OnlineMainFragment;
import com.calf.fragments.search.SearchMainFragment;

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
}
