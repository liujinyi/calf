package com.calf.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.calf.factory.SimpleFactory;
import com.calf.fragments.BaseFragment;

import java.util.List;

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

//    @Override
//    public CharSequence getPageTitle(int position) {
//        if (position >= 0){
//            return "更多";
//        }
//        return super.getPageTitle(position);
//    }
}
