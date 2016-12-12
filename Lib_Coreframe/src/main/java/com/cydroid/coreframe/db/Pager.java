package com.cydroid.coreframe.db;

public class Pager {
	public int curpage=1;
	public int pagesize;
	public int minId;
	public int maxId;
	public static int Default_Page = 20;
	public Pager(int curpage, int pagesize) {
		this.curpage = curpage;
		this.pagesize = pagesize;
	}

}
