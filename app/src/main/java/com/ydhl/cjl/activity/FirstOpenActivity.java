package com.ydhl.cjl.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;
import com.ydhl.cjl.utils.Md5Util;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FirstOpenActivity extends ActionBarActivity {

    private final int REGISTER=100;
    private final int LOGIN=300;
    private final String url="http://scene.fjydhl.cn/Scene/Appuser/login";
    private final String DISPLAYNAME="displayName";
    private final String IMAGEURL="imageUrl";
    private final String EMAIL="email";



    List<View> list = new ArrayList<View>();
    int Mposition=0;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIsFirst();
        setContentView(R.layout.activity_first_open);
        initView();

    }

    private void checkIsFirst() {

        SharedPreferences preferences = getSharedPreferences("login",
                Activity.MODE_PRIVATE);
        Boolean haslogin = preferences.getBoolean("hasloginin", false);

        if (haslogin) {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }

    }

    private void initView() {

        final List<View> dots = new ArrayList<View>();
        dots.add(findViewById(R.id.startentry_dot0));
        dots.add(findViewById(R.id.startentry_dot1));
        dots.add(findViewById(R.id.startentry_dot2));
        dots.add(findViewById(R.id.startentry_dot3));
        dots.add(findViewById(R.id.startentry_dot4));

        TextView tv1 = new TextView(this);
        tv1.setBackgroundResource(R.mipmap.bg_intro_1);
        list.add(tv1);

        TextView tv2 = new TextView(this);
        tv2.setBackgroundResource(R.mipmap.bg_intro_2);
        list.add(tv2);

        TextView tv3 = new TextView(this);
        tv3.setBackgroundResource(R.mipmap.bg_intro_3);
        list.add(tv3);

        TextView tv4 = new TextView(this);
        tv4.setBackgroundResource(R.mipmap.bg_intro_4);
        list.add(tv4);

        TextView tv5 = new TextView(this);
        tv5.setBackgroundResource(R.mipmap.bg_intro_5);
        list.add(tv5);


        viewPager = (ViewPager) findViewById(R.id.startentry_viewpager);

        PagerAdapter adapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Mposition=position;
                container.addView(list.get(position));

                return list.get(position);
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return list.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(list.get(position));
            }

        };

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                resetAll(dots);
                dots.get(arg0).setBackgroundResource(R.drawable.dot_focused);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    private void resetAll(List<View> dots) {

        for (View view : dots) {

            view.setBackgroundResource(R.drawable.dot_normal);
        }

    }

    public void login(View v) {

        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra("isFirst",true);
        startActivityForResult(intent,LOGIN);
       // overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out_center);

    }

    public void regist(View v) {
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivityForResult(intent,REGISTER);
       // overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out_center);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //如果是登录界面返回的
        if (requestCode==LOGIN&&resultCode==200){
            Intent mItent=new Intent(this,MainActivity.class);
            startActivity(mItent);
            finish();
            return;
        }

        //如果是注册界面返回的
        if(requestCode==REGISTER&&resultCode==200){
            String str_accout = data.getStringExtra("email");
            String str_pwd = data.getStringExtra("password");
            checkLogin(str_accout,str_pwd);

        }


    }


    private void checkLogin(final String str_account, final String str_pwd){


        final JSONObject obj = new JSONObject();

        String md5 = Md5Util.md5(str_account + str_pwd);
        try {
            obj.put("email", str_account);
            obj.put("password", str_pwd);
            obj.put("sign", md5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(obj.toString(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            int code = object.getInt("code");

                            if (code!=0){
                                Toast.makeText(FirstOpenActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }else{

                                String uid=object.getString("uid");
                                String displayName=object.getString("display_name");
                                String imageUrl=object.getString("avatar");
                                String email=object.getString("email");

                                CJLApplication application=(CJLApplication)getApplication();
                                application.setAppdata(CJLApplication.KEY_UID,uid);

                                if (displayName!=null&&!displayName.equals("")) {
                                    application.setAppdata(DISPLAYNAME, displayName);
                                }else{
                                    application.setAppdata(DISPLAYNAME, email);
                                }
                                application.setAppdata(EMAIL,email);
                                application.setAppdata(IMAGEURL, imageUrl);


                                //记住本次登录的账号
                                SharedPreferences.Editor editor=FirstOpenActivity.this.getSharedPreferences("login",MODE_PRIVATE).edit();
                                editor.putString("login_name",str_account);
                                editor.putString("password",str_pwd);
                                editor.putString("uid",uid);
                                editor.putBoolean("hasloginin",true);
                                editor.commit();


                                Intent mItent=new Intent(FirstOpenActivity.this,MainActivity.class);
                                FirstOpenActivity.this.startActivity(mItent);
                                finish();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(FirstOpenActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }


                });

    }
}
