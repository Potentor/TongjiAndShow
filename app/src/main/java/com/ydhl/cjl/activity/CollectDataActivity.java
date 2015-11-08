package com.ydhl.cjl.activity;

import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectDataActivity extends ActionBarActivity {

    //每个Item中开始显示的数目
    private final int SHOWNUM=3;


    private String url="http://scene.fjydhl.cn/Scene/Appscene/customdata?uid=";


    @ViewInject(R.id.collect_el_1)
    private ExpandableListView mExpand;


    @ViewInject(R.id.collect_tv_title)
    private TextView tv_title;

    //存储显示的数据集合
    private List<Map<String,String>> mData=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_data);
        ViewUtils.inject(this);
        tv_title.setText(getIntent().getStringExtra("title"));



        loadData();


    }

    private void loadData(){

        int position=getIntent().getIntExtra("form",-1);
        if (position==-1)
        {
            Toast.makeText(this,"目前还无数据",Toast.LENGTH_SHORT).show();
            return;
        }


        ArrayList<Map<String, String>> mData=SingleSceneActivity.getTableInfo(position);
        int size=mData.size();

        for(int i=0;i<size;i++){

            this.mData.add(mData.get(i));

            }


        initDataShow();
    }



    /**
     * 初始化数据控件并显示
     */
    private void initDataShow(){

        if (mData.size()<0)
            return;

        mExpand.setGroupIndicator(null);
        mExpand.setAdapter(new MExpandAdapter());
        mExpand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        mExpand.setDividerHeight(0);

    }


    @OnClick(R.id.collect_iv_back)
    public void back(View v){
        finish();
    }


    private class MExpandAdapter extends BaseExpandableListAdapter{


        private Boolean[] listisExpand;


        public MExpandAdapter(){

            listisExpand=new Boolean[mData.size()];
            for (int i=0;i<mData.size();i++){
                listisExpand[i]=false;
            }

        }

        /**
         *
         * @param position
         * @return
         */
        private View generateGroupView(final int position){

            LinearLayout linear=new LinearLayout(CollectDataActivity.this);
            linear.setOrientation(LinearLayout.VERTICAL);


            Map<String,String> map=mData.get(position);
            Object[] strKeys=map.keySet().toArray();

            if (position!=0) {
                View line = new View(CollectDataActivity.this);
                line.setBackgroundColor(Color.parseColor("#CCCCCC"));
                ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                linear.addView(line, params);

            }

            int show=SHOWNUM-1;
            for (int i=0;i<show;i++){

                View view=LayoutInflater.from(CollectDataActivity.this).inflate(R.layout.item_collect_data,null);
                TextView tv_first=(TextView)view.findViewById(R.id.collect_lv_tv_first);
                TextView tv_second=(TextView)view.findViewById(R.id.collect_lv_tv_second);
                view.findViewById(R.id.collect_lv_iv_more).setVisibility(View.GONE);
                tv_first.setText((String)strKeys[i]);
                tv_second.setText(map.get(strKeys[i]));
                linear.addView(view);

            }


            View view=LayoutInflater.from(CollectDataActivity.this).inflate(R.layout.item_collect_data,null);
            TextView tv_first=(TextView)view.findViewById(R.id.collect_lv_tv_first);
            TextView tv_second=(TextView)view.findViewById(R.id.collect_lv_tv_second);

            tv_first.setText((String)strKeys[SHOWNUM-1]);
            tv_second.setText(map.get(strKeys[SHOWNUM-1]));
            final ImageView iv_more=(ImageView)view.findViewById(R.id.collect_lv_iv_more);


            if (listisExpand[position]){
                iv_more.setVisibility(View.GONE);
            }else {
                iv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       listisExpand[position]=true;

                        //显示
                        mExpand.expandGroup(position);


                    }
                });
            }
            linear.addView(view);


            return linear;
        }


        /**
         * 生成展开项
         * @param group
         * @param position
         * @return
         */
        private View generateChildView(final int group,int position){

            Map<String,String> map=mData.get(group);
            Object[] strKeys=map.keySet().toArray();

            View view=LayoutInflater.from(CollectDataActivity.this).inflate(R.layout.item_collect_data,null);
            TextView tv_first=(TextView)view.findViewById(R.id.collect_lv_tv_first);
            TextView tv_second=(TextView)view.findViewById(R.id.collect_lv_tv_second);

            Log.i("here",""+position+strKeys[position+SHOWNUM]);

            tv_first.setText((String)strKeys[position+SHOWNUM]);
            tv_second.setText(map.get(strKeys[position+SHOWNUM]));
            final ImageView iv_more=(ImageView)view.findViewById(R.id.collect_lv_iv_more);
            iv_more.setVisibility(View.GONE);

            if (position==map.size()-1-SHOWNUM) {

                iv_more.setVisibility(View.VISIBLE);
                iv_more.setImageResource(R.mipmap.ic_donw_arrow);
                iv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listisExpand[group]=false;
                        //叠起
                        mExpand.collapseGroup(group);

                    }
                });
            }

            return view;
        }


        @Override
        public int getGroupCount() {
            return mData.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return mData.get(i).size()-SHOWNUM;
        }

        @Override
        public Object getGroup(int i) {
            return mData.get(i);
        }

        @Override
        public Object getChild(int i, int i2) {

            return mData.get(i).keySet().toArray()[i2+SHOWNUM];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i2) {
            return i2;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            return generateGroupView(i);
        }

        @Override
        public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
            return generateChildView(i,i2);
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return false;
        }
    }




}
