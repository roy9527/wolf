package com.roy.wolf.handler;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.roy.wolf.model.Entry;

public class SendActivityHandler extends BaseHandler {

	private String msg = "";
	private String title = "";

	public SendActivityHandler(Context context) {
		super(context);
		this.action = "push";
	}

	public void setParams(String title, String msg) {
		this.title = title;
		this.msg = msg;
	}
	
	@Override
	public void initRequest() {
		paramsList.add(new RequestPair("msg", msg));
		paramsList.add(new RequestPair("act", "push"));
		paramsList.add(new RequestPair("title", title));
	}

	@Override
	public void initEntity() {
		try {
			if (!paramsList.isEmpty()) {
				this.entity = new UrlEncodedFormEntity(paramsList, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onParser(JSONObject response) {
		Log.d("Wolf", response.toString());
	}

	@Override
	public Entry getData() {
		return null;
	}

}
