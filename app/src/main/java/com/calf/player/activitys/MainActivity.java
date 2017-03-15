package com.calf.player.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;

import com.calf.fragments.EntranceFragment;
import com.calf.fragments.base.BaseFragment;
import com.calf.frame.log.Logger;
import com.calf.player.R;
import com.calf.player.manager.MainFragmentManager;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, mSimpleName + "[onCreate] ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_activity_root, EntranceFragment.newInstance());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            BaseFragment f = MainFragmentManager.getTopFragment();
            if (f == null) {
                moveTaskToBack(true);
            } else {
                f.onBackPressed();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }


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
