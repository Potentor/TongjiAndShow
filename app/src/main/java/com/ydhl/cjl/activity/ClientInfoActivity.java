package com.ydhl.cjl.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;
import com.ydhl.cjl.db.entity.ClientInfo;

import java.util.List;

public class ClientInfoActivity extends ActionBarActivity {
    @ViewInject(R.id.iv_menu)
    ImageView iv_back;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.iv_call)
    ImageView iv_call;
    @ViewInject(R.id.tv_sex)
    TextView tv_sex;
    @ViewInject(R.id.tv_email)
    TextView tv_email;
    @ViewInject(R.id.tv_sector)
    TextView tv_sector;
    @ViewInject(R.id.tv_job)
    TextView tv_job;
    @ViewInject(R.id.tv_address)
    TextView tv_address;
    @ViewInject(R.id.tv_net)
    TextView tv_net;
    @ViewInject(R.id.tv_qq)
    TextView tv_qq;
    @ViewInject(R.id.tv_team)
    TextView tv_team;

    private ClientInfo clientInfo;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);
        ViewUtils.inject(this);
        setupTitleBar();
        setupClientInfo();
        updatePage();
    }

    private void setupTitleBar() {
        iv_back.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText(R.string.titleBar_activity_clientInfo);
    }

    /**
     * 初始化客户信息
     */
    private void setupClientInfo() {
//        clientInfo = new ClientInfo();
//        clientInfo.setC_name("晓明1");
//        clientInfo.setC_phoneNum("15333333333");
//        clientInfo.setC_sex("男");
//        clientInfo.setC_email("123123@qq.com");
//        clientInfo.setC_sector("呵呵部门");
//        clientInfo.setC_job("专职下水道");
//        clientInfo.setC_address("北京七大胡同八小巷");
//        clientInfo.setC_net("bukengnikengshui.com");
//        clientInfo.setC_qq("941941");
//        clientInfo.setC_team("下水道，下水道，我们都是下水道");
        mIntent = getIntent();
        int position = mIntent.getIntExtra("position", 0);
        clientInfo = ((List<ClientInfo>) ((CJLApplication) this.getApplication()).getAppData(CJLApplication.KEY_CLIENT_LIST)).get(position);

    }

    /**
     * 更新页面信息
     */
    public void updatePage() {
        tv_name.setText(clientInfo.getC_name());
        tv_phone.setText(clientInfo.getC_phoneNum());
        if (clientInfo.getC_phoneNum()!=null&&clientInfo.getC_phoneNum().length()>0){
            iv_call.setVisibility(View.VISIBLE);
        }
        tv_sex.setText(clientInfo.getC_sex());
        tv_email.setText(clientInfo.getC_email());
        tv_sector.setText(clientInfo.getC_sector());
        tv_job.setText(clientInfo.getC_job());
        tv_address.setText(clientInfo.getC_address());
        tv_net.setText(clientInfo.getC_net());
        tv_qq.setText(clientInfo.getC_qq());
        tv_team.setText(clientInfo.getC_team());


    }

    @OnClick(R.id.iv_menu)
    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
        }

    }
    @OnClick(R.id.iv_call)
    public void onClick(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + clientInfo.getC_phoneNum()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

}
