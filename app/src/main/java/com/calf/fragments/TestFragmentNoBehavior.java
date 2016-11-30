package com.calf.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.calf.fragments.base.BaseFragment;
import com.calf.frame.message.MessageManager;
import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-11-27.
 */

public class TestFragmentNoBehavior extends BaseFragment<String> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_blank_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String s) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setBackgroundColor(Color.WHITE);
        TextView textView = new TextView(getActivity());
        if (TextUtils.isEmpty(s)) {
            textView.setText(getSimpleName());
        } else {
            textView.setText(s);
        }
        linearLayout.addView(textView);
        return linearLayout;
    }

    @Override
    protected Behavior onBehaviorSetup() {
        LocalBehavior behavior = new LocalBehavior() {
            @Override
            public String onBackgroundLoading() throws Exception {
                Thread.sleep(1000);
                return "我是流氓我是谁";
            }
        };
        behavior.getListener().onRetry();
        behavior.setLoadingContent("自定义Loading....");
        return behavior;
    }

    @Override
    protected void afterOnCreateContentView(String s) {
        // ... do some thing
        showEmptyView();

        MessageManager.postDelayed(3000, new Runnable() {
            @Override
            public void run() {
                removeEmptyView();
            }
        });
    }
}
