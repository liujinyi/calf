package com.calf.adapters;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.calf.bean.MusicInfo;
import com.calf.frame.http.HttpSession;
import com.calf.frame.log.Logger;
import com.calf.frame.message.MessageManager;
import com.calf.player.R;
import com.calf.utils.UrlFactory;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by JinYi Liu on 2017/2/9.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private MediaPlayer mMediaPlayer;
    private List<MusicInfo> mMusicInfos;

    public MusicAdapter(List<MusicInfo> onlineInfos) {
        this.mMusicInfos = onlineInfos;
        this.mMediaPlayer = new MediaPlayer();
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
                        HttpSession session = new HttpSession();
                        Response response = session.get(UrlFactory.createConvertUrl(musicInfo.getId()));
                        if (response != null && response.isSuccessful()) {
                            try {
                                final String data = response.body().string();
                                String url = data.split("\r\n")[2].substring(4);
                                mMediaPlayer.reset();
                                mMediaPlayer.setDataSource(url);
                                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mMediaPlayer.prepare();
                                mMediaPlayer.start();
                                MessageManager.asyncPost(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(holder.mRootView.getContext(), data, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (IOException e) {
                                Logger.printStackTrace(e);
                            }
                        }
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
