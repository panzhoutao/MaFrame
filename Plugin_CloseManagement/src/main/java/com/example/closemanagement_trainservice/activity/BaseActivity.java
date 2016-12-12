package com.example.closemanagement_trainservice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.closemanagement_trainservice.R;

import butterknife.BindView;

import static com.example.closemanagement_trainservice.data.Data.token;
import static com.example.closemanagement_trainservice.data.Data.url;

/**
 * Created by 潘洲涛 on 2016/12/5.
 */

public class BaseActivity extends Activity{

    @BindView(R.id.title_back)
    ImageView back;
    @BindView(R.id.title_title)
    TextView title;
    @BindView(R.id.title_sure)
    TextView sure;
    @BindView(R.id.title_a)
    RelativeLayout a;
    @BindView(R.id.title_b)
    RelativeLayout b;

    protected void setToken(){
        Intent intent =getIntent();
        token = intent.getStringExtra("token");
        //token = "15D12AB40DFD0FFDADE76A8ADCD3B820";
        //url = "http://10.10.2.231:8580/";
        url = intent.getStringExtra("webUrl");
    }

    /***
     * 非空处理
     */
    protected String getnull(String values) {
        if (values == null) {
            return "";
        } else if ("null".equals(values)) {
            return "";
        } else {
            return values;
        }
    }

    public void getTitleBar() {
        android.view.ViewGroup.LayoutParams aa = a.getLayoutParams();
        android.view.ViewGroup.LayoutParams bb = b.getLayoutParams();
        aa.height = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            aa.height = getStatusBarHeight() + bb.height;
        }else{
            aa.height = bb.height ;
        }
        a.setLayoutParams(aa);//把banner图的高度设为此宽度值
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
