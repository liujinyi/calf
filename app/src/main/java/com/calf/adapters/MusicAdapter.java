package com.calf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calf.bean.MusicInfo;
import com.calf.mode.ModeMgr;
import com.calf.mode.PlaybackMgr;
import com.calf.player.R;

import java.util.List;

/**
 * Created by JinYi Liu on 2017/2/9.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<MusicInfo> mMusicInfos;

    public MusicAdapter(List<MusicInfo> onlineInfos) {
        this.mMusicInfos = onlineInfos;
    }

    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.adapter_music, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final MusicAdapter.ViewHolder holder, int position) {
        final MusicInfo musicInfo = mMusicInfos.get(position);
        holder.mTitleTextView.setText(musicInfo.getName());
        holder.mDescTextView.setText(musicInfo.getArtist() + "-" + musicInfo.getAlbum());
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ModeMgr.getPlaybackMgr().play(musicInfo);
                    }
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMusicInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            this.mRootView = itemView;
            this.mDescTextView = (TextView) itemView.findViewById(R.id.tv_music_desc);
            this.mTitleTextView = (TextView) itemView.findViewById(R.id.tv_music_title);
        }

        public View mRootView;
        public TextView mDescTextView;
        public TextView mTitleTextView;

    }

}
