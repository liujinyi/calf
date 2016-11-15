package com.calf.player.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.calf.bean.OnlineInfo;
import com.calf.frame.log.Logger;

/**
 * Created by JinYi Liu on 16-11-13.
 */

public class SingleTopActivity extends BaseActivity {


    private OnlineInfo mOnlineInfo;
    private OnlineInfo mTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, mSimpleName + "[onCreate] " + hashCode());
        super.onCreate(savedInstanceState);

        Button btn = new Button(this);
        btn.setText("点我试试");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleTopActivity.this, SingleTopActivity.class));
            }
        });
        setContentView(btn);
    }

    @Override
    protected void onResume() {
        Logger.i(TAG, mSimpleName + "[onResume] " + hashCode());
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Logger.e(TAG, mSimpleName + "[onNewIntent] " + hashCode());
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        Logger.i(TAG, mSimpleName + "[onPause] " + hashCode());
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.i(TAG, mSimpleName + "[onStop] " + hashCode());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Logger.e(TAG, mSimpleName + "[onDestroy] " + hashCode());
        super.onDestroy();
    }

}
