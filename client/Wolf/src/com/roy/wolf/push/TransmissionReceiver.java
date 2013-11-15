package com.roy.wolf.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.Consts;

public class TransmissionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GexinSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(Consts.CMD_ACTION)) {
		case Consts.GET_MSG_DATA:
			// 获取透传(payload)数据
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				String data = new String(payload);
				Log.d("GexinSdkDemo", "Got Payload:" + data);
				// TODO:接收处理透传(payload)数据
			}
			break;
		case Consts.GET_CLIENTID:
			// 获取 ClientID(CID)
			String cid = bundle.getString("clientid");
			Log.d("GexinSdkDemo", "Got ClientID:" + cid);
			// TODO:
			/*
			 * 第三方应用需要将 ClientID 上传到第三方服务器,并且将当前用户帐号和 ClientID 进行关联,以便以后通过用户帐号查找
			 * ClientID 进行消息推送 有些情况下 ClientID 可能会发生变化,为保证获取最新的 ClientID,请应用程序
			 * 在每次获取 ClientID 广播后,都能进行一次关联绑定
			 */
			break;
		case Consts.BIND_CELL_STATUS:
			String cell = bundle.getString("cell");
			Log.d("GexinSdkDemo", "BIND_CELL_STATUS:" + cell);
			break;
		default:
			break;
		}
	}
}
