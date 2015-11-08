package com.ydhl.cjl.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class SplashActivity extends ActionBarActivity {


    private final String url="http://scene.fjydhl.cn/Scene/Appuser/login";
    private final String DISPLAYNAME="displayName";
    private final String IMAGEURL="imageUrl";
    private final String EMAIL="email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);


        checkHasLogin();




    }


    Handler mHandler=new Handler();

    private void checkHasLogin(){

        SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
        String login_name=preferences.getString("login_name","0#");
        String password=preferences.getString("password","0#");
        if (login_name.equals("0#")||password.equals("0#")){


            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.finish();
                    Intent mIntent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(mIntent);
                }
            },3000);

        }else{

         checkLogin(login_name,password);

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
                            Log.i("shen", "info:" + responseInfo.result);
                            int code = object.getInt("code");

                            if (code!=0){

                                if (code==1003|code==1004){

                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent mItent=new Intent(SplashActivity.this,LoginActivity.class);
                                            SplashActivity.this.startActivity(mItent);
                                            SplashActivity.this.finish();
                                        }
                                    },3000);

                                    Toast.makeText(SplashActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                }


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
                                SharedPreferences.Editor editor=SplashActivity.this.getSharedPreferences("login",MODE_PRIVATE).edit();
                                editor.putString("login_name",str_account);
                                editor.putString("password",str_pwd);
                                editor.putString("uid",uid);
                                editor.commit();


                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        SplashActivity.this.finish();
                                        Intent mItent=new Intent(SplashActivity.this,MainActivity.class);
                                        SplashActivity.this.startActivity(mItent);
                                    }
                                },2000);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SplashActivity.this, "数据传输错误！", Toast.LENGTH_SHORT).show();
                        }finally {

                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(SplashActivity.this, "连接服务器失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                });

    }

}
