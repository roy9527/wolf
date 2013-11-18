package com.roy.wolf.push;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.Consts;
import com.roy.wolf.activity.MainActivity;

public class TransmissionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		Bundle bundle = intent.getExtras();
		switch (bundle.getInt(Consts.CMD_ACTION)) {
		case Consts.GET_MSG_DATA:
			Log.d("Wolf1", "Got Payload");
			// 获取透传(payload)数据
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				String data = new String(payload);
				Log.d("Wolf1", "Got Payload:" + data);
				// TODO:接收处理透传(payload)数据
				try {
					JSONObject jo = new JSONObject(data);
					Intent i = new Intent(arg0, MainActivity.class);
					i.putExtra("Title", jo.optString("title"));
					i.putExtra("Content", jo.optString("msg"));
					String cod = jo.optString("cod");
					if (!TextUtils.isEmpty(cod)) {
						String[] t = cod.split("/");
						int l = Integer.valueOf(t[0]);
						int tt = Integer.valueOf(t[1]);
						i.putExtra("Longitude", l);
						i.putExtra("Latitude", tt);
					}
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					arg0.startActivity(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case Consts.GET_CLIENTID:
			// 获取 ClientID(CID)
			String cid = bundle.getString("clientid");
			Log.d("Wolf1", "Got ClientID:" + cid);
			// TODO:
			/*
			 * 第三方应用需要将 ClientID 上传到第三方服务器,并且将当前用户帐号和 ClientID 进行关联,以便以后通过用户帐号查找
			 * ClientID 进行消息推送 有些情况下 ClientID 可能会发生变化,为保证获取最新的 ClientID,请应用程序
			 * 在每次获取 ClientID 广播后,都能进行一次关联绑定
			 */
			break;
		case Consts.BIND_CELL_STATUS:
			String cell = bundle.getString("cell");
			Log.d("Wolf1", "BIND_CELL_STATUS:" + cell);
			break;
		default:
			break;
		}
	}
}
