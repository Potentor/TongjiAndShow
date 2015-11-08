package com.ydhl.cjl.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;

public class UserCentralActivity extends ActionBarActivity {
    @ViewInject(R.id.iv_menu)
    ImageView iv_back;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.tv_wx)
    TextView tv_wx;
    @ViewInject(R.id.tv_about)
    TextView tv_about;
    @ViewInject(R.id.tv_fb)
    TextView tv_fb;
    @ViewInject(R.id.tv_share)
    TextView tv_share;


    private final String DISPLAYNAME="email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_central);
        ViewUtils.inject(this);
        setupTitleBar();
        setupUserName();
    }
    private void setupTitleBar() {
        iv_back.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText(R.string.titleBar_activity_user_central);
        tv_wx.setText((String)((CJLApplication)this.getApplication()).getAppData("email"));
    }

    private void setupUserName() {
        CJLApplication application = (CJLApplication) getApplication();
        String name = application.getAppData(DISPLAYNAME).toString();
        if (name != null){
            tv_wx.setText(name);
        }

    }



    @OnClick({R.id.btn_logoff, R.id.tv_fb, R.id.tv_share, R.id.tv_wx, R.id.tv_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logoff:
                setResult(300,getIntent());
                finish();
                break;
            case R.id.tv_fb:
                FeedbackAgent agent = new FeedbackAgent(this);
                agent.startFeedbackActivity();
                break;
            case R.id.tv_share:
               shareTo();
                break;
            case R.id.tv_wx:
                break;
            case R.id.tv_about:
                Intent mItent=new Intent(this,AboutUsActivity.class);
                startActivity(mItent);
                break;

        }
    }


    /**
     * 分享到第三方社交平台
     */
    private void shareTo(){


        String title="移动互联";
        String imageUrl="http://as.fjydhl.cn/ydhl/assets/images/logo.png";
        String content="精美展示，社交推广，移动时代最强大的移动场景自营销管家";
        String targetUrl="http://scene.fjydhl.cn/APP/cjl.apk";


        UMSocialService umService = UMServiceFactory.getUMSocialService("com.umeng.share");

        UMImage image = new UMImage(this,imageUrl);
        //分享到微信
        WeiXinShareContent wxContent = new WeiXinShareContent(image);
        wxContent.setTitle(title);
        wxContent.setShareContent(content);
        wxContent.setTargetUrl(targetUrl);
        umService.setShareMedia(wxContent);


        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareImage(image);
        circleMedia.setTargetUrl(targetUrl);
        umService.setShareMedia(circleMedia);


        //分享到新浪微博
        SinaShareContent sinaShareContent=new SinaShareContent();
        sinaShareContent.setShareContent(content);
        sinaShareContent.setTitle(title);
        sinaShareContent.setShareImage(image);
        sinaShareContent.setTargetUrl(targetUrl);
        umService.setShareMedia(sinaShareContent);

        TencentWbShareContent tencentWbShareContent=new TencentWbShareContent();
        tencentWbShareContent.setShareContent(content);
        tencentWbShareContent.setTargetUrl(targetUrl);
        tencentWbShareContent.setTitle(title);
        tencentWbShareContent.setShareImage(image);
        umService.setShareMedia(tencentWbShareContent);

        //分享到QQ
        QQShareContent qqContent = new QQShareContent(image);
        qqContent.setTitle(title);
        qqContent.setShareContent(content);
        qqContent.setTargetUrl(targetUrl);
        umService.setShareMedia(qqContent);

        //分享到QQ空间
        QZoneShareContent qzoneContent = new QZoneShareContent(image);
        qzoneContent.setTitle(title);
        qzoneContent.setShareContent(content);
        qzoneContent.setTargetUrl(targetUrl);
        umService.setShareMedia(qzoneContent);


        umService.setShareContent(targetUrl);
        umService.setShareImage(image);

        umService.openShare(this, false);
    }


    @OnClick(R.id.iv_menu)
    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
        }

    }
}
