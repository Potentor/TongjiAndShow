package com.ydhl.cjl.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ydhl.cjl.CJLApplication;
import com.ydhl.cjl.R;

import com.ydhl.cjl.activity.LoginActivity;
import com.ydhl.cjl.activity.UserCentralActivity;
import com.ydhl.cjl.view.RoundedImageView;


/**
 * 主界面侧滑菜单
 */
public class MenuFragment extends Fragment {

    @ViewInject(R.id.menu_tv_account)
    private TextView tv_acount;

    @ViewInject(R.id.menu_iv_potrait)
    private RoundedImageView iv_portrait;

    @ViewInject(R.id.menu_iv_setting)
    private ImageView iv_setting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
        ViewUtils.inject(this,view);
        loadPotrainAndName();
        return view;
    }


    /**
     * 加载头像和用户名
     */
    private void loadPotrainAndName(){

       // BitmapUtils bitmapUtils=new BitmapUtils(getActivity());
        CJLApplication application=(CJLApplication)getActivity().getApplication();
      //  BitmapDisplayConfig config=new BitmapDisplayConfig();
      //  config.setBitmapMaxSize(new BitmapSize(100,100));


//        bitmapUtils.display(iv_portrait,(String)application.getAppData("imageUrl"),config,new BitmapLoadCallBack<RoundedImageView>() {
//            @Override
//            public void onLoadCompleted(RoundedImageView roundedImageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
//                Toast.makeText(MenuFragment.this.getActivity(),"加载头像失败",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLoadFailed(RoundedImageView roundedImageView, String s, Drawable drawable) {
//
//                Toast.makeText(MenuFragment.this.getActivity(),"加载头像失败",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//


        iv_portrait.setImageUrl((String)application.getAppData("imageUrl"));
        tv_acount.setText((String)application.getAppData("displayName"));


    }


    /**
     * 设置按钮点击事件
     * @param view
     */
    @OnClick(R.id.menu_iv_setting)
    public void setTing(View view){

        Intent mIntent=new Intent(this.getActivity(),UserCentralActivity.class);
        startActivityForResult(mIntent,300);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&resultCode==300){
            Intent mItent=new Intent(getActivity(),LoginActivity.class);
            startActivity(mItent);
            getActivity().finish();
            SharedPreferences.Editor editor=getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE).edit();
            editor.putString("login_name","");
            editor.putString("password","0#");
            editor.commit();
        }
    }
}
