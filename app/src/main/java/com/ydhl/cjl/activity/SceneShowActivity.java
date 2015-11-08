package com.ydhl.cjl.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;
import com.ydhl.cjl.adapter.SceneShowAcitivity.MobileAdapter;
import com.ydhl.cjl.adapter.SceneShowAcitivity.OverviewAdapter;
import com.ydhl.cjl.adapter.SceneShowAcitivity.StatisticsAdapter;
import com.ydhl.cjl.adapter.SceneShowAcitivity.ViewPagerAdapter;
import com.ydhl.cjl.db.entity.SceneContentInfo;
import com.ydhl.cjl.db.entity.SceneInfo;
import com.ydhl.cjl.db.entity.SceneMobileInfo;
import com.ydhl.cjl.db.entity.SceneSummaryInfo;
import com.ydhl.cjl.utils.CommonUtils;
import com.ydhl.cjl.utils.Md5Util;
import com.ydhl.cjl.utils.ParseUtil;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SceneShowActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.iv_menu)
    private ImageView iv_menu;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.iv_cover)
    private ImageView iv_cover;
    @ViewInject(R.id.tv_show)
    private TextView tv_show;
    @ViewInject(R.id.tv_collect)
    private TextView tv_collect;
    @ViewInject(R.id.rg_navi)
    private RadioGroup rg_navi;
    @ViewInject(R.id.rb_overview)
    private RadioButton rb_overview;
    @ViewInject(R.id.rb_mobile)
    private RadioButton rb_mobile;
    @ViewInject(R.id.rb_statistics)
    private RadioButton rb_statistics;
    @ViewInject(R.id.vp_navi)
    private ViewPager vp_navi;
    ViewPagerAdapter viewPagerAdapter;

    private List<View> views = new ArrayList<>();


    private static final int index_vp_overview = 0;
    private static final int index_vp_mobile = 1;
    private static final int index_vp_statistics = 2;
    Intent mIntent;
    SceneInfo sceneInfo;
    OverviewAdapter overviewAdapter;
    StatisticsAdapter statisticsAdapter;
    MobileAdapter mobileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_show);

        ViewUtils.inject(this);
        iv_menu.setImageResource(R.mipmap.ic_back_arrow);
        tv_title.setText("场景展示");
        mIntent = getIntent();
        setupViewPager();
        getDate();
        CJLApplication application = (CJLApplication) getApplication();

    }

    //初始化viewPager
    private void setupViewPager() {
        setupViews();
        viewPagerAdapter = new ViewPagerAdapter(views);
        vp_navi.setAdapter(viewPagerAdapter);
        vp_navi.setCurrentItem(index_vp_overview);
        vp_navi.setOnPageChangeListener(this);
    }

    //初始化viewPager每页选项卡
    private void setupViews() {


        LayoutInflater inflater = LayoutInflater.from(this);
        View viewOver = inflater.inflate(R.layout.item_vp_overview, null);
        ListView lvOver = (ListView) viewOver.findViewById(R.id.lv_overview);
        lvOver.setAdapter(overviewAdapter = new OverviewAdapter(this, new ArrayList<SceneSummaryInfo>()));
        views.add(viewOver);
        View viewMobile = inflater.inflate(R.layout.item_vp_mobile, null);
        ListView lvMobile = (ListView) viewMobile.findViewById(R.id.lv_mobile);
        lvMobile.setAdapter(mobileAdapter = new MobileAdapter(this, new ArrayList<SceneMobileInfo>()));
        views.add(viewMobile);
        View viewStatistics = inflater.inflate(R.layout.item_vp_statistics, null);
        ListView lvStatistics = (ListView) viewStatistics.findViewById(R.id.lv_statistics);
        lvStatistics.setAdapter(statisticsAdapter = new StatisticsAdapter(this, new ArrayList<SceneContentInfo>()));
        views.add(viewStatistics);


    }

    @OnClick(R.id.iv_menu)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
        }
    }

    @OnRadioGroupCheckedChange(R.id.rg_navi)
    public void onNavigationChange(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_statistics:
                vp_navi.setCurrentItem(index_vp_statistics);
                break;
            case R.id.rb_mobile:
                vp_navi.setCurrentItem(index_vp_mobile);
                break;
            case R.id.rb_overview:
                vp_navi.setCurrentItem(index_vp_overview);
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d(this.getClass()+"",position+"");
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("position", position + "");
        switch (position) {

            case index_vp_mobile:
                rb_mobile.setChecked(true);
                break;
            case index_vp_overview:
                rb_overview.setChecked(true);
                break;
            case index_vp_statistics:
                rb_statistics.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.d(this.getClass()+"",state+"");
    }

    public void getDate() {


        RequestParams params = new RequestParams();
        String sid = mIntent.getStringExtra("id");
        Log.d("SceneShowActivity", sid);
        params.addQueryStringParameter("sid", sid);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                "http://scene.fjydhl.cn/Scene/Appscene/detail",
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
                            if (object.getInt("code") == 0) {

                                SceneInfo scene  = ParseUtil.parseScene(responseInfo.result);

                                overviewAdapter.setData(scene.getSummaries());
                                statisticsAdapter.setData(scene.getContents());
                                mobileAdapter.setData(scene.getMobiles());
                                BitmapUtils utils = new BitmapUtils(SceneShowActivity.this);
                                utils.display(iv_cover, CommonUtils.BASE_PATH + scene.getThumd());
                                tv_show.setText(scene.getSpread_count() + "");
                                tv_collect.setText(scene.getGather_data_count() + "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(SceneShowActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
