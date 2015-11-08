package com.ydhl.cjl.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ydhl.cjl.R;
import com.ydhl.cjl.utils.Md5Util;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RetrievePasswordActivity extends ActionBarActivity {
    @ViewInject(R.id.iv_menu)
    ImageView iv_back;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.et_email)
    EditText et_email;
    @ViewInject(R.id.btn_next)
    Button btn_next;
    private static final String EMAIL_REGEX = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);
        ViewUtils.inject(this);
        setupTitleBar();
        setupETListener();
    }

    private void setupTitleBar() {
        iv_back.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText(R.string.titleBar_activity_retrieve_password);
    }

    @OnClick(R.id.iv_menu)
    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
        }
    }

    @OnClick(R.id.btn_next)
    public void onClickNext(View view) {
        pwdretrieve(et_email.getText().toString().trim());

    }

    public void setupETListener() {
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = editable.toString().trim();
                btn_next.setEnabled(email.matches(EMAIL_REGEX));

            }
        });


    }

    private void pwdretrieve(String email) {
        //1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//2.调用hideSoftInputFromWindow方法隐藏软键盘
        imm.hideSoftInputFromWindow(et_email.getWindowToken(), 0); //强制隐藏键盘

        Toast.makeText(this, "邮件已发送,请登录邮箱修改密码", Toast.LENGTH_SHORT).show();
        final JSONObject obj = new JSONObject();

        String md5 = Md5Util.md5(email);
        try {
            obj.put("email", email);
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
                "http://scene.fjydhl.cn/Scene/Appuser/pwdretrieve",
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
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            int code = object.getInt("code");
                            Toast.makeText(RetrievePasswordActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(RetrievePasswordActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }


                });


    }
}
