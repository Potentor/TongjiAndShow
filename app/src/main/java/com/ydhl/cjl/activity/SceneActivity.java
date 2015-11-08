package com.ydhl.cjl.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ydhl.cjl.R;

public class SceneActivity extends ActionBarActivity {

    @ViewInject(R.id.iv_menu)
    private ImageView iv_back;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        ViewUtils.inject(this);
        setupTitleBar();
    }
    private void setupTitleBar(){
        iv_back.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText("客户列表");
    }



}
