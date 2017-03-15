package com.calf.fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.calf.adapters.MainActivityTabAdapter;
import com.calf.fragments.base.BaseFragment;
import com.calf.fragments.search.SearchMainFragment;
import com.calf.mode.ModeMgr;
import com.calf.player.R;
import com.calf.player.activitys.MainActivity;
import com.calf.player.manager.MainFragmentManager;

/**
 * Created by JinYi Liu on 17-03-15.
 */
public class MainFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivity mActivity;
    private DrawerLayout mDrawerLayout;

    public static MainFragment newInstance() {
        MainFragment f = new MainFragment();
        return f;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                MainFragmentManager.showFragment(SearchMainFragment.newInstance());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        if (!ModeMgr.getPlaybackMgr().isReady()) {
            Toast.makeText(getContext(), "解码器加载失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected boolean isDisplayTitleContainer() {
        return false;
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Object o) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        Toolbar toolbar = (Toolbar) child.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mActivity.setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) child.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) child.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainActivityTabAdapter tabAdapter = new MainActivityTabAdapter(mActivity.getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) child.findViewById(R.id.container);
        mViewPager.setAdapter(tabAdapter);

        TabLayout tabLayout = (TabLayout) child.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return child;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
