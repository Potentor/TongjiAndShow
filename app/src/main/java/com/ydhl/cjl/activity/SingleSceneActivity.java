package com.ydhl.cjl.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ydhl.cjl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SingleSceneActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {


    //所有表单的用户数据
    private static ArrayList<SingleSceneData> allTableData=new ArrayList<>();


    /**
     * 获取对应表单的用户信息
     * @param position
     * @return
     */
    public static ArrayList<Map<String, String>> getTableInfo(int position){

        return allTableData.get(position).getTableData();

    }



    private String url="http://scene.fjydhl.cn/Scene/Appscene/datadetail?sid=";

    //显示数据对应的适配器中的数据
    private  List<Map<String,String>> mList=new ArrayList<>();

    private SimpleAdapter mAdapter;
    private String sid;
    private String name;
    private MHandler handler=new MHandler();



    private TextView tv_collect;


    private LinearLayout ll_custmer;

    @ViewInject(R.id.single_tv_title)
    private TextView tv_title;

    @ViewInject(R.id.single_pull_01)
    private PullToRefreshListView pullToRefreshListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_scene);
        ViewUtils.inject(this);
        initView();
        loadData();
    }


    @OnClick(R.id.single_iv_back)
    public void back(View view){
        finish();
    }




    public void openCustomer(View view){

        String str_collect=tv_collect.getText().toString();
        if (str_collect==null||Integer.parseInt(str_collect)==0){
            Toast.makeText(this,"现在还没有数据",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent mIntent=new Intent(this,ClientListActivity.class);
        mIntent.putExtra("id",sid);
        startActivity(mIntent);

    }


    /**
     * 初始化组件
     */
    private void initView(){

        String[] from=new String[]{"scenes","data"};
        int[] to =new int[]{R.id.statistics_lv_tv_scenes,R.id.statistics_lv_data};
        mAdapter=new SimpleAdapter(this,mList,R.layout.item_statistics,from,to);
        pullToRefreshListView.setAdapter(mAdapter);
        pullToRefreshListView.setOnItemClickListener(this);
        sid=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");
        tv_title.setText(name);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                loadData();
            }
        });

        View view= LayoutInflater.from(this).inflate(R.layout.single_head,null);

        ll_custmer=(LinearLayout)view.findViewById(R.id.single_scene_ll_data);
        tv_collect=(TextView)view.findViewById(R.id.single_scene_tv_data);

        ll_custmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomer(view);
            }
        });

        pullToRefreshListView.getRefreshableView().addHeaderView(view);



    }


    /**
     * 更新数据
     * @param object
     */
    private void updateData(JSONObject object){


        try {
            JSONObject data=object.getJSONObject("data");

            if (data==null){
                Toast.makeText(this, "这儿现在还没有数据", Toast.LENGTH_SHORT).show();
                return;
            }

            tv_collect.setText(data.getString("customer_data_count"));


            JSONArray array=data.getJSONArray("page");
            int size=array.length();

            //清空原有数据
            mList.clear();
            allTableData.clear();

            for(int i=0;i<size;i++){


                JSONObject temObject=array.getJSONObject(i);

                Map<String,String> mMap=new HashMap<>();
                mMap.put("scenes",temObject.getString("title"));

                JSONArray array1=temObject.getJSONArray("record");
                int size1=array1.length();

                mMap.put("data",""+size1);
                mList.add(mMap);

                ArrayList<Map<String,String>> mArrayList=new ArrayList<>();
                SingleSceneData singleSceneData=new SingleSceneData();

                //获取一个表单中所有的用户数据，到mArrayList中
                for(int j=0;j<size1;j++){
                    Map<String,String> map=new ArrayMap<>();
                    JSONObject temData=array1.getJSONObject(j).getJSONObject("data");

                    Iterator<String> iterator=temData.keys();
                    while (iterator.hasNext()){

                        String key=iterator.next();
                        JSONObject finalObject=temData.getJSONObject(key);
                        map.put(finalObject.getString("key"),finalObject.getString("value"));
                    }

                    mArrayList.add(map);
                }

                singleSceneData.setTableData(mArrayList);
                allTableData.add(singleSceneData);


            }

            ll_custmer.setClickable(true);
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


        String str_get=url+sid;

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, str_get,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            Log.i("single", responseInfo.result);
                            int code = object.getInt("code");

                            if (code!=0){
                                Toast.makeText(SingleSceneActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SingleSceneActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(200);
                    }

                });


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i-2<0){

            return;
        }


        Intent mIntent=new Intent(this, CollectDataActivity.class);
        mIntent.putExtra("form",i-2);
        mIntent.putExtra("title",name);
        startActivity(mIntent);


    }


    /**
     * 每个表单中所有的用户数据
     */
    public class SingleSceneData{

        //每个map为一个用户的数据
            private ArrayList<Map<String,String>> tableData;

        public ArrayList<Map<String, String>> getTableData() {
            return tableData;
        }

        public void setTableData(ArrayList<Map<String, String>> tableData) {
            this.tableData = tableData;
        }
    }


    public class MHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what==200){
                pullToRefreshListView.onRefreshComplete();
            }
        }
    }

}
