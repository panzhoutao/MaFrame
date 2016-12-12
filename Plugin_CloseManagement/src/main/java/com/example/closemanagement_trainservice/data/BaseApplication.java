package com.example.closemanagement_trainservice.data;

import android.app.Application;

/**
 * Created by 潘洲涛 on 2016/12/6.
 */

public class BaseApplication extends Application {
    public static BaseApplication app;
    private String depart;


    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }


    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }
}
