package com.ydhl.cjl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.image.SmartImageView;
import com.umeng.socialize.bean.SHARE_MEDIA;
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
import com.ydhl.cjl.activity.SceneShowActivity;
import com.ydhl.cjl.activity.ShowScenesActivity;
import com.ydhl.cjl.activity.SingleSceneActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的场景
 */
public class ScenesFragment extends Fragment {

    private ArrayList<ScenesData> mData = new ArrayList<ScenesData>();
    private BitmapUtils bitmapUtils;
    private MAdapter mAdapter;
    private UMSocialService umService;


    private String url = "http://scene.fjydhl.cn/Scene/Appscene/scenelist?uid=";
    private MHandler handler = new MHandler();


    @ViewInject(R.id.scenes_pl_1)
    private PullToRefreshListView mListVew;

    @ViewInject(R.id.iv_guide)
    private ImageView iv_guide;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenes, container, false);
        ViewUtils.inject(this, view);
        initView();
        loadData();
        umService = UMServiceFactory.getUMSocialService("com.umeng.share");
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("life", "hide");
        mListVew.onRefreshComplete();
    }

    /**
     * 初始化界面
     */
    private void initView() {

        bitmapUtils = new BitmapUtils(getActivity());
        mListVew.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));

        mListVew.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                loadData();
            }
        });

        mAdapter = new MAdapter();
        mListVew.setAdapter(mAdapter);
        if(mAdapter.getCount()==0){
            iv_guide.setVisibility(View.VISIBLE);
        }else {
            iv_guide.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 更新场景显示数据
     */
    private void updateData(JSONObject object) {

        try {

            JSONArray array = object.getJSONArray("data");
            int size = array.length();

            if (size < 1) {
                Toast.makeText(ScenesFragment.this.getActivity(), "还没有任何数据", Toast.LENGTH_SHORT).show();
                return;
            }
            //清空原有数据
            mData.clear();

            for (int i = 0; i < size; i++) {

                JSONObject tempObject = array.getJSONObject(i);

                ScenesData data01 = new ScenesData();
                data01.setTitle(tempObject.getString("title"));
                data01.setShare_url(tempObject.getString("share_url"));
                data01.setGather_data_count(tempObject.getString("gather_data_count"));
                data01.setSid(tempObject.getString("sid"));
                data01.setSpread_data_count(tempObject.getString("spread_data_count"));
                data01.setThumb(tempObject.getString("thumb"));
                mData.add(data01);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

            handler.sendEmptyMessage(200);
        }


    }

    /**
     * 从服务器加载数据
     */
    private void loadData() {

        String mUid = (String) ((CJLApplication) getActivity().getApplication()).getAppData(CJLApplication.KEY_UID);
        String str_get = url + mUid;

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, str_get,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result);
                            Log.i("scenes", responseInfo.result);
                            Log.i("shen", "scenes:" + responseInfo.result);
                            int code = object.getInt("code");

                            if (code != 0) {
                                Toast.makeText(ScenesFragment.this.getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                updateData(object);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            handler.sendEmptyMessage(200);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(ScenesFragment.this.getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(200);
                    }
                });
    }


    /**
     * 分享到第三方社交平台
     *
     * @param title     分享的主题名
     * @param content   分享的文字内容
     * @param targetUrl 分享所带的链接
     */
    private void shareTo(String title, String imageUrl, String content, String targetUrl) {


        UMImage image = new UMImage(getActivity(), imageUrl);
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
        SinaShareContent sinaShareContent = new SinaShareContent();
        sinaShareContent.setShareContent(content);
        sinaShareContent.setTitle(title);
        sinaShareContent.setShareImage(image);
        sinaShareContent.setTargetUrl(targetUrl);
        umService.setShareMedia(sinaShareContent);

        TencentWbShareContent tencentWbShareContent = new TencentWbShareContent();
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

        umService.openShare(getActivity(), false);
    }


    /**
     * 适配器生成View方法
     *
     * @param position
     * @return
     */
    private View generateView(int position) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_scenes, null);

        SmartImageView iv_scenes = (SmartImageView) view.findViewById(R.id.scenes_lv_iv_1);
        ImageView iv_share = (ImageView) view.findViewById(R.id.scenes_lv_iv_share);
        TextView tv_show = (TextView) view.findViewById(R.id.scenes_lv_tv_show);
        TextView tv_collection = (TextView) view.findViewById(R.id.scenes_lv_tv_collection);
        TextView tv_name = (TextView) view.findViewById(R.id.scenes_lv_tv_name);


        final ScenesData childData = mData.get(position);

        tv_show.setText(childData.getSpread_data_count());
        tv_collection.setText(childData.getGather_data_count());
        tv_name.setText(childData.getTitle());


        //场景展示
        view.findViewById(R.id.scenes_ll_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Integer.parseInt(childData.getSpread_data_count()) == 0) {
                    Toast.makeText(ScenesFragment.this.getActivity(), "这里还没有什么数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                ;
                Intent mIntent = new Intent(ScenesFragment.this.getActivity(), SceneShowActivity.class);
                mIntent.putExtra("id", childData.getSid());
                ScenesFragment.this.startActivity(mIntent);
            }
        });


        //数据收集
        view.findViewById(R.id.scenes_ll_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(childData.getGather_data_count()) == 0) {
                    Toast.makeText(ScenesFragment.this.getActivity(), "这里还没有什么数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                ;
                Intent mIntent = new Intent(ScenesFragment.this.getActivity(), SingleSceneActivity.class);
                mIntent.putExtra("id", childData.getSid());
                mIntent.putExtra("name", childData.getTitle());
                ScenesFragment.this.startActivity(mIntent);
            }
        });


        iv_scenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(ScenesFragment.this.getActivity(), ShowScenesActivity.class);
                mIntent.putExtra("address", childData.getShare_url());
                ScenesFragment.this.startActivity(mIntent);

            }
        });


        //分享点击事件
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareTo("微场景", childData.getThumb(), childData.getTitle(), childData.getShare_url());

            }
        });


        iv_scenes.setImageUrl(childData.getThumb());

        return view;


    }

    /**
     * 数据结构类，存储每个场景项的基本信息
     */
    private class ScenesData {

        private String sid;
        private String share_url;
        private String spread_data_count;
        private String gather_data_count;
        private String title;
        private String thumb;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getSpread_data_count() {
            return spread_data_count;
        }

        public void setSpread_data_count(String spread_data_count) {
            this.spread_data_count = spread_data_count;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getGather_data_count() {
            return gather_data_count;
        }

        public void setGather_data_count(String gather_data_count) {
            this.gather_data_count = gather_data_count;
        }
    }

    /**
     * 自定义适配器
     */
    private class MAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return generateView(position);
        }
    }

    /**
     * Handler
     */
    private class MHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 200) {
                mAdapter.notifyDataSetChanged();
                mListVew.onRefreshComplete();
                if(mAdapter.getCount()==0){
                    iv_guide.setVisibility(View.VISIBLE);
                }else {
                    iv_guide.setVisibility(View.INVISIBLE);
                }
            }

        }
    }


}
