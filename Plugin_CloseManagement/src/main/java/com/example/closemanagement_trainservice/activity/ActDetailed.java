package com.example.closemanagement_trainservice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.closemanagement_trainservice.R;
import com.example.closemanagement_trainservice.bean.ListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActDetailed extends BaseActivity {

    private ListBean data;
    @BindView(R.id.detailed_remark)
    TextView remark;
    @BindView(R.id.detailed_find_user)
    TextView find_user;
    @BindView(R.id.detailed_dept_name)
    TextView dept_name;
    @BindView(R.id.detailed_zr_user)
    TextView zr_user;
    @BindView(R.id.detailed_rectification)
    TextView rectification;
    @BindView(R.id.detailed_status)
    TextView status1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act_detailed);
        ButterKnife.bind(this);
        getTitleBar();
        Intent intent = this.getIntent();
        data = (ListBean) intent.getSerializableExtra("data");

        remark.setText(getnull(data.getRemark()));
        find_user.setText(getnull(data.getFind_user()));
        dept_name.setText(getnull(data.getDept_name()));
        zr_user.setText(getnull(data.getZr_user()));
        rectification.setText(getnull(data.getRectification()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sure.setVisibility(View.GONE);

        String status = getnull(data.getStatus());

        if(status.equals("1")){
            status1.setText("发布");
        }else if(status.equals("100")){
            status1.setText("销号");
        }else if(status.equals("30")){
            status1.setText("待评价");
        }else if(status.equals("5")){
            status1.setText("采纳");
        }else if(status.equals("10")){
            status1.setText("拒绝");
        }else if(status.equals("20")){
            status1.setText("督办");
        }
    }
}
