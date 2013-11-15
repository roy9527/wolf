package com.roy.wolf.handler;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.roy.wolf.model.Entry;

public class GetWebInfoHandler extends BaseHandler {
	
	private String imei = "";
	private String udid = "";
	private String data = "";
	
	public GetWebInfoHandler(Context context) {
		super(context);
		this.action = "getinfo";
	}
	
	@Override
	protected void initRequest() {
		params.put("udid", udid);
		params.put("imei", imei);
		params.put("appid", "flashlight");
		params.put("model", Build.MODEL);
		params.put("version", String.valueOf(versionCode));
	}

	@Override
	protected void initEntity() {
		if (!TextUtils.isEmpty(data)) {
			try {
				this.entity = new StringEntity(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
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
