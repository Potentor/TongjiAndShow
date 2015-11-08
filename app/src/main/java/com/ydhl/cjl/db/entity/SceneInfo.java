package com.ydhl.cjl.db.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/5/8.
 */
public class SceneInfo {
    String thumd;
    int spread_count;
    int gather_data_count;
    List<SceneContentInfo> contents;
    List<SceneMobileInfo> mobiles;
    List<SceneSummaryInfo> summaries;

    public String getThumd() {
        return thumd;
    }

    public void setThumd(String thumd) {
        this.thumd = thumd;
    }

    public int getSpread_count() {
        return spread_count;
    }

    public void setSpread_count(int spread_count) {
        this.spread_count = spread_count;
    }

    public int getGather_data_count() {
        return gather_data_count;
    }

    public void setGather_data_count(int gather_data_count) {
        this.gather_data_count = gather_data_count;
    }

    public List<SceneContentInfo> getContents() {
        return contents;
    }

    public void setContents(List<SceneContentInfo> contents) {
        this.contents = contents;
    }

    public List<SceneMobileInfo> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<SceneMobileInfo> mobiles) {
        this.mobiles = mobiles;
    }

    public List<SceneSummaryInfo> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<SceneSummaryInfo> summaries) {
        this.summaries = summaries;
    }

    @Override
    public String toString() {
        return "SceneInfo{" +
                "thumd='" + thumd + '\'' +
                ", spread_count=" + spread_count +
                ", gather_data_count=" + gather_data_count +
                ", contents=" + contents +
                ", mobiles=" + mobiles +
                ", summaries=" + summaries +
                '}';
    }
}
