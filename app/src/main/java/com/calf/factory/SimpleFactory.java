package com.calf.factory;

import com.calf.fragments.BaseFragment;
import com.calf.fragments.local.LocalMainFragment;
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
                f = OnlineMainFragment.newInstance(position);
                break;
            case 1:
                f = LocalMainFragment.newInstance(position);
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
