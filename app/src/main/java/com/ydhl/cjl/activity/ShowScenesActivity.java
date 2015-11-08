package com.ydhl.cjl.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ydhl.cjl.R;

public class ShowScenesActivity extends ActionBarActivity {

    WebView web;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_show_scenes);

        web = (WebView) findViewById(R.id.web_show_web);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("tel")) {

                    String phone = url.replaceFirst("tel", "");
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }


                view.loadUrl(url);
                return true;
            }
        });
        init(web);


        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("address");

        url = url.replaceAll("&amp;", "&");


        web.loadUrl(url);

    }

    private void init(WebView view) {


        view.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = view.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//


        webSettings.setSupportZoom(true); //
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLoadWithOverviewMode(true);

        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            // 返回键退回
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        web.loadUrl("www.baidu.c");
    }
}
