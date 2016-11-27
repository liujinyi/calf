package com.calf.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.calf.factory.SimpleFactory;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class MainActivityTabAdapter extends FragmentPagerAdapter {

    public MainActivityTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SimpleFactory.createMainTabs(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String str = "更多";
        switch (position) {
            case 0:
                str = "我的";
                break;
            case 1:
                str = "乐库";
                break;
            case 2:
                str = "发现";
                break;
        }
        return str;
    }
}
