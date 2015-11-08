package com.ydhl.cjl.db.entity;

/**
 * Created by Administrator on 2015/5/8.
 */
public class SceneMobileInfo {
    String date;
    int wx_friends_count;
    int wx_chat_content;
    int wx_group_count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWx_friends_count() {
        return wx_friends_count;
    }

    public void setWx_friends_count(int wx_friends_count) {
        this.wx_friends_count = wx_friends_count;
    }

    public int getWx_chat_content() {
        return wx_chat_content;
    }

    public void setWx_chat_content(int wx_chat_content) {
        this.wx_chat_content = wx_chat_content;
    }

    public int getWx_group_count() {
        return wx_group_count;
    }

    public void setWx_group_count(int wx_group_count) {
        this.wx_group_count = wx_group_count;
    }

    @Override
    public String toString() {
        return "SceneMobileInfo{" +
                "date='" + date + '\'' +
                ", wx_friends_count=" + wx_friends_count +
                ", wx_chat_content=" + wx_chat_content +
                ", wx_group_count=" + wx_group_count +
                '}';
    }
}
