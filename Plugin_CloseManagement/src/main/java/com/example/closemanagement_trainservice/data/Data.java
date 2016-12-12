package com.example.closemanagement_trainservice.data;

import android.content.Context;
import android.util.Log;

import com.cydroid.coreframe.model.ModelCompleteCallback;
import com.cydroid.coreframe.model.base.BaseHttpAsyncModel;
import com.cydroid.coreframe.model.base.BaseResponesBean;
import com.cydroid.coreframe.tool.util.JsonUtil;
import com.cydroid.coreframe.web.http.HttpJsonInterface;
import com.example.closemanagement_trainservice.bean.Bean;
import com.example.closemanagement_trainservice.bean.DepartBean;
import com.example.closemanagement_trainservice.bean.ListBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 潘洲涛 on 2016/9/1.
 */
public class Data extends BaseHttpAsyncModel<Bean> {

    JSONArray jsonArray = null;
    private BaseResponesBean bean;
    public static String token = "";
    public static String url = "";

    public Data(Context context, ModelCompleteCallback<BaseResponesBean> callback) {
        super(context, callback);
    }

    public Data(Context context) {
        super(context);
    }

    @Override
    protected BaseResponesBean doInBackground(String... strings) {
        return getContracts(context);
    }

    private BaseResponesBean getContracts(Context context){

        if(holder.getStatus()== null   && holder.getCode()== null ){
            bean = HttpJsonInterface.getInstance().doGet(url+"safeSupervision/list?token="+token+"&pageNumber="+holder.getPageNumber());
        }else{
            bean = HttpJsonInterface.getInstance().doGet(url+"safeSupervision/list?token="+token+"&pageNumber="+holder.getPageNumber()+"&zrdep_id="+holder.getId()+"&zrdep_code="+holder.getCode()+"&status="+holder.getStatus());

        }

        ArrayList<ListBean> list = null;
        try {
            JSONObject json=bean.getJson().getJSONObject("page");
            Bean isPage = JsonUtil.parseJson(json.toString(),Bean.class);
            bean.setEquipmentholder(isPage);
            String json1=bean.getJson().getString("searchDeptNodes");
            bean.setDepartholder(json1);
            jsonArray=json.getJSONArray("list");
            list= JsonUtil.parseArray(jsonArray, ListBean.class);
            bean.setDataholder(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

}
