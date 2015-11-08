package com.ydhl.cjl.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.ydhl.cjl.R;
import com.ydhl.cjl.fragment.ScenesFragment;
import com.ydhl.cjl.fragment.SpreadFragment;
import com.ydhl.cjl.fragment.StatisticsFragment;

import java.util.HashMap;
import java.util.Map;


/**
 * 主界面
 */
public class MainActivity extends ActionBarActivity {

    private static final String WX_APP_ID = "wx0c77eff0af22ada7";
    private static final String WX_APP_SECRET = "bbddfcd8e9490bd504e356f1a670de04";
    private static final String QQ_APP_ID = "1104551837";
    private static final String QQ_APP_SECRET = "FrTWNAliTVhjFmWh";



    private SlidingMenu smLeft;
    private Handler handler = new Handler();
    private boolean isClosing = false;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.rb_scenes)
    private RadioButton rbScenes;
    private UMSocialService umService;

    Map<String,Fragment> map_fragments=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        setupMenu();
        if(savedInstanceState==null){
            rbScenes.setChecked(true);
        }
        setupUMSDK();


    }


    /**
     * 初始化侧滑菜单
     */
    private void setupMenu() {
        smLeft = new SlidingMenu(this);
        smLeft.setMode(SlidingMenu.LEFT);
        smLeft.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        smLeft.setShadowWidthRes(R.dimen.menu_shadow_width);
        smLeft.setShadowDrawable(R.drawable.bg_shadow);
        smLeft.setBehindOffsetRes(R.dimen.menu_offset);
        smLeft.setFadeDegree(0.35f);
        smLeft.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        smLeft.setMenu(R.layout.activity_main_menu);
    }

    /**
     * 左上菜单按钮点击事件
     * @param v
     */
    @OnClick(R.id.iv_menu)
    public void onMenuButtonClick(View v) {
        if(smLeft.isMenuShowing()) {
            smLeft.showContent();
        } else {
            smLeft.showMenu();
        }
    }


    /**
     * 下方导航栏选择事件
     * @param group
     * @param checkedId
     */
    @OnRadioGroupCheckedChange(R.id.rg_navi)
    public void onNavigationChange(RadioGroup group,int checkedId){

        changeContent(checkedId);
    }


    private void changeContent(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id){
            case R.id.rb_scenes:

                tvTitle.setText("我的场景");
                if (map_fragments.containsKey("scenes")) {
                    hideAll();
                    transaction.show(map_fragments.get("scenes"));
                }else{
                    Fragment scenes=new ScenesFragment();
                    map_fragments.put("scenes",scenes);
                    transaction.add(R.id.content,scenes,"ScenesFragment");
                }
                break;

            case R.id.rb_statistics:

                tvTitle.setText("数据收集");
                if (map_fragments.containsKey("statistics")){
                    hideAll();
                    transaction.show(map_fragments.get("statistics"));
                }else{
                    Fragment statisticsFragment=new StatisticsFragment();
                    map_fragments.put("statistics",statisticsFragment);
                    transaction.add(R.id.content,statisticsFragment,"StatisticsFragment");
                }
                break;
            case R.id.rb_spread:

                tvTitle.setText("展示推广");
                if (map_fragments.containsKey("spread")){
                    hideAll();
                    transaction.show(map_fragments.get("spread"));
                }else{
                    Fragment spreadFragment=new SpreadFragment();
                    map_fragments.put("spread",spreadFragment);
                    transaction.add(R.id.content,spreadFragment,"SpreadFragment");
                }
                break;
        }
        transaction.commit();
    }


    /**
     * 隐藏所有的Fragment
     */
    private void hideAll(){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (map_fragments.containsKey("scenes")){
            transaction.hide(map_fragments.get("scenes"));
        }

        if (map_fragments.containsKey("statistics")){
            transaction.hide(map_fragments.get("statistics"));
        }

        if (map_fragments.containsKey("spread")){
            transaction.hide(map_fragments.get("spread"));
        }
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        if(smLeft.isMenuShowing()){
            smLeft.showContent();
            return;
        }
        if(isClosing){
            super.onBackPressed();
            return;
        } else {
            isClosing =true;
            Toast.makeText(this,"再次按下返回按钮退出应用",Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isClosing = false;
                }
            },500);
        }
    }



    /**
     * 初始化友盟SDK
     */
    private void setupUMSDK() {

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
            Log.e("error", "init sdk err:" + e.getMessage());
        }
    }


}
