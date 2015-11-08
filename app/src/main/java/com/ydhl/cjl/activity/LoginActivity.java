package com.ydhl.cjl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;
import com.ydhl.cjl.utils.Md5Util;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 登录
 */
public class LoginActivity extends ActionBarActivity {


    private final String url="http://scene.fjydhl.cn/Scene/Appuser/login";
    private final int REGISTER=500;//注册界面访问标志
    private final String DISPLAYNAME="displayName";
    private final String IMAGEURL="imageUrl";
    private final String EMAIL="email";


    private boolean isFirst=false;

    @ViewInject(R.id.register_et_login)
    private EditText et_account;

    @ViewInject(R.id.register_et_pwd)
    private EditText et_pwd;

    @ViewInject(R.id.register_bt_login)
    private Button bt_login;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        Intent intent=getIntent();
        if (intent!=null&&intent.getBooleanExtra("isFirst",false)){
            isFirst=true;
        }

        SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
        String login_name=preferences.getString("login_name","");
       // String password=preferences.getString("password","");


        et_account.setText(login_name);
        progressDialog=ProgressDialog.show(this,"","登录中...");
      //  progressDialog.setContentView(LayoutInflater.from(this).inflate(R.layout.login_progress,null));
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.dismiss();


    }

    @OnClick(R.id.register_bt_login)
    private void login(View view){

        String str_account=et_account.getText().toString();
        if (str_account==null||str_account.replaceAll(" ","").equals("")){
            Toast.makeText(this,"您账户名还未输入",Toast.LENGTH_SHORT).show();
            return;

        }


        String str_pwd=et_pwd.getText().toString();
        if (str_pwd==null||str_pwd.replaceAll(" ","").equals("")){
            Toast.makeText(this,"您还未输入您的密码",Toast.LENGTH_SHORT).show();
            return;

        }

        progressDialog.show();
        checkLogin(str_account,str_pwd);


    }


    @OnClick(R.id.login_iv_back)
    private void back(View view){
        finish();
    }


    private void checkLogin(String str_account,String str_pwd){



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
                            Log.i("shen","info:"+responseInfo.result);
                            int code = object.getInt("code");

                            if (code!=0){
                                Toast.makeText(LoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
                                SharedPreferences.Editor editor=LoginActivity.this.getSharedPreferences("login",MODE_PRIVATE).edit();
                                editor.putString("login_name",et_account.getText().toString());
                                editor.putString("password",et_pwd.getText().toString());
                                editor.putString("uid",uid);
                                editor.putBoolean("hasloginin",true);
                                editor.commit();

                                if (isFirst){
                                    setResult(200,getIntent());
                                    finish();
                                    return;
                                }

                                Intent mItent=new Intent(LoginActivity.this,MainActivity.class);
                                LoginActivity.this.startActivity(mItent);
                                finish();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(LoginActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                });

    }

    @OnClick(R.id.register_tv_forget_pwd)
    private void forgerPassWord(View view){
        //忘记密码
        Intent mIntent=new Intent(this,RetrievePasswordActivity.class);
        startActivity(mIntent);
    }

    @OnClick(R.id.register_tv_no_account)
    private void registerAcount(View view) {
        //注册账户
        Intent mIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(mIntent, REGISTER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REGISTER&&resultCode==200){

                String str_accout = data.getStringExtra("email");
                String str_pwd = data.getStringExtra("password");
                checkLogin(str_accout,str_pwd);

        }


    }

    @Override
    public void onBackPressed() {
        setResult(404,getIntent());
        super.onBackPressed();

    }
}
