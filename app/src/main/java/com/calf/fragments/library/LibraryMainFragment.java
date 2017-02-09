package com.calf.fragments.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.fragments.base.BaseFragment;
import com.calf.fragments.base.HttpBehavior;
import com.calf.player.R;
import com.calf.utils.UrlFactory;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class LibraryMainFragment extends BaseFragment<String> {

    public static LibraryMainFragment newInstance() {
        LibraryMainFragment fragment = new LibraryMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isDisplayTitleContainer() {
        return false;
    }

    @Override
    protected ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String s) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_library_main, container, false);
        TextView textView = (TextView) child.findViewById(R.id.section_label);
        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString(getSimpleName()));
        } else {
            textView.setText(s);
        }
        return child;
    }

    @Override
    protected Behavior<String> onBehaviorSetup() {
        HttpBehavior<String> behavior = new HttpBehavior() {
            @Override
            public String onBackgroundParser(String data) {
                return data;
            }

            @Override
            protected String giveMeUrl(int start, int count) {
                return UrlFactory.createAlbumMusic(574, start, count);
            }
        };
        //behavior.getCacheParameter().setDecoder(null);
        return behavior;
    }

}
