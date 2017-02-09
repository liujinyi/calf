package com.calf.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JinYi Liu on 17-2-9.
 */

public class SectionInfo {

    private int id;
    private int start;
    private int count;
    private int total;
    private String type;
    private List<OnlineInfo> onlineInfos = new ArrayList<OnlineInfo>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OnlineInfo> getOnlineInfos() {
        return onlineInfos;
    }

    public int getOnlineInfoSize() {
        return onlineInfos.size();
    }

    public void addOnlineInfo(OnlineInfo onlineInfo) {
        if (onlineInfos != null) {
            this.onlineInfos.add(onlineInfo);
        }
    }

    public void addOnlineInfos(List<OnlineInfo> onlineInfos) {
        if (onlineInfos != null) {
            this.onlineInfos.addAll(onlineInfos);
        }
    }

}
