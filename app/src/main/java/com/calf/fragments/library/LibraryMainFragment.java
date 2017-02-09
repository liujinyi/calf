package com.calf.fragments.library;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.calf.adapters.MusicAdapter;
import com.calf.bean.MusicInfo;
import com.calf.bean.OnlineInfo;
import com.calf.bean.RootInfo;
import com.calf.fragments.base.HttpBehavior;
import com.calf.fragments.base.RecyclerViewFragment;
import com.calf.utils.LibraryParser;
import com.calf.utils.UrlFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JinYi Liu on 16-11-12.
 */

public class LibraryMainFragment extends RecyclerViewFragment<RootInfo> {

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
    protected void onRecyclerViewSetAdapter(RecyclerView recyclerView, RootInfo rootInfo) {
        List<OnlineInfo> onlineInfos = rootInfo.getFirstSection().getOnlineInfos();
        List<MusicInfo> musicInfos = new ArrayList<>(onlineInfos.size());
        for (OnlineInfo info : onlineInfos) {
            if (info instanceof MusicInfo) {
                musicInfos.add((MusicInfo) info);
            }
        }
        MusicAdapter adapter = new MusicAdapter(musicInfos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Behavior<RootInfo> onBehaviorSetup() {
        HttpBehavior<RootInfo> behavior = new HttpBehavior() {
            @Override
            public RootInfo onBackgroundParser(String data) throws Exception {
                return LibraryParser.parse(getContext(), data);
            }

            @Override
            protected String giveMeUrl(int start, int count) {
                return UrlFactory.createAlbumMusic(574, start, count);
            }
        };
        return behavior;
    }

}
