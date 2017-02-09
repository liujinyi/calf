package com.calf.bean;

/**
 * Created by JinYi Liu on 17-2-9.
 */

public class MusicInfo extends OnlineInfo {

    private String album;
    private int duration;
    private long payFlag;
    private String mvFlag;
    private String artist;
    private String mvQuality;

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
