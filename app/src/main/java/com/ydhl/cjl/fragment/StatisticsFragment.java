package com.ydhl.cjl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;
import com.ydhl.cjl.activity.SingleSceneActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计
 */
public class StatisticsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private String url="http://scene.fjydhl.cn/Scene/Appscene/collect?uid=";

    //数据集合
    private List<Map<String,String>> mList=new ArrayList<>();
    private SimpleAdapter mAdapter;
    private MHandler handler=new MHandler();



    @ViewInject(R.id.statistics_pull_01)
    private PullToRefreshListView pullToRefreshListView;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics,container,false);
        ViewUtils.inject(this,view);
        initView();
        loadData();
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                loadData();
            }
        });

        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        pullToRefreshListView.onRefreshComplete();
    }


    /**
     * 初始化组件
     */
    private void initView(){

        String[] from=new String[]{"scenes","data"};
        int[] to =new int[]{R.id.statistics_lv_tv_scenes,R.id.statistics_lv_data};
        mAdapter=new SimpleAdapter(getActivity(),mList,R.layout.item_statistics,from,to);
        pullToRefreshListView.setAdapter(mAdapter);
        pullToRefreshListView.setOnItemClickListener(this);
        ListView mListView=pullToRefreshListView.getRefreshableView();
        View view =LayoutInflater.from(this.getActivity()).inflate(R.layout.statistics_head,null);
        mListView.addHeaderView(view);


    }


    /**
     * 更新数据
     * @param object
     */
    private void updateData(JSONObject object){

        try {
            JSONArray array=object.getJSONArray("data");

            int size=array.length();

            if (size<1){
                Toast.makeText(StatisticsFragment.this.getActivity(), "这儿现在还没有数据", Toast.LENGTH_SHORT).show();
                return;
            }


            //清空原有数据
            mList.clear();

            for(int i=0;i<size;i++){

                JSONObject temObject=array.getJSONObject(i);

                Map<String,String> mMap=new HashMap<>();
                mMap.put("scenes",temObject.getString("scene_name"));
                mMap.put("data",temObject.getString("gather_data_count"));
                mMap.put("id",temObject.getString("scene_id"));
                mList.add(mMap);


            }

            mAdapter.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            handler.sendEmptyMessage(200);
        }

    }


    /**
     * 从服务器加载数据
     */
    private void loadData(){

        String mUid=(String)((CJLApplication)getActivity().getApplication()).getAppData(CJLApplication.KEY_UID);
        String str_get=url+mUid;

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, str_get,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            Log.i("statistics", responseInfo.result);
                            int code = object.getInt("code");

                            if (code!=0){
                                Toast.makeText(StatisticsFragment.this.getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }else {

                                updateData(object);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            handler.sendEmptyMessage(200);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(StatisticsFragment.this.getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(200);
                    }


                });


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int position=i-2;
        if (position<0){
            return;
        }
        String id=mList.get(position).get("id");
        String name=mList.get(position).get("scenes");
        int count=Integer.parseInt(mList.get(position).get("data"));

        if (count<1){
            Toast.makeText(this.getActivity(),"还没有数据", Toast.LENGTH_SHORT).show();
            return;
        }




        Intent mIntent=new Intent(StatisticsFragment.this.getActivity(), SingleSceneActivity.class);
        mIntent.putExtra("id",id);
        mIntent.putExtra("name",name);
        StatisticsFragment.this.getActivity().startActivity(mIntent);


    }


    private class MHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==200) {
                pullToRefreshListView.onRefreshComplete();
            }
        }
    }


}
