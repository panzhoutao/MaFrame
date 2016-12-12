package com.example.closemanagement_trainservice.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cydroid.coreframe.tool.util.ToastUtil;
import com.example.closemanagement_trainservice.R;
import com.example.closemanagement_trainservice.bean.DepartBean;
import com.example.closemanagement_trainservice.data.BaseApplication;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShaiXuan extends BaseActivity {

    @BindView(R.id.btn_depart)
    LinearLayout depart;
    @BindView(R.id.btn_depart_tv)
    TextView depart_tv;
    @BindView(R.id.btn_status)
    LinearLayout status;
    @BindView(R.id.btn_status_tv)
    TextView status_tv;
    @BindView(R.id.btn_shaixuan)
    Button btn;

    private String statuStr="";
    private String statu = "";
    private String id ="";
    private String code ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shai_xuan);
        ButterKnife.bind(this);
        getTitleBar();
        clickListener();
    }

    private void clickListener() {

        depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getDepart()!=null){
                    startActivityForResult(new Intent(ShaiXuan.this,DepartList.class),1);
                }else{
                    ToastUtil.showShort(getApplicationContext(),"没有数据");
                }

            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("code",code);
                bundle.putString("id",id);
                bundle.putString("status",statu);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        sure.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                Bundle data1 = data.getExtras();
                id = data1.getString("id");
                String name = data1.getString("name");
                code = data1.getString("code");
                depart_tv.setText(name);

                break;

        }
    }

    private void dialog(){
        final String items[]={"全部","发布","销号","待评价","采纳","拒绝","督办"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器

        builder.setItems(items,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                statuStr = items[i];
                dialog.dismiss();
                if(statuStr.equals("发布")){
                    statu = "1";
                }else if(statuStr.equals("销号")){
                    statu = "100";;
                }else if(statuStr.equals("待评价")){
                    statu = "30";;
                }else if(statuStr.equals("采纳")){
                    statu = "5";;
                }else if(statuStr.equals("拒绝")){
                    statu = "10";;
                }else if(statuStr.equals("督办")){
                    statu = "20";
                }else{
                    statu = "";
                }
                status_tv.setText(statuStr);
                Toast.makeText(ShaiXuan.this, items[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
}
