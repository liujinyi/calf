package com.calf.player.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.calf.R;
import com.calf.adapters.MainActivityTabAdapter;
import com.calf.fragments.base.BaseFragment;
import com.calf.frame.log.Logger;
import com.calf.player.manager.MainFragmentManager;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, mSimpleName + "[onCreate] ");
        super.onCreate(savedInstanceState);
        MainFragmentManager.init(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainActivityTabAdapter mSectionsPagerAdapter = new MainActivityTabAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        int count = tabLayout.getChildCount();
//        tabLayout.getTabAt(0).setIcon(R.drawable.selector_tab_online);
//        tabLayout.getTabAt(1).setIcon(R.drawable.selector_tab_local);
//        tabLayout.getTabAt(2).setIcon(R.drawable.selector_tab_friend);

//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        FrameLayout f0 = (FrameLayout) inflater.inflate(R.layout.item_main_tab, tabLayout, false);
//        ImageView imageView0 = (ImageView) f0.findViewById(R.id.icon);
//        imageView0.setImageResource(R.drawable.selector_tab_online);
//        tabLayout.getTabAt(0).setCustomView(imageView0);
//
//        FrameLayout f1 = (FrameLayout) inflater.inflate(R.layout.item_main_tab, tabLayout, false);
//        ImageView imageView1 = (ImageView) f1.findViewById(R.id.icon);
//        imageView1.setImageResource(R.drawable.selector_tab_local);
//        tabLayout.getTabAt(1).setCustomView(imageView1);
//
//        FrameLayout f2 = (FrameLayout) inflater.inflate(R.layout.item_main_tab, tabLayout, false);
//        ImageView imageView2 = (ImageView) f2.findViewById(R.id.icon);
//        imageView2.setImageResource(R.drawable.selector_tab_friend);
//        tabLayout.getTabAt(2).setCustomView(imageView2);

//        for (int i = 0; i < 6; i++) {
//            BlankFragment f = new BlankFragment();
//            f.mStr = "BlankFragment " + i;
//            MainFragmentManager.showFragment(f);
//        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            BaseFragment f = MainFragmentManager.getTopFragment();
            if (f == null) {
                super.onBackPressed();
            } else {
                f.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SingleTopActivity.class));
                break;
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        Logger.i(TAG, mSimpleName + "[onResume] ");
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Logger.e(TAG, mSimpleName + "[onNewIntent] ");
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        Logger.i(TAG, mSimpleName + "[onPause] ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.i(TAG, mSimpleName + "[onStop] ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Logger.e(TAG, mSimpleName + "[onDestroy] ");
        super.onDestroy();
        MainFragmentManager.release();
    }

}
