package com.imagelibrary.imagescan;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.imagelibrary.R;

import java.util.List;

/**
 * @blog http://blog.csdn.net/xiaanming
 *
 * @author xiaanming
 *
 *
 */
public class SpinnerPathPopupwin extends PopupWindow {
	private GroupAdapter adapter;
	private ListView mGroupGridView;
	public static final String ALL="所有";

	public interface SpinnerPathPopupCallBack{
		void onClick(int position, String parent);
	}
	private SpinnerPathPopupCallBack mCallback;
	public SpinnerPathPopupwin(Context context, SpinnerPathPopupCallBack callback,final List<ImageBean> list) {
		this.mCallback=callback;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.imagelib_layout_select_img,
				null);
		mGroupGridView = (ListView) contentView.findViewById(R.id.main_grid);

		mGroupGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				mCallback.onClick(position,list.get(position).getFolderName());
			}
		});
		adapter = new GroupAdapter(context, list, mGroupGridView);
		mGroupGridView.setAdapter(adapter);
		this.setContentView(contentView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(DensityUtil.dip2px(context, 400));
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
	}

}
