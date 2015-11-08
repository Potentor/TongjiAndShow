package com.ydhl.cjl.adapter.SceneShowAcitivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ydhl.cjl.R;
import com.ydhl.cjl.db.entity.SceneMobileInfo;
import com.ydhl.cjl.db.entity.SceneSummaryInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/6.
 */
public class MobileAdapter extends BaseAdapter{

    Context mContext;

    private List<SceneMobileInfo> mobiles;
    private LayoutInflater inflater;

    public MobileAdapter(Context mContext, List<SceneMobileInfo> mobiles) {
        this.mContext = mContext;
        this.mobiles = mobiles;
        inflater = LayoutInflater.from(mContext);


    }


    @Override
    public int getCount() {
        return mobiles.size();
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
            view1 = inflater.inflate(R.layout.item_list_mobile, null);
        } else {
            view1 = view;
        }
        HoldeView holdeView = new HoldeView(view1);
        holdeView.time.setText(mobiles.get(i).getDate());
        holdeView.tv_circle.setText(mobiles.get(i).getWx_friends_count()+"");
        holdeView.tv_group.setText(mobiles.get(i).getWx_chat_content()+"");
        holdeView.tv_weChatFriend.setText(mobiles.get(i).getWx_group_count()+"");
        view1.setTag(holdeView);

        return view1;
    }

    private static class HoldeView {
        TextView time;
        TextView tv_circle;
        TextView tv_group;
        TextView tv_weChatFriend;

        public HoldeView(View view) {
            time = (TextView) view.findViewById(R.id.tv_time);
            tv_circle = (TextView) view.findViewById(R.id.tv_circle);
            tv_group = (TextView) view.findViewById(R.id.tv_group);
            tv_weChatFriend = (TextView) view.findViewById(R.id.tv_weChatFriend);

        }
    }
    public void setData(List<SceneMobileInfo> mobiles) {
        this.mobiles = mobiles;
        this.notifyDataSetChanged();
    }
}
