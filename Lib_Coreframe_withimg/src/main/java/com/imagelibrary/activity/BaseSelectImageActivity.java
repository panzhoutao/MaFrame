package com.imagelibrary.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.imagelibrary.R;
import com.imagelibrary.imagescan.ImageBean;
import com.imagelibrary.imagescan.SpinnerPathPopupwin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class BaseSelectImageActivity extends AppCompatActivity implements
		OnItemClickListener {
	protected GridView mGridView;
	protected final static int SCAN_OK = 1;
	public final static String CAMRE = "com.tingshuo.hearfrom.camre";
	protected ProgressDialog mProgressDialog;
	protected List<String> Imagelist = new ArrayList<String>();
	private HashMap<String, List<String>> mGroupMap = new HashMap<String, List<String>>();
	private TextView spinner;
	private SpinnerPathPopupwin spinnerPopupwin;
	public final static String ALL="所有图片";
	protected Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SCAN_OK:
					// 关闭进度条
					mProgressDialog.dismiss();
					initGridView();
					break;
			}
		}

	};
	abstract protected void initContentViewResource();
	protected abstract void initGridView();

	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	public static final String appPath = Environment
			.getExternalStorageDirectory() + "/.xianzhi/";
	protected String cramepath;
	public static final int CAMERA_TAKE = 1001;

	protected void takePic() {
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (isHasSdcard()) {
			cramepath = System.currentTimeMillis() + ".jpg";
			camera.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(appPath + cramepath)));
		} else {
			Toast.makeText(getApplicationContext(), "没有找到sd卡",
					Toast.LENGTH_LONG).show();
		}
		startActivityForResult(camera, CAMERA_TAKE);
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContentViewResource();

		initProgressDialog("正在加载图片...");
		mGridView = (GridView) findViewById(R.id.child_grid);
		mGridView.setOnItemClickListener(this);
		getImages();
		spinner = (TextView) findViewById(R.id.spinner);
		spinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSpinnerPopup();
			}
		});
	}

	private SpinnerPathPopupwin.SpinnerPathPopupCallBack mCallback=new SpinnerPathPopupwin.SpinnerPathPopupCallBack() {

		@Override
		public void onClick(int position,String parentString) {
			// TODO Auto-generated method stub
			spinnerPopupwin.dismiss();
			if(Imagelist!=null){
				Imagelist.clear();
				Imagelist.addAll(mGroupMap.get(parentString));
			}
			spinner.setText(parentString);
			refreshGridView();
		}
	};
	protected abstract void refreshGridView();
	private void showSpinnerPopup(){
		if(spinnerPopupwin==null){
			spinnerPopupwin=new SpinnerPathPopupwin(getApplicationContext()
					, mCallback, subGroupOfImage(mGroupMap));
		}
//		spinnerPopupwin.showAsDropDown(findViewById(R.id.child_grid));
		spinnerPopupwin.showAtLocation(
				this.findViewById(R.id.child_grid), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0
				, 0);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initProgressDialog(String content) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("");
		mProgressDialog.setMessage(content);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		Imagelist.clear();
		//如果是物业服务的选择多张图片的情况，就不让拍照
//		if(!( this instanceof TenementImageActivity)){
		Imagelist.add(CAMRE);
//		}
//		Imagelist.add(CAMRE);

		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = BaseSelectImageActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED+" desc");
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					// 获取该图片的父路径名
					String parentName = new File(path).getParentFile()
							.getName();

					Imagelist.add(path);
					if (!mGroupMap.containsKey(parentName)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGroupMap.put(parentName, chileList);
					} else {
						mGroupMap.get(parentName).add(path);
					}
				}
				List<String> all=new ArrayList<String>();
				all.addAll(Imagelist);
				mGroupMap.put(ALL, all);

				mCursor.close();

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);

			}
		}).start();

	}

	private List<ImageBean> subGroupOfImage(
			HashMap<String, List<String>> mGruopMap) {
		if (mGruopMap.size() == 0) {
			return null;
		}
		List<ImageBean> list = new ArrayList<ImageBean>();
		List<String> allvalue = mGruopMap.get(ALL);
		ImageBean AllImageBean = new ImageBean();
		AllImageBean.setFolderName(ALL);
		AllImageBean.setImageCounts(allvalue.size());
		AllImageBean.setTopImagePath(allvalue.get(0));
		list.add(AllImageBean);

		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageBean mImageBean = new ImageBean();
			String key = entry.getKey();
			if(key.equals(ALL)){
				continue;
			}
			List<String> value = entry.getValue();

			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片

			list.add(mImageBean);
		}
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (Imagelist.get(position).equals(CAMRE)) {
			takePic();
		}
	}
}
