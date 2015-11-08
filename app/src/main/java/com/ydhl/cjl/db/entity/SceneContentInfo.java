package com.ydhl.cjl.db.entity;

/**
 * Created by Administrator on 2015/5/8.
 */
public class SceneContentInfo {
    String date;
    int link_count;
    int call_count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLink_count() {
        return link_count;
    }

    public void setLink_count(int link_count) {
        this.link_count = link_count;
    }

    public int getCall_count() {
        return call_count;
    }

    public void setCall_count(int call_count) {
        this.call_count = call_count;
    }

    @Override
    public String toString() {
        return "SceneContentInfo{" +
                "date='" + date + '\'' +
                ", link_count=" + link_count +
                ", call_count=" + call_count +
                '}';
    }
}
