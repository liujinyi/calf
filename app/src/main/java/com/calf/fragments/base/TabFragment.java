package com.calf.fragments.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.calf.fragments.BlankFragment;
import com.calf.fragments.library.LibraryRecommendFragment;
import com.calf.player.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JinYi Liu on 16-11-26.
 */

public class TabFragment extends BaseFragment {


    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Object o) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_tab, container, false);
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) child.findViewById(R.id.container);
        mViewPager.setAdapter(tabAdapter);
        TabLayout tabLayout = (TabLayout) child.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return child;
    }

    private class TabAdapter extends FragmentPagerAdapter {

        private List<BaseFragment> mFragments;

        public TabAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>(6);
            mFragments.add(new LibraryRecommendFragment());
            mFragments.add(BlankFragment.newInstance("歌手详情页"));
            mFragments.add(BlankFragment.newInstance("MV详情页"));
            mFragments.add(BlankFragment.newInstance("排行榜详情页"));
            mFragments.add(BlankFragment.newInstance("专区详情页"));
            mFragments.add(BlankFragment.newInstance("评论详情页"));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String str = "更多";
            switch (position) {
                case 0:
                    str = "推荐";
                    break;
                case 1:
                    str = "歌手";
                    break;
                case 2:
                    str = "MV";
                    break;
                case 3:
                    str = "排行榜";
                    break;
                case 4:
                    str = "专区";
                    break;
                case 5:
                    str = "评论";
                    break;
            }
            return str;
        }
    }

}
