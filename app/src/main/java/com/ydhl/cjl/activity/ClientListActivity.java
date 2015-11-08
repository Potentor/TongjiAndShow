package com.ydhl.cjl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.umeng.fb.audio.c;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;
import com.ydhl.cjl.adapter.ClientListActivity.ClientListAdapter;
import com.ydhl.cjl.db.entity.ClientInfo;
import com.ydhl.cjl.utils.ParseUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientListActivity extends ActionBarActivity {

    @ViewInject(R.id.iv_menu)
    ImageView iv_back;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.lv_user)
    ListView lv_user;
    ClientListAdapter adapter;

    List<ClientInfo> clients = new ArrayList<>();

    Intent mIntent;

    private String url = "http://scene.fjydhl.cn/Scene/Appscene/customdata?sid=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ViewUtils.inject(this);
        setupTitleBar();
        setupClients();
        setupAdapter();
        mIntent = getIntent();
        update();

    }

    private void setupTitleBar() {
        iv_back.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText(R.string.titleBar_activity_clientList);
    }

    /**
     * 初始化客户列表
     */
    private void setupClients() {
        clients = new ArrayList<>();
    }
    @OnItemClick(R.id.lv_user)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,ClientInfoActivity.class);
        intent.putExtra("position",i);
        startActivity(intent);

    }

    /**
     * 初始化适配器
     */
    private void setupAdapter() {
        adapter = new ClientListAdapter(this, clients);
        lv_user.setAdapter(adapter);
    }

    @OnClick(R.id.iv_menu)
    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
        }

    }

    public void update() {
        String id = mIntent.getStringExtra("id");

        /**
         * 从网络加载数据
         */

        String str_get = url + id;

        Log.i("getd",str_get);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, str_get,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            Log.i("scenes", responseInfo.result);
                            int code = object.getInt("code");

                            if (code != 0) {
                                Toast.makeText(ClientListActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                List<ClientInfo> clients = ParseUtil.parseClientInfo(responseInfo.result);
                                CJLApplication app = (CJLApplication) ClientListActivity.this.getApplication();
                                app.setAppdata(CJLApplication.KEY_CLIENT_LIST, clients);
                                adapter.setData(clients);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(ClientListActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
