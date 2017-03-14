package com.calf.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JinYi Liu on 17-2-9.
 */

public class MusicInfo extends OnlineInfo implements Parcelable{

    private String album;
    private int duration;
    private long payFlag;
    private String mvFlag;
    private String artist;
    private String mvQuality;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(album);
        dest.writeInt(duration);
        dest.writeLong(payFlag);
        dest.writeString(mvFlag);
        dest.writeString(artist);
        dest.writeString(mvQuality);
        dest.writeLong(getId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.album = in.readString();
            musicInfo.duration = in.readInt();
            musicInfo.payFlag = in.readLong();
            musicInfo.mvFlag = in.readString();
            musicInfo.artist = in.readString();
            musicInfo.mvQuality = in.readString();
            musicInfo.setId(in.readLong());
            return musicInfo;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(long payFlag) {
        this.payFlag = payFlag;
    }

    public String getMvFlag() {
        return mvFlag;
    }

    public void setMvFlag(String mvFlag) {
        this.mvFlag = mvFlag;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMvQuality() {
        return mvQuality;
    }

    public void setMvQuality(String mvQuality) {
        this.mvQuality = mvQuality;
    }

    @Override
    public String toString() {
        return "{" + getName() + "}";
    }
}
