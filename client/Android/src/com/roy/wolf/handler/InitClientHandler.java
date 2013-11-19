package com.roy.wolf.handler;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONObject;

import android.content.Context;

import com.roy.wolf.model.Entry;

public class InitClientHandler extends BaseHandler {

	private String imei = "";
	private String cid = "";
	private String phone = "";

	public InitClientHandler(Context context) {
		super(context);
	}

	public void setParams(String imei, String cid, String phone) {
		this.imei = imei;
		this.cid = cid;
		this.phone = phone;
	}

	@Override
	protected void initRequest() {
		paramsList.add(new RequestPair("imei", imei));
		paramsList.add(new RequestPair("act", "init"));
		paramsList.add(new RequestPair("phone", imei));
		paramsList.add(new RequestPair("cid", cid));
	}

	@Override
	protected void initEntity() {

		try {
			if (!paramsList.isEmpty()) {
				this.entity = new UrlEncodedFormEntity(paramsList, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onParser(JSONObject response) {

	}

	@Override
	public Entry getData() {
		return null;
	}

}
