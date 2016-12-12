package com.zhy.tree_view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.zhy.bean.FileBean;
import com.zhy.tree.bean.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MainActivity extends Activity {
	private List<FileBean> mDatas2 = new ArrayList<FileBean>();
	private ListView mTree;
	private TreeListViewAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		initDatas();
//		mTree = (ListView) findViewById(R.id.id_tree);
//		try {
//			mAdapter = new SimpleTreeAdapter<FileBean>(mTree, this, mDatas2, 10);
//			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
//				@Override
//				public void onClick(Node node, int position) {
//					if (node.isLeaf()) {
//						Toast.makeText(getApplicationContext(), node.getName(),
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		mTree.setAdapter(mAdapter);

	}

//	private void initDatas() {
//		mDatas2.add(new FileBean(1, 0, "文件管理系统"));
//		mDatas2.add(new FileBean(2, 1, "游戏"));
//		mDatas2.add(new FileBean(3, 1, "文档"));
//		mDatas2.add(new FileBean(4, 1, "程序"));
//		mDatas2.add(new FileBean(5, 2, "war3"));
//		mDatas2.add(new FileBean(6, 2, "刀塔传奇"));
//
//		mDatas2.add(new FileBean(7, 4, "面向对象"));
//		mDatas2.add(new FileBean(8, 4, "非面向对象"));
//
//		mDatas2.add(new FileBean(9, 7, "C++"));
//		mDatas2.add(new FileBean(10, 7, "JAVA"));
//		mDatas2.add(new FileBean(11, 7, "Javascript"));
//		mDatas2.add(new FileBean(12, 8, "C"));
//	}

}
