package com.calf.fragments.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.calf.player.R;

/**
 * Created by JinYi Liu on 16-12-31.
 */

public abstract class RecyclerViewFragment<T> extends BaseFragment<T> {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected final ViewGroup onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, T t) {
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.fragment_recycler_view, container, false);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) child.findViewById(R.id.swipe_refresh_layout);
        RecyclerView recyclerView = (RecyclerView) child.findViewById(R.id.recycler_view);
        onRecyclerViewSetAdapter(recyclerView, t);
        return child;
    }

    protected abstract void onRecyclerViewSetAdapter(RecyclerView recyclerView, T t);

    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

}
