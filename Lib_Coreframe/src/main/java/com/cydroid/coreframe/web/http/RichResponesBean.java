package com.cydroid.coreframe.web.http;

import com.cydroid.coreframe.model.base.BaseResponesBean;

public class RichResponesBean<T> extends BaseResponesBean {
	private T content;
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
	public RichResponesBean() {
		super();
	}
}
