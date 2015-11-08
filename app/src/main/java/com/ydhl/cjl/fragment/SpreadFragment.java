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
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;
import com.ydhl.cjl.activity.MainActivity;
import com.ydhl.cjl.activity.SceneShowActivity;
import com.ydhl.cjl.activity.SingleSceneActivity;
import com.ydhl.cjl.utils.Md5Util;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的推广
 */
public class SpreadFragment extends Fragment implements AdapterView.OnItemClickListener{





    //
    private String url="http://scene.fjydhl.cn/Scene/Appscene/stat?uid=";

    //存储场景展示的数据信息
    List<Map<String,String>> mList=new ArrayList<Map<String,String>>();

    private SimpleAdapter mAdapter;



    private TextView tv_collection;

    private TextView tv_show;

    private TextView tv_summary;

    @ViewInject(R.id.spread_pull_01)
    private PullToRefreshListView pullToRefreshListView;

    private MHandler handler=new MHandler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spread,container,false);
        ViewUtils.inject(this,view);
        initView();
        loadData();
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        pullToRefreshListView.onRefreshComplete();
    }


    private void initView(){
        String[] from=new String[]{"scenes","num"};
        int[] to =new int[]{R.id.spread_lv_tv_scenes,R.id.spread_lv_tv_num};
        mAdapter=new SimpleAdapter(getActivity(),mList,R.layout.item_spread,from,to);
        pullToRefreshListView.setAdapter(mAdapter);
        pullToRefreshListView.setOnItemClickListener(this);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                loadData();
            }
        });

        View view=LayoutInflater.from(this.getActivity()).inflate(R.layout.spread_head,null);
        tv_summary=(TextView)view.findViewById(R.id.spread_tv_summary);
        tv_show=(TextView)view.findViewById(R.id.spread_tv_show);
        tv_collection=(TextView)view.findViewById(R.id.spread_tv_collection);

        pullToRefreshListView.getRefreshableView().addHeaderView(view);

    }


    /**
     * 储存并显示数据
     * @param object
     */
    private void updateData(JSONObject object) {

        try {

            JSONObject data=object.getJSONObject("data");

            if (data==null){
                return;
            }

            JSONObject threeFront=data.getJSONObject("summary");

            tv_collection.setText(threeFront.getString("gather_data_count"));
            tv_summary.setText(threeFront.getString("scene_count"));
            tv_show.setText(threeFront.getString("spread_data_count"));

            JSONArray sceneData=data.getJSONArray("detail");

            int length=sceneData.length();

            //若没有数据，直接返回
            if (length==0){
                Toast.makeText(SpreadFragment.this.getActivity(),"现在还没有数据", Toast.LENGTH_SHORT).show();
                return;

            }
            mList.clear();
            for(int i=0;i<length;i++){

                JSONObject tempObject=sceneData.getJSONObject(i);
                Map<String,String> map01=new HashMap();
                map01.put("scenes",tempObject.getString("scene_name"));
                map01.put("num",tempObject.getString("spread_data_count"));
                map01.put("id",tempObject.getString("scene_id"));
                mList.add(map01);


            }

            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SpreadFragment.this.getActivity(),"error", Toast.LENGTH_SHORT).show();
        }finally {
            handler.sendEmptyMessage(200);
        }






    }


    /**
     * 从服务器拉取场景信息
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
                            int code = object.getInt("code");

                            if (code!=0){
                                Toast.makeText(SpreadFragment.this.getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }else {
                                Log.i("result1","result"+responseInfo.result);
                                Log.i("shen","result:"+responseInfo.result);
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
                        Toast.makeText(SpreadFragment.this.getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(200);
                    }


                });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i-2<0){
            return;

        }

        int num=Integer.parseInt(mList.get(i-2).get("num"));
        if (num<1){
            Toast.makeText(this.getActivity(),"这里还没有数据",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent mIntent= new Intent(SpreadFragment.this.getActivity(), SceneShowActivity.class);
        mIntent.putExtra("id",mList.get(i-2).get("id"));
        SpreadFragment.this.startActivity(mIntent);

    }

    private class MHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==200){

                pullToRefreshListView.onRefreshComplete();
            }
        }
    }



}
