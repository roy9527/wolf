package com.roy.wolf.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapStatus;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.igexin.slavesdk.MessageManager;
import com.roy.wolf.R;
import com.roy.wolf.application.WolfApplication;
import com.roy.wolf.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	private MapView mMapView;
	private GeoPoint mCurrentPoint;
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
			Log.d("Wolf", "poi = " + sb.toString());
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
			Log.d("Wolf", "addr = " + sb.toString());

			LocationData ld = new LocationData();
			ld.accuracy = location.getRadius();
			ld.direction = location.getDerect();
			ld.latitude = location.getLatitude();
			ld.longitude = location.getLongitude();
			MyLocationOverlay lo = new MyLocationOverlay(mMapView);
			lo.setData(ld);
			mMapView.getOverlays().add(lo);
			mMapView.refresh();
			mCurrentPoint = new GeoPoint((int) (ld.latitude * 1E6),
					(int) (ld.longitude * 1E6));
			mMapView.getController().animateTo(mCurrentPoint);
			mMapView.getController().setZoom(18);

		}
	};

	private MKMapTouchListener mapTouchListener = new MKMapTouchListener() {

		@Override
		public void onMapLongClick(GeoPoint arg0) {

		}

		@Override
		public void onMapDoubleClick(GeoPoint arg0) {

		}

		@Override
		public void onMapClick(GeoPoint arg0) {

		}
	};

	private MKMapViewListener mapViewListener = new MKMapViewListener() {

		@Override
		public void onMapMoveFinish() {
			Log.d("Wolf", "map move finish.");
		}

		@Override
		public void onMapLoadFinish() {
			Log.d("Wolf", "map load finish.");
			switchTitleLoading(false);
		}

		@Override
		public void onMapAnimationFinish() {
			Log.d("Wolf", "map animation finish.");
		}

		@Override
		public void onGetCurrentMap(Bitmap arg0) {
			Log.d("Wolf", "get current map finish.");
		}

		@Override
		public void onClickMapPoi(MapPoi poi) {
			Log.d("Wolf", "poi click = " + poi.toString());
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);
		MessageManager.getInstance().initialize(this.getApplicationContext());
		init();
	}

	private void init() {
		setTitleInfo(R.string.title_main);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		mMapView.regMapViewListener(wapp.mBMapManager, mapViewListener);
		mMapView.regMapTouchListner(mapTouchListener);
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.setAK(WolfApplication.strKey);
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.setLocOption(getLocationOption(-1));
		initCenter();
		switchTitleLoading(true);
		mLocationClient.start();
		showFanPoint(1);
		initBottomTab();
	}

	private void initBottomTab() {
		LinearLayout l = (LinearLayout) findViewById(R.id.bottom_tab_layout);
		int s = l.getChildCount();
		for (int i = 0; i < s; i++) {
			l.getChildAt(i).setOnClickListener(this);
		}
	}

	private void initCenter() {
		MapController mMapController = mMapView.getController();
		MKMapStatus status = new MKMapStatus();
		status.targetGeo = getPoint(-1);
		status.zoom = mMapView.getMaxZoomLevel();
		mMapController.setMapStatusWithAnimation(status);
		mMapController.setCompassMargin(50, 200);
	}

	private GeoPoint getPoint(int index) {
		GeoPoint point = null;
		if (index == -1) {
			point = new GeoPoint((int) (39.965765 * 1E6),
					(int) (116.44716 * 1E6));
		}
		return point;
	}

	private LocationClientOption getLocationOption(int index) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setProdName("wolf");
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(3); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		return option;
	}

	private void showFanPoint(final OverlayItem item) {
		final PopupOverlay po1 = new PopupOverlay(mMapView, null);
		View v = LayoutInflater.from(this).inflate(
				R.layout.popup_show_operate_layout, null);
		TextView name = (TextView) v.findViewById(R.id.shop_name);
		TextView addr = (TextView) v.findViewById(R.id.shop_addr);
		
		name.setText(item.getTitle());
		addr.setText(item.getSnippet());
		
		v.findViewById(R.id.pop_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						po1.hidePop();
					}
				});
		
		v.findViewById(R.id.operate_0).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						po1.hidePop();
					}
				});
		v.findViewById(R.id.operate_1).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						po1.hidePop();
					}
				});
		v.findViewById(R.id.operate_2).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						po1.hidePop();
					}
				});
		po1.showPopup(v, item.getPoint(), 100);
	}

	class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		public MyItemizedOverlay(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
		}

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			Log.d("Wolf", arg0.toString());
			return super.onTap(arg0, arg1);
		}

		@Override
		protected boolean onTap(int arg0) {
			Log.d("Wolf", arg0 + "");
			OverlayItem oi = (OverlayItem) getItem(arg0);
			GeoPoint gp = oi.getPoint();
			mMapView.getController().animateTo(gp);
			mMapView.getController().setZoom(mMapView.getMaxZoomLevel());
			showFanPoint(oi);
			return true;
		}

	}

	// 116.452512,39.964858 大串
	// 116.446408,39.964561 麦当劳
	// 116.447284,39.966182 管式
	private void showFanPoint(int index) {
		Drawable d = getResources().getDrawable(R.drawable.boss);

		ArrayList<OverlayItem> list = new ArrayList<OverlayItem>();

		MyItemizedOverlay io = new MyItemizedOverlay(d, mMapView);
		GeoPoint point = new GeoPoint((int) (39.964675 * 1E6),
				(int) (116.448874 * 1E6));
		OverlayItem oi = new OverlayItem(point, "萨莉亚意式餐厅", "地址：朝阳区左家庄东街天虹百货2楼");

		GeoPoint point1 = new GeoPoint((int) (39.96387 * 1E6),
				(int) (116.451264 * 1E6));
		OverlayItem oi1 = new OverlayItem(point1, "老边饺子馆（三元桥店）", "地址：朝阳区左家庄东街静安里37号");
		GeoPoint point2 = new GeoPoint((int) (39.964858 * 1E6),
				(int) (116.452512 * 1E6));
		OverlayItem oi2 = new OverlayItem(point2, "蜀味居", "地址：朝阳区静安里1区5号临3号");

		GeoPoint point3 = new GeoPoint((int) (39.964561 * 1E6),
				(int) (116.446408 * 1E6));
		OverlayItem oi3 = new OverlayItem(point3, "麦当劳(左家庄餐厅)", "地址：朝阳区左家庄北里2号");

		GeoPoint point4 = new GeoPoint((int) (39.966182 * 1E6),
				(int) (116.447284 * 1E6));
		OverlayItem oi4 = new OverlayItem(point4, "管记翅吧(国展店)", "地址：朝阳区北三环东路8");

		list.add(oi);
		list.add(oi1);
		list.add(oi2);
		list.add(oi3);
		list.add(oi4);
		io.addItem(list);

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
	public void onBackPressed() {
		List<Overlay> list = mMapView.getOverlays();
		if (!list.isEmpty()) {
			for (Overlay overlay : list) {
				if (overlay instanceof PopupOverlay) {
					((PopupOverlay)overlay).hidePop();
					return;
				}
			}
		}
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tab_location:
			if (mCurrentPoint != null) {
				mMapView.getController().animateTo(mCurrentPoint);
				return;
			}
			if (!mLocationClient.isStarted()) {
				mLocationClient.start();
				return;
			}
			mLocationClient.requestLocation();
			break;
		case R.id.tab_satellite:
			String flag = (String) v.getTag();
			boolean show = "y".equals(flag);
			mMapView.setSatellite(!show);
			v.setTag(show ? "n" : "y");
			if (v instanceof TextView) {
				((TextView) v).setText(!show ? "关闭卫星视图" : "查看卫星视图");
			}
			break;
		case R.id.tab_traffic:
			String flag_ = (String) v.getTag();
			boolean show_ = "y".equals(flag_);
			mMapView.setTraffic(!show_);
			v.setTag(show_ ? "n" : "y");
			if (v instanceof TextView) {
				((TextView) v).setText(!show_ ? "关闭实况交通" : "查看实况交通");
			}
			break;
		case R.id.tab_nearly:
			break;
		}

	}

	@Override
	public void setTitleOperate(TITLE_OPERATE operate) {

	}
}
