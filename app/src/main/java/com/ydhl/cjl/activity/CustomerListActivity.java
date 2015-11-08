package com.ydhl.cjl.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private String url="http://scene.fjydhl.cn/Scene/Appscene/customdata?uid=";

    @ViewInject(R.id.customer_list_lv_1)
    private ListView mListView;
    private List<Map<String,String>> mData=new ArrayList<>();
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        ViewUtils.inject(this);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView(){
        String[] from=new String[]{"name"};
        int[] to=new int[]{R.id.customer_list_tv_name};
        mAdapter=new SimpleAdapter(this,mData,R.layout.item_customer_list,from,to);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }


    /**
     * 更新显示数据
     * @param object
     */
    private void updateData(JSONObject object){

        mAdapter.notifyDataSetChanged();

    }


    /**
     * 从网络加载数据
     */
    private void loadData(){

        String mUid=(String)((CJLApplication)getApplication()).getAppData(CJLApplication.KEY_UID);
        String str_get=url+mUid;

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, str_get,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            Log.i("scenes", responseInfo.result);
                            int code = object.getInt("code");

                            if (code!=0){
                                Toast.makeText(CustomerListActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }else {
                                updateData(object);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(CustomerListActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        Intent mItent=new Intent(this,CustomerInfoActivity.class);
        //mItent.putExtra("");


        startActivity(mItent);

    }







}
