package com.ydhl.cjl.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ydhl.cjl.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegisterProtocolActivity extends ActionBarActivity {
    @ViewInject(R.id.iv_menu)
    ImageView iv_back;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.tv_protocol)
    TextView tv_protocol;
    StringBuilder protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_protocol);
        ViewUtils.inject(this);
        Log.d("Register","RegisterProtocolActivity"+"onCreate");
        setupTitleBar();
        loadProtocol();
        tv_protocol.setText(protocol.toString());

    }

    public void loadProtocol() {
        protocol = new StringBuilder();
        AssetManager assetManager = getResources().getAssets();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open("protocol.txt")));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.length()>0){
                    protocol.append(line).append("\n");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.iv_menu)
    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
        }
    }

    private void setupTitleBar() {
        iv_back.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText(R.string.titleBar_activity_register_protocol);
    }
}
