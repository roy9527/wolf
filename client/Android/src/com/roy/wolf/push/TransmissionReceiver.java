package com.roy.wolf.push;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.Consts;
import com.roy.wolf.R;
import com.roy.wolf.activity.MainActivity;
import com.roy.wolf.application.WolfApplication;
import com.roy.wolf.handler.InitClientHandler;
import com.roy.wolf.model.EventEntry;
import com.roy.wolf.model.PointEntry;
import com.roy.wolf.net.RequestListener;

public class TransmissionReceiver extends BroadcastReceiver {

	private final static String EVENT_PUSH_ACTION = "com.roy.wolf.event_push_action";

	@Override
	public void onReceive(Context arg0, Intent intent) {
		WolfApplication app = (WolfApplication) arg0.getApplicationContext();
		Bundle bundle = intent.getExtras();
		switch (bundle.getInt(Consts.CMD_ACTION)) {
		case Consts.GET_MSG_DATA:
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				String data = new String(payload);
				Log.d("Wolf1", "Got Payload:" + data);
				try {
					JSONObject jo = new JSONObject(data);
					EventEntry entry = new EventEntry();
					Intent i = new Intent();

					entry.title = jo.optString("title");
					entry.content = jo.optString("msg");
					// TODO update
					// entry.date = jo.optString("date");
					entry.date = System.currentTimeMillis();
					String cod = jo.optString("cod");
					if (!TextUtils.isEmpty(cod)) {
						String[] t = cod.split("/");
						int l = Integer.valueOf(t[0]);
						int tt = Integer.valueOf(t[1]);
						PointEntry point = new PointEntry();
						point.latitude = tt;
						point.longtitude = l;
						entry.point = point;
					}
					i.putExtra("Event", entry);
					
					if (app.mainLive == 1) {
						i.setAction(EVENT_PUSH_ACTION);
						arg0.sendBroadcast(i);
					}else {
						sendNotify(arg0, entry);
//						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						i.setClass(arg0, MainActivity.class);
//						arg0.startActivity(i);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case Consts.GET_CLIENTID:
			// 获取 ClientID(CID)
			String cid = bundle.getString("clientid");
			Log.d("Wolf1", "Got ClientID:" + cid);
			InitClientHandler ih = new InitClientHandler(arg0);
			ih.setParams(app.imei, cid, app.phone);
			ih.onRequest(new RequestListener() {
				
				@Override
				public void onError(Object error) {
					
				}
				
				@Override
				public void onCallBack(Object data) {
					
				}
			}, false);
			break;
		case Consts.BIND_CELL_STATUS:
			String cell = bundle.getString("cell");
			Log.d("Wolf1", "BIND_CELL_STATUS:" + cell);
			break;
		default:
			break;
		}
	}
	
	private void sendNotify(Context context, EventEntry entry) {
		long time = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.wolf_alarms, entry.content, time);
		notification.tickerText = entry.content;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		int id = (int) (time % 10000);
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("Event", entry);
		PendingIntent contentIntent = PendingIntent.getActivity(context, id,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.setLatestEventInfo(context, entry.title, entry.content, contentIntent);
		notificationManager.notify(id, notification);
	}
}
