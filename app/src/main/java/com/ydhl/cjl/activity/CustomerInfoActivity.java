package com.ydhl.cjl.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ydhl.cjl.R;


public class CustomerInfoActivity extends ActionBarActivity {


    @ViewInject(R.id.customer_info_tv_name)
    private TextView tv_name;

    @ViewInject(R.id.customer_info_tv_phone)
    private TextView tv_phone;

    @ViewInject(R.id.customer_info_tv_sex)
    private TextView tv_sex;

    @ViewInject(R.id.customer_info_tv_email)
    private TextView tv_email;

    @ViewInject(R.id.customer_info_tv_depart)
    private TextView tv_depart;

    @ViewInject(R.id.customer_info_tv_business)
    private TextView tv_business;

    @ViewInject(R.id.customer_info_tv_address)
    private TextView tv_address;

    @ViewInject(R.id.customer_info_tv_website)
    private TextView tv_website;

    @ViewInject(R.id.customer_info_tv_qq)
    private TextView tv_qq;

    @ViewInject(R.id.customer_info_tv_team)
    private TextView tv_team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        ViewUtils.inject(this);
        initView();

    }

    private void initView(){

        Intent mIntent=getIntent();


    }











}
