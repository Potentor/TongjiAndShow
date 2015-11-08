package com.ydhl.cjl.utils;

import android.util.Log;

import com.umeng.socialize.net.o;
import com.ydhl.cjl.db.entity.ClientInfo;
import com.ydhl.cjl.db.entity.SceneContentInfo;
import com.ydhl.cjl.db.entity.SceneInfo;
import com.ydhl.cjl.db.entity.SceneMobileInfo;
import com.ydhl.cjl.db.entity.SceneSummaryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/8.
 */
public class ParseUtil {

    public static SceneInfo parseScene(String json)  {
        SceneInfo scene = new SceneInfo();
        List<SceneSummaryInfo> summarys = new ArrayList<>();
        List<SceneMobileInfo> mobiles = new ArrayList<>();
        List<SceneContentInfo> contents = new ArrayList<>();
        try {

            JSONObject object1 = new JSONObject(json).getJSONObject("data");
            JSONObject object = object1.getJSONObject("scene");
            scene.setThumd(object.getString("thumb"));
            scene.setSpread_count(object.getInt("spread_count"));
            scene.setGather_data_count(object.getInt("gather_data_count"));
            JSONArray arrSum = object1.getJSONArray("summary");
            summarys = new ArrayList<>();
            if (arrSum != null) {
                for (int i = 0; i < arrSum.length(); i++) {
                    SceneSummaryInfo summary = new SceneSummaryInfo();
                    summary.setDate(arrSum.getJSONObject(i).getString("date"));
                    summary.setGather_data_count(arrSum.getJSONObject(i).getInt("gather_data_count"));
                    summary.setSpread_count(arrSum.getJSONObject(i).getInt("spread_count"));
                    summarys.add(summary);
                }
            }
            JSONArray arrMob = object1.getJSONArray("mobile");

            mobiles = new ArrayList<>();
            if (arrMob != null)
                for (int i = 0; i < arrMob.length(); i++) {
                    SceneMobileInfo mobile = new SceneMobileInfo();
                    mobile.setDate(arrMob.getJSONObject(i).getString("date"));
                    mobile.setWx_friends_count(arrMob.getJSONObject(i).getInt("wx_friends_count"));
                    mobile.setWx_chat_content(arrMob.getJSONObject(i).getInt("wx_chat_count"));
                    mobile.setWx_group_count(arrMob.getJSONObject(i).getInt("wx_group_count"));
                    mobiles.add(mobile);
                }
            JSONArray arrCon = object1.getJSONArray("content");

           contents = new ArrayList<>();
            if (arrCon != null)
                for (int i = 0; i < arrCon.length(); i++) {
                    SceneContentInfo content = new SceneContentInfo();
                    content.setDate(arrCon.getJSONObject(i).getString("date"));
                    content.setLink_count(arrCon.getJSONObject(i).getInt("link_count"));
                    content.setCall_count(arrCon.getJSONObject(i).getInt("call_count"));
                    contents.add(content);
                }
        }catch (JSONException e){
            e.printStackTrace();
        }
        scene.setContents(contents);
        scene.setMobiles(mobiles);
        scene.setSummaries(summarys);
        return scene;
    }

    public static List<ClientInfo> parseClientInfo(String json) throws JSONException {
        List<ClientInfo> clientInfos = new ArrayList<>();
        JSONArray array = new JSONObject(json).getJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            ClientInfo client = new ClientInfo();
            client.setC_name(object.getString("name"));
            client.setC_phoneNum(object.getString("phone"));
            client.setC_sex(object.getString("gender"));
            client.setC_email(object.getString("email"));
            client.setC_sector(object.getString("company"));
            client.setC_job(object.getString("bussiness"));
            client.setC_address(object.getString("address"));
            client.setC_net(object.getString("website"));
            client.setC_qq(object.getString("qq"));
            client.setC_team(object.getString("team"));

            clientInfos.add(client);
        }
        return clientInfos;
    }
}
