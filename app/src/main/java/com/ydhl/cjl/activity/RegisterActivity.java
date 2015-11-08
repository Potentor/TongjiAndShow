package com.ydhl.cjl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.BodyParamsEntity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;
import com.tencent.open.a;
import com.ydhl.cjl.R;
import com.ydhl.cjl.utils.Md5Util;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

/**
 * 注册
 */
public class RegisterActivity extends ActionBarActivity {
    @ViewInject(R.id.iv_menu)
    ImageView iv_back;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.register_edit_email)
    private EditText email;
    @ViewInject(R.id.register_edit_password)
    private EditText input_password;
    @ViewInject(R.id.register_edit_maketrue_password)
    private EditText makeTrue_password;
    @ViewInject(R.id.checkBox)
    private CheckBox checkBox;
    @ViewInject(R.id.register_btn_regist)
    private Button regist;
    private static final String EMAIL_REGEX = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        setupTitleBar();
        mIntent = getIntent();

    }

    private void setupTitleBar() {
        iv_back.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText(R.string.titleBar_activity_register);
    }

    @OnClick({R.id.tv_protocol, R.id.iv_menu, R.id.register_btn_regist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_protocol:
                startActivity(new Intent(this, RegisterProtocolActivity.class));
                break;
            case R.id.iv_menu:
                finish();
                break;
            case R.id.register_btn_regist:
                String email = this.email.getText().toString().trim();
                if (email == null || email.length() == 0) {
                    Toast.makeText(this, R.string.Error_activity_register_email_null, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!email.matches(EMAIL_REGEX)) {
                    Toast.makeText(this, R.string.Error_activity_register_email_regex, Toast.LENGTH_SHORT).show();
                    break;
                }

                String password = this.input_password.getText().toString().trim();
                if (password == null || password.length() == 0) {
                    Toast.makeText(this, R.string.Error_activity_register_password_null, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (password != null && password.length() < 6) {
                    Toast.makeText(this, R.string.Error_activity_register_password_small, Toast.LENGTH_SHORT).show();
                    break;
                }
                String makeTurePssword = makeTrue_password.getText().toString();
                if (!password.equals(makeTurePssword)) {
                    Toast.makeText(this, R.string.Error_activity_register_password_not_equal, Toast.LENGTH_SHORT).show();
                    break;
                }
                //todo  regist
                regist(email, password);
                break;

        }
    }

    @OnCompoundButtonCheckedChange(R.id.checkBox)
    public void checkChange(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.checkBox:
                regist.setEnabled(b);
                break;
        }
    }

    private void regist(final String email, final String password) {
        //{“username”:”zhangsan”,”password”:”123456”,
//        ”sign”:”c1cb843f3929978af615fe7dfbf532cb”}
        regist.setEnabled(false);
        final JSONObject obj = new JSONObject();

        String md5 = Md5Util.md5(email + password);
        try {
            obj.put("email", email);
            obj.put("password", password);
            obj.put("sign", md5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("RegisterActivity", obj.toString());
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(obj.toString(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://scene.fjydhl.cn/Scene/Appuser/register",
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        if (isUploading) {
                        } else {
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        regist.setEnabled(true);
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            int code = object.getInt("code");
                            if (code != 0) {
                                Toast.makeText(RegisterActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();

                            } else {
                                mIntent.putExtra("email", email);
                                mIntent.putExtra("password", password);
                                setResult(200, mIntent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        regist.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, R.string.Error_activity_register_web, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
