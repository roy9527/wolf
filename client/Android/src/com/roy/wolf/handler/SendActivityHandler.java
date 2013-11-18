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
	private String cod = "";
	
	public SendActivityHandler(Context context) {
		super(context);
		this.action = "push";
	}

	public void setParams(String title, String msg, String cod) {
		this.title = title;
		this.msg = msg;
		this.cod = cod;
	}
	
	@Override
	public void initRequest() {
		paramsList.add(new RequestPair("msg", msg));
		paramsList.add(new RequestPair("act", "push"));
		paramsList.add(new RequestPair("title", title));
		try {
			JSONObject jo = new JSONObject();
			for (RequestPair pair : paramsList) {
				jo.put(pair.getName(), pair.getValue());
			}
			jo.put("cod", cod);
			paramsList.add(new RequestPair("cod", jo.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
