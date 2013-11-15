package com.roy.wolf.application;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class WolfApplication extends Application {

	public static final String strKey = "C12fe738d81c558952faa3896af365e3";

	public boolean m_bKeyRight = true;
	BMapManager mBMapManager = null;

	@Override
	public void onCreate() {
		super.onCreate();
		initEngineManager(this);
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(getApplicationContext(), "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(getApplicationContext(), "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(getApplicationContext(),
						"请在 WolfApplication.java文件输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
