package com.imagelibrary.imagescan;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


import com.imagelibrary.R;
import com.imagelibrary.activity.BaseSelectImageActivity;
import com.imagelibrary.activity.MutilySelectImageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ChildAdapter extends BaseAdapter {
	private Point mPoint = new Point(600, 600);;//用来封装ImageView的宽和高的对象
	private imageSelectListener listener;
	/**
	 * 用来存储图片的选中情况
	 */
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private GridView mGridView;
	private List<String> list;
	protected LayoutInflater mInflater;
	private Context context;
//	FragmentActivity T;
	int MAX_SELECT;

	public HashMap<Integer, Boolean> getmSelectMap() {
		return mSelectMap;
	}

	public void setmSelectMap(HashMap<Integer, Boolean> mSelectMap) {
		this.mSelectMap = mSelectMap;
	}

	public imageSelectListener getListener() {
		return listener;
	}

	public void setListener(imageSelectListener listener) {
		this.listener = listener;
	}
//	Activity aty;
//	ShowImageActivity show
	int flag=-1;//用于判断是哪个类调用了这个适配器

	public ChildAdapter(Context context, List<String> list, GridView mGridView, int flag) {
		this.context=context;
		this.list = list;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
		this.flag=flag;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
	//	private static final int FADE_IN_TIME = 200;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list.get(position);

		if(convertView == null){
			convertView = mInflater.inflate(R.layout.imagelib_grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);

			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.mImageView.setTag(path);
		viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//如果是未选中的CheckBox,则添加动画
				boolean unhaschoose=!mSelectMap.containsKey(position) || !mSelectMap.get(position);
//				if(aty instanceof  Sh)
				MAX_SELECT= MutilySelectImageActivity.MAX_SELECT;
//				if(unhaschoose&&mSelectMap.size()>ShowImageActivity.MAX_SELECT-1){
				if(unhaschoose&&mSelectMap.size()>MAX_SELECT-1){
					viewHolder.mCheckBox.setChecked(false);
					Toast.makeText(context, "最多只能选取" + MAX_SELECT + "张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				if(unhaschoose){
					addAnimation(viewHolder.mCheckBox);
				}
				if(!isChecked){
					mSelectMap.remove(position);
				}else{
					mSelectMap.put(position, isChecked);
				}
				if(listener!=null){
					listener.updateTitle(mSelectMap.size());
				}
			}
		});

		viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(position) ? mSelectMap.get(position) : false);
		viewHolder.mCheckBox.setVisibility(path.equals(BaseSelectImageActivity.CAMRE)? View.GONE: View.VISIBLE);
		if(path.equals(BaseSelectImageActivity.CAMRE)){
			viewHolder.mImageView.setImageResource(android.R.drawable.ic_menu_camera);
			return convertView;
		}
		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance(context).loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {

			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					mImageView.setImageBitmap(bitmap);
				}
			}
		});

		if(bitmap != null){
			viewHolder.mImageView.setImageBitmap(bitmap);
		}else{
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}

		return convertView;
	}

	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
	 * @param view
	 */
	private void addAnimation(View view){
		float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}


	/**
	 * 获取选中的Item的position
	 * @return
	 */
	public List<Integer> getSelectItems(){
		List<Integer> list = new ArrayList<Integer>();
		for(Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Integer, Boolean> entry = it.next();
			if(entry.getValue()){
				list.add(entry.getKey());
			}
		}

		return list;
	}


	public static class ViewHolder{
		public MyImageView mImageView;
		public CheckBox mCheckBox;
	}



}
