package com.calf.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.calf.fragments.base.BaseFragment;
import com.calf.frame.message.MessageManager;
import com.calf.mode.ModeMgr;
import com.calf.mode.PlaybackMgr;
import com.calf.player.Init;
import com.calf.player.R;
import com.calf.player.activitys.MainActivity;

/**
 * Created by JinYi Liu on 17-03-15.
 */
public class EntranceFragment extends BaseFragment {

    private static final long ENTRANCE_BG_DISPLAY = 3000L;

    public static EntranceFragment newInstance() {
        EntranceFragment f = new EntranceFragment();
        return f;
    }

    @Override
    protected boolean isDisplayTitleContainer() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new EntranceTask()).start();
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Object o) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_entrance, container, false);
        ImageView imageView = (ImageView) child.findViewById(R.id.iv_entrance);
        imageView.setImageResource(R.drawable.bg_entrance);
        return child;
    }

    private class EntranceTask extends BaseTask {

        @Override
        public void run() {
            long s = System.currentTimeMillis();
            MainActivity activity = (MainActivity) getActivity();
            Init.initInMainActivity(activity);
            ModeMgr.init(activity);
            ModeMgr.loadMode(PlaybackMgr.class);
            long delay = ENTRANCE_BG_DISPLAY - (System.currentTimeMillis() - s);
            MessageManager.postDelayed(delay >= 0 ? delay : 0, new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_activity_root, MainFragment.newInstance());
                    transaction.commitAllowingStateLoss();
                }
            });

        }

    }
}
