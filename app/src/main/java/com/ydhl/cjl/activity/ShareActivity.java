package com.ydhl.cjl.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.ydhl.cjl.R;

import java.util.regex.Pattern;

public class ShareActivity extends ActionBarActivity {

    private static final String WX_APP_ID = "wx0c77eff0af22ada7";
    private static final String WX_APP_SECRET = "bbddfcd8e9490bd504e356f1a670de04";
    private static final String QQ_APP_ID = "1104551837";
    private static final String QQ_APP_SECRET = "FrTWNAliTVhjFmWh";
    private UMSocialService umService;

    @ViewInject(R.id.editText)
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ViewUtils.inject(this);
        setupUMSDK();
    }

    private void setupUMSDK() {
        umService = UMServiceFactory.getUMSocialService("com.umeng.share");
        try {
            // 添加微信平台
            UMWXHandler wxHandler = new UMWXHandler(this, WX_APP_ID, WX_APP_SECRET);
            wxHandler.addToSocialSDK();
            // 支持微信朋友圈
            UMWXHandler wxCircleHandler = new UMWXHandler(this, WX_APP_ID, WX_APP_SECRET);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();

            //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, QQ_APP_ID, QQ_APP_SECRET);
            qqSsoHandler.addToSocialSDK();


            //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
            QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, QQ_APP_ID,QQ_APP_SECRET);
            qZoneSsoHandler.addToSocialSDK();
        } catch (Exception e) {
            Log.e("ShareActivity","init sdk err:"+e.getMessage());
        }
    }

    @OnClick(R.id.button5)
    private void onClick(View v) {


        UMImage image = new UMImage(this,"http://as.fjydhl.cn/ydhl/assets/image/conerwei.jpg");
        //分享到微信
        WeiXinShareContent wxContent = new WeiXinShareContent(image);
        wxContent.setTitle("Title");
        wxContent.setShareContent("Content");
        wxContent.setTargetUrl("http://scene.fjydhl.cn/#/home");
        umService.setShareMedia(wxContent);

        //分享到QQ
        QQShareContent qqContent = new QQShareContent(image);
        qqContent.setTitle("QQ Title");
        qqContent.setShareContent("QQ Content");
        qqContent.setTargetUrl("http://scene.fjydhl.cn/#/home");
        umService.setShareMedia(qqContent);

        //分享到QQ空间
        QZoneShareContent qzoneContent = new QZoneShareContent(image);
        qzoneContent.setTitle("QQ Title");
        qzoneContent.setShareContent("QQ Content");
        qzoneContent.setTargetUrl("http://scene.fjydhl.cn/#/home");
        umService.setShareMedia(qzoneContent);

        umService.setShareContent("Content1");
        umService.setShareImage(image);
        umService.setAppWebSite(SHARE_MEDIA.QQ,"http://as.fjydhl.cn/ydhl/assets/image/conerwei.jpg");

        umService.openShare(this, false);



    }
}
