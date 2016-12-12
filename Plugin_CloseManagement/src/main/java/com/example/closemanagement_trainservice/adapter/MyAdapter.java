package com.example.closemanagement_trainservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.closemanagement_trainservice.R;
import com.example.closemanagement_trainservice.activity.ActDetailed;
import com.example.closemanagement_trainservice.bean.ListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 潘洲涛 on 2016/12/4.
 */

public class MyAdapter extends MyBaseAdapter<ListBean> {

    public MyAdapter(Context c) {
        super(c);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view = inflater.inflate(R.layout.question_item,null);
            viewHolder =new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        final ListBean data = list.get(i);
        viewHolder.remark.setText(getnull(data.getRemark()));
        viewHolder.find_user.setText(getnull(data.getFind_user()));
        viewHolder.dept_name.setText(getnull(data.getDept_name()));
        viewHolder.zr_user.setText(getnull(data.getZr_user()));
        viewHolder.rectification.setText(getnull(data.getRectification()));
        String status = getnull(data.getStatus());

        if(status.equalsIgnoreCase("1")){
            viewHolder.status.setText("发布");
        }else if(status.equals("100")){
            viewHolder.status.setText("销号");
        }else if(status.equals("30")){
            viewHolder.status.setText("待评价");
        }else if(status.equals("5")){
            viewHolder.status.setText("采纳");
        }else if(status.equals("10")){
            viewHolder.status.setText("拒绝");
        }else if(status.equals("20")){
            viewHolder.status.setText("督办");
        }

        //按钮点击事件
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",data);
                intent.putExtras(bundle);
                context.startActivity(intent.setClass(context,ActDetailed.class));
            }
        });

        return view;
    }

    class ViewHolder{
        @BindView(R.id.remark)
        TextView remark;
        @BindView(R.id.find_user)
        TextView find_user;
        @BindView(R.id.dept_name)
        TextView dept_name;
        @BindView(R.id.zr_user)
        TextView zr_user;
        @BindView(R.id.rectification)
        TextView rectification;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.itemBtn)
        Button btn;

        ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }


}
