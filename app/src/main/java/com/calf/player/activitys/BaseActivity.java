package com.calf.player.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "calf";

    protected String mSimpleName;

    public BaseActivity() {
        mSimpleName = getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
