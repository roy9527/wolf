package com.roy.wolf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.igexin.slavesdk.MessageManager;
import com.roy.wolf.application.WolfApplication;
import com.roy.wolf.guanzi.GuanZiActivity;

public class MainActivity extends Activity implements View.OnClickListener {

	private MapView mMapView;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new BDLocationListener() {

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			// Log.i("wolf", "poi = " + sb.toString());
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			// Log.i("wolf", "addr = " + sb.toString());

			LocationData ld = new LocationData();
			ld.accuracy = location.getRadius();
			ld.direction = location.getDerect();
			ld.latitude = location.getLatitude();
			ld.longitude = location.getLongitude();
			LocationOverlay lo = new LocationOverlay(mMapView);
			lo.setData(ld);
			mMapView.getOverlays().add(lo);
			mMapView.refresh();
			GeoPoint point = new GeoPoint((int) (ld.latitude * 1E6),
					(int) (ld.longitude * 1E6));
			mMapView.getController().setCenter(point);
			mMapView.getController().setZoom(18);

		}
	};

	// 继承MyLocationOverlay重写dispatchTap实现点击处理
	public class LocationOverlay extends MyLocationOverlay {

		public LocationOverlay(MapView mapView) {
			super(mapView);
			// TODO Auto-generated constructor stub
		}
		// @Override
		// protected boolean dispatchTap() {
		// // TODO Auto-generated method stub
		// //处理点击事件,弹出泡泡
		// popupText.setBackgroundResource(R.drawable.popup);
		// popupText.setText("我的位置");
		// pop.showPopup(BMapUtil.getBitmapFromView(popupText),
		// new GeoPoint((int)(locData.latitude*1e6),
		// (int)(locData.longitude*1e6)),
		// 8);
		// return true;
		// }

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MessageManager.getInstance().initialize(this.getApplicationContext());
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.setAK(WolfApplication.strKey);
		mLocationClient.registerLocationListener(myListener);

		MapController mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point = new GeoPoint((int) (39.965765 * 1E6),
				(int) (116.44716 * 1E6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(14);// 设置地图zoom级别
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setProdName("wolf");
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(3); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		findViewById(R.id.fetch_location).setOnClickListener(this);
		showFanPoint(1);
		
	}

	private void showFanPoint() {
		PopupOverlay po1 = new PopupOverlay(mMapView, new PopupClickListener() {

			@Override
			public void onClickedPopup(int arg0) {
				Intent i = new Intent(MainActivity.this, GuanZiActivity.class);
				startActivity(i);
			}
		});
		ImageView tv = new ImageView(this);
		tv.setImageResource(R.drawable.sailiya);
		GeoPoint point = new GeoPoint((int) (39.964675 * 1E6),
				(int) (116.448874 * 1E6));
		po1.showPopup(tv, point, 10);
	}

	private void showFanPoint(int index) {
		Drawable d = getResources().getDrawable(R.drawable.boss);
		ItemizedOverlay io = new ItemizedOverlay<OverlayItem>(null, mMapView);
		GeoPoint point = new GeoPoint((int) (39.964675 * 1E6),
				(int) (116.448874 * 1E6));
		OverlayItem oi = new OverlayItem(point, "A", "AA");
		oi.setMarker(d);
		oi.setAnchor(OverlayItem.ALIGN_BOTTON);
		oi.setSnippet("BB");
		oi.setTitle("CC");
		io.addItem(oi);
		mMapView.getOverlays().add(io);
		mMapView.refresh();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		mLocationClient.stop();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.fetch_location:
			// if (!mLocationClient.isStarted()) {
			// mLocationClient.start();
			// return;
			// }
			// mLocationClient.requestLocation();
			/*
			 * if (mLocationClient != null && mLocationClient.isStarted())
			 * mLocationClient.requestPoi();
			 */
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// IWHttpsClient ic = new IWHttpsClient(MainActivity.this);
			// test(ic);
			// }
			// }).start();
			// break;
		}
	}

	private void test(HttpClient client) {
		String url = "https://192.168.85.11:8682";
		InputStream is = null;
		HttpResponse httpResponse = null;
		ByteArrayOutputStream baos = null;
		HttpUriRequest request = null;
		try {
			request = new HttpPost(url);
			// if (runnable.mParams != null && !runnable.mParams.isEmpty()) {
			// post.setEntity(new UrlEncodedFormEntity(runnable.mParams,
			// HTTP.UTF_8));
			// } else if (runnable.mEntity != null) {
			// post.setEntity(runnable.mEntity);
			// }
			request.addHeader("Accept-Encoding", "gzip");

			httpResponse = client.execute(request);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				Header encodeHeader;
				String encode = "";
				if ((encodeHeader = httpResponse
						.getFirstHeader("Content-Encoding")) != null) {
					encode = encodeHeader.getValue();
				}
				if (encode.equals("gzip")) {
					is = new GZIPInputStream(httpResponse.getEntity()
							.getContent());
				} else {
					is = httpResponse.getEntity().getContent();
				}
				if (is == null) {
					return;
				}
				baos = new ByteArrayOutputStream();
				byte[] data = toByteArray(is, baos);
				if (data == null || data.length < 1) {
					return;
				}
				String d = new String(data);
				System.out.println(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
				if (request != null) {
					request.abort();
				}
				if (client != null) {
					client.getConnectionManager().shutdown();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] toByteArray(InputStream is, ByteArrayOutputStream baos)
			throws IOException {
		if (is == null) {
			return null;
		}
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = is.read(buffer))) {
			baos.write(buffer, 0, n);
		}
		return baos.toByteArray();
	}
}
