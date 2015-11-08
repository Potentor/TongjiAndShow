package com.ydhl.cjl.adapter.ClientListActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ydhl.cjl.R;
import com.ydhl.cjl.db.entity.ClientInfo;
import com.ydhl.cjl.db.entity.SceneMobileInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/5/7.
 */
public class ClientListAdapter extends BaseAdapter{
    Context mContext;
    List<ClientInfo> clients;
    LayoutInflater inflater;

    public ClientListAdapter(Context mContext, List<ClientInfo> clients) {
        this.mContext = mContext;
        this.clients = clients;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public Object getItem(int i) {
        return clients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = null;
        if (view!=null){
            view1 = view;
        }else {
            view1 = inflater.inflate(R.layout.item_user_list,null);
        }
        HolderView holderView = new HolderView(view1);
        holderView.name.setText(clients.get(i).getC_name());
        view1.setTag(holderView);
        return view1;
    }

    public static class HolderView{
        TextView name;

        public HolderView(View view) {
            this.name = (TextView) view.findViewById(R.id.tv_name);
        }
    }


    public void setData(List<ClientInfo> clients) {
        this.clients = clients;
        this.notifyDataSetChanged();
    }
}
