package com.example.closemanagement_trainservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有适配器的父类
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    // 上下文对象
    protected Context context;
    // 布局填充器
    protected LayoutInflater inflater;
    // 泛型集合
    protected ArrayList<T> list = new ArrayList<T>();


    public MyBaseAdapter(Context c) {
        super();
        // 传递activity
        this.context = c;
        inflater = LayoutInflater.from(context);
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

    /**
     * 获取颜色
     */
    protected int getcolor(int rid) {
        return context.getResources().getColor(rid);
    }

    /**
     * 格式化时间
     */
//    protected String formatDate(long date) {
//        return BaseActivity.forDate(date);
//    }

    /**
     * 返回适配器中数据
     */
    public ArrayList<T> getAdapterData() {
        return list;
    }

    /**
     * 添加多条记录
     *
     * @param alist      数据集合
     * @param isClearOld 是否清空原数据
     */
    public void addData(List<T> alist, boolean isClearOld) {
        if (alist == null) {
            return;
        }
        if (isClearOld) {
            list.clear();
        }
        list.addAll(alist);
        updateAdapter();
    }

    /**
     * 更新适配器数据
     */
    public void updateAdapter() {
        this.notifyDataSetChanged();
    }

    /**
     * 清空适配器数据
     */
    public void clearAdapter() {
        list.clear();
        updateAdapter();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }

    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        if (list == null) {
            return null;
        }
        // 如果已经没有数据了返回空
        if (position > list.size() - 1) {
            return null;
        }
        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        return getMyView(position, convertView, parent);
    }

    /**
     * 用户必须实现此方法 加载不同的布局
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getMyView(int position, View convertView,
                                   ViewGroup parent);


}
