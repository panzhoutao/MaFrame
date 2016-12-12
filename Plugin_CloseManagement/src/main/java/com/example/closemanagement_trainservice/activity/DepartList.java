package com.example.closemanagement_trainservice.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.closemanagement_trainservice.R;
import com.example.closemanagement_trainservice.bean.DepartBean;
import com.example.closemanagement_trainservice.data.BaseApplication;
import com.google.gson.Gson;
import com.zhy.tree.bean.Node;
import com.zhy.tree.bean.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

public class DepartList extends BaseActivity{
    private List<DepartBean> departBean;


    private ListView mTree;
    private TreeListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depat_list);
        ButterKnife.bind(this);
        getTitleBar();
        getDepartData();  //获取部门全部信息

        seek();   //搜索事件

        listener();

    }

    private void listener() {
        sure.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void seek() {
        mTree = (ListView) findViewById(com.zhy.tree_view.R.id.id_tree);
        try {
            mAdapter = new TreeListViewAdapter(mTree, this, departBean, 1);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("code",node.getCode());
                    bundle.putString("name",node.getName());
                    bundle.putString("id",node.getId()+"");
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        mTree.setAdapter(mAdapter);
    }

    private void getDepartData() {
        String departStr = BaseApplication.app.getDepart();
        DepartBean[] depart = new Gson().fromJson(departStr, DepartBean[].class);
        departBean = Arrays.asList(depart);
    }


}
