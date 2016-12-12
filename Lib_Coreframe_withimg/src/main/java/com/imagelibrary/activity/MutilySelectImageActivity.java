package com.imagelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.imagelibrary.R;
import com.imagelibrary.imagescan.ChildAdapter;
import com.imagelibrary.imagescan.imageSelectListener;
import com.imagelibrary.imageshower.ImageDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MutilySelectImageActivity extends BaseSelectImageActivity implements
		imageSelectListener {
	public final static int ShowImageActivity = 1;
	private final static int PIC_LOAD_FINISH = 2;
	public static int FIN_MAX_SELECT = 1;
	public static int MAX_SELECT;

	public static final int RESULT_IMG = 103;

	private ChildAdapter adapter;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case PIC_LOAD_FINISH:
					mProgressDialog.dismiss();
					Intent intent = new Intent();
					@SuppressWarnings("unchecked")
					MapHolder holder = new MapHolder(
							(List<Map<String, Object>>) msg.obj);
					Bundle bundle = new Bundle();
					bundle.putSerializable("imgPaths", holder);
					intent.putExtras(bundle);
					setResult(RESULT_IMG, intent);
					finish();
					break;
			}
		}

	};


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FIN_MAX_SELECT=(getIntent().getIntExtra("max", 6));
		MAX_SELECT=FIN_MAX_SELECT-(getIntent().getIntExtra("count", 0));
		updateTitle(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CAMERA_TAKE:
				Log.i("CAMERA_TAKE", "CAMERA_TAKE");
				if (resultCode == RESULT_OK) {
					try {
						if (cramepath.length() > 0) {
							String savePath = appPath + "j_"
									+ cramepath;
							ImageFileCache.compressImage(appPath
									+ cramepath, savePath, true);
							Map<String, Object> mapdata = new HashMap<String, Object>();
							List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
							mapdata.put("path", savePath);
							listdata.add(mapdata);
							Message msg = new Message();
							msg.obj = listdata;
							msg.what = PIC_LOAD_FINISH;
							mHandler.sendMessage(msg);
						}
					} catch (Exception e) {
//						LogUtil.e("Exception", e.getMessage(), e);
						e.printStackTrace();
					}
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// Toast.makeText(this, "选中 " + adapter.getSelectItems().size() +
		// " item",
		// Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}

	@Override
	public void updateTitle(int count) {
		// TODO Auto-generated method stub
//		rightTxtBtn.setText("完成(" + count + "/" + MAX_SELECT + ")");

	}

	protected void commit(){
		mProgressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
				for (int i : adapter.getSelectItems()) {
					Map<String, Object> data = new HashMap<String, Object>();
					String savePath = appPath
							+ System.currentTimeMillis() + ".jpg";
					ImageFileCache.compressImage(Imagelist.get(i),
							savePath, false);
					data.put("path", savePath);
					listdata.add(data);
				}
				Message msg = new Message();
				msg.obj = listdata;
				msg.what = PIC_LOAD_FINISH;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	protected void initContentViewResource() {
		setContentView(R.layout.imagelib_layout_show_img);
	}

	@Override
	protected void initGridView() {
		// TODO Auto-generated method stub
		adapter = new ChildAdapter(MutilySelectImageActivity.this, Imagelist,
				mGridView,ShowImageActivity);
		adapter.setListener(this);
		mGridView.setAdapter(adapter);
	}

	@Override
	protected void refreshGridView() {
		// TODO Auto-generated method stub
		adapter.getmSelectMap().clear();
		adapter.notifyDataSetChanged();
	}
	private ImageDialog imageDialog;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (Imagelist.get(position).equals(CAMRE)) {
			takePic();
		}else{
			if(imageDialog!=null){
				if(imageDialog.isShowing()){
					return;
				}
			}
			imageDialog = new ImageDialog(
					MutilySelectImageActivity.this, R.style.imgDialog, Imagelist,
					position,false);
			imageDialog.setCanceledOnTouchOutside(false);
			imageDialog.show();
		}
	}
}