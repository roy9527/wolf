package com.roy.wolf.handler;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.roy.wolf.net.RequestCallBack;
import com.roy.wolf.net.RequestListener;
import com.roy.wolf.net.RoyHttpManager;
import com.roy.wolf.net.RoyHttpManager.Request;

public abstract class BaseHandler {
	private static final String DOMAIN = "192.168.85.11:8682";
	private static final String HTTP = "https://";
	public String action = "";
	public boolean isGet = false;
	public Context context;
	public Map<String, String> params = new HashMap<String, String>();
	public HttpEntity entity = null;
	public int versionCode = 0;
	private Request request = new Request();
	private RoyHttpManager manager = null;
	private JSONObject response;

	public ArrayList<RequestPair> paramsList = new ArrayList<RequestPair>();

	abstract protected void initRequest();

	abstract protected void initEntity();

	abstract protected void onParser(JSONObject response);

	abstract public com.roy.wolf.model.Entry getData();

	public BaseHandler(Context context) {
		this.context = context;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		manager = RoyHttpManager.getInstance(context);
	}

	public String mapToString(Map<String, String> params) throws Exception {
		if (params == null || params.isEmpty())
			return null;
		Set<Entry<String, String>> enties = params.entrySet();
		StringBuilder b = new StringBuilder();
		boolean isFirst = true;
		for (Entry<String, String> entry : enties) {
			if (!isFirst) {
				b.append("&");
			}
			if (TextUtils.isEmpty(entry.getKey())) {
				b.append(entry.getValue());
				continue;
			}
			b.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			b.append("=");
			b.append(URLEncoder.encode(
					entry.getValue() == null ? "" : entry.getValue(), "UTF-8"));
			isFirst = false;
		}
		return b.toString();
	}

	public String initUrl() {
		String url = HTTP + DOMAIN + "/index.php?";
		initRequest();
//		try {
//			url = url + mapToString(params);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return url;
	}

	public boolean checkResponse() {
		if (response == null) {
			return false;
		}
		String errCode = response.optString("error_code", "-1");
		return errCode.equals("0");
	}

	public void onRequest(final RequestListener callback, boolean isCache) {
		request.domain = DOMAIN;
		request.isGet = isGet;
		request.url = initUrl();
		initEntity();
		if (null != entity) {
			request.entity = entity;
		}
		request.callBack = new RequestCallBack() {

			@Override
			public void onFetch(boolean success, Object data) {
				if (success) {
					if (data != null) {
						try {
							String d = (String) data;
							response = new JSONObject(d);
							onParser(response);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					callback.onCallBack(data);
				} else {
					callback.onError(data);
				}
			}
		};
		manager.onRequest(request);
	}
}
