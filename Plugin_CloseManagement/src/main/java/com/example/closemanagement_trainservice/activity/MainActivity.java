package com.example.closemanagement_trainservice.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cydroid.coreframe.model.ModelCompleteCallback;
import com.cydroid.coreframe.model.base.BaseResponesBean;
import com.example.closemanagement_trainservice.R;
import com.example.closemanagement_trainservice.adapter.MyAdapter;

import com.example.closemanagement_trainservice.bean.Bean;
import com.example.closemanagement_trainservice.bean.ListBean;
import com.example.closemanagement_trainservice.data.BaseApplication;
import com.example.closemanagement_trainservice.data.Data;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, ModelCompleteCallback<BaseResponesBean> {

    @BindView(R.id.list)
    PullToRefreshListView lv;
    private ArrayList<ListBean> list = null;
    private MyAdapter adapter;
    private Bean bean = new Bean();
    @BindView(R.id.shaixuan)
    LinearLayout shaiXuan;
    private String depart=null;
    private String statu = "";
    private String id;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getTitleBar();
        //setTitleBar();
        setToken();
        init();
        listener();
        getData();

    }

    private void listener() {
        lv.setOnRefreshListener(this);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        shaiXuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                BaseApplication.app.setDepart(depart);
                startActivityForResult(intent.setClass(MainActivity.this,ShaiXuan.class),1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.getRefreshableView(). smoothScrollToPositionFromTop(0,0);
            }
        });
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                lv.setMode(PullToRefreshBase.Mode.BOTH);
                Bundle data1 = data.getExtras();
                id = data1.getString("id");
                statu = data1.getString("status");
                code = data1.getString("code");
                bean.setStatus(statu);
                bean.setCode(code);
                bean.setId(id);
                init();
                getData();
                break;
        }
    }

    private void init() {
        sure.setVisibility(View.GONE);
        adapter = new MyAdapter(this);
        lv.setAdapter(adapter);
    }

    private void getData() {

        new Data(this, this).excuteParams(bean);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        bean.setPageNumber(0);
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

        bean.setPageNumber(bean.getPageNumber() + 1);
        getData();
    }

    //数据获取
    @Override
    public void onTaskPostExecute(int taskid, BaseResponesBean result) {
        if (result != null) {
            Bean page = (Bean) result.getEquipmentholder();
            list = (ArrayList<ListBean>) result.getDataholder();
            depart =  result.getDepartholder();
            if(page!=null){
                adapter.addData(list, page.getFirstPage());
                if (page.getLastPage()) {
                    lv.onRefreshComplete();
                    lv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                }
            }
            lv.onRefreshComplete();
        }
        lv.onRefreshComplete();
    }
}
