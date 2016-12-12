package com.cydroid.coreframe.model.base;

import org.json.JSONObject;

public class BaseResponesBean {
	private int status;
	private int interface_status;
	private String info;
	private String jsonString;
	private JSONObject json;
	private Object dataholder;
	private Object equipmentholder;
	private String departholder;


	public String getDepartholder() {
		return departholder;
	}

	public void setDepartholder(String departholder) {
		this.departholder = departholder;
	}




	public Object getEquipmentholder() {
		return equipmentholder;
	}

	public void setEquipmentholder(Object equipmentholder) {
		this.equipmentholder = equipmentholder;
	}

	public Object getDataholder() {
		return dataholder;
	}

	public void setDataholder(Object dataholder) {
		this.dataholder = dataholder;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public int getInterface_status() {
		return interface_status;
	}

	public void setInterface_status(int interface_status) {
		this.interface_status = interface_status;
	}

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public JSONObject getJson() {
		return json;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}
	public BaseResponesBean(int status,String info,JSONObject json) {
		super();
		this.status = status;
		this.info=info;
		this.json = json;
	}
	public BaseResponesBean() {
		super();
	}

	@Override
	public String toString() {
		return "BaseResponesBean{" +
				"status=" + status +
				", interface_status=" + interface_status +
				", info='" + info + '\'' +
				", json=" + json +
				", dataholder=" + dataholder +
				", equipmentholder=" + equipmentholder +
				'}';
	}
}
