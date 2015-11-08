package com.ydhl.cjl.db.entity;

/**
 * Created by Administrator on 2015/5/8.
 */
public class SceneSummaryInfo {
    String date;
    int spread_count;
    int gather_data_count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "SceneSummaryInfo{" +
                "date='" + date + '\'' +
                ", spread_count=" + spread_count +
                ", gather_data_count=" + gather_data_count +
                '}';
    }
}
