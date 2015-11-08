package com.ydhl.cjl.adapter.SceneShowAcitivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ydhl.cjl.R;
import com.ydhl.cjl.db.entity.SceneContentInfo;
import com.ydhl.cjl.db.entity.SceneMobileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/6.
 */
public class StatisticsAdapter extends BaseAdapter {
    Context mContext;

    private List<SceneContentInfo> contents;
    private LayoutInflater inflater;

    public StatisticsAdapter(Context mContext, List<SceneContentInfo> contents) {
        this.mContext = mContext;
        this.contents = contents;
        inflater = LayoutInflater.from(mContext);


    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        if (view == null) {
            view1 = inflater.inflate(R.layout.item_list_statistics, null);
        } else {
            view1 = view;
        }
        HoldeView holdeView = new HoldeView(view1);
        holdeView.time.setText(contents.get(i).getDate());
        holdeView.clickTimes.setText(contents.get(i).getLink_count()+"");
        holdeView.phoneNum.setText(contents.get(i).getCall_count()+"");
        view1.setTag(holdeView);

        return view1;
    }

    private static class HoldeView {
        TextView time;
        TextView clickTimes;
        TextView phoneNum;

        public HoldeView(View view) {
            time = (TextView) view.findViewById(R.id.tv_time);
            clickTimes = (TextView) view.findViewById(R.id.tv_clickTimes);
            phoneNum = (TextView) view.findViewById(R.id.tv_phoneNum);

        }
    }
    public void setData(List<SceneContentInfo> contents) {
        this.contents = contents;
        this.notifyDataSetChanged();
    }
}
