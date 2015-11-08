package com.ydhl.cjl.adapter.SceneShowAcitivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ydhl.cjl.R;
import com.ydhl.cjl.db.entity.SceneSummaryInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/5/6.
 */
public class OverviewAdapter extends BaseAdapter {


    Context mContext;

    private List<SceneSummaryInfo> summaries;
    private LayoutInflater inflater;

    public OverviewAdapter(Context mContext, List<SceneSummaryInfo> summaries) {
        this.mContext = mContext;
        this.summaries = summaries;
        inflater = LayoutInflater.from(mContext);


    }

    @Override
    public int getCount() {
        return summaries.size();
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
            view1 = inflater.inflate(R.layout.item_list_overview, null);
        } else {
            view1 = view;
        }
        HoldeView holdeView = new HoldeView(view1);
        holdeView.time.setText(summaries.get(i).getDate());
        holdeView.times.setText(summaries.get(i).getSpread_count() + "");
        holdeView.phoneNum.setText(summaries.get(i).getGather_data_count() + "");
        view1.setTag(holdeView);

        return view1;
    }

    private static class HoldeView {
        TextView time;
        TextView times;
        TextView phoneNum;

        public HoldeView(View view) {
            time = (TextView) view.findViewById(R.id.tv_time);
            times = (TextView) view.findViewById(R.id.tv_times);
            phoneNum = (TextView) view.findViewById(R.id.tv_data);

        }
    }

    public void setData(List<SceneSummaryInfo> summaries) {
        this.summaries = summaries;
        this.notifyDataSetChanged();
    }


}
