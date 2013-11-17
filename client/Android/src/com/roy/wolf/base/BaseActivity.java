package com.roy.wolf.base;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.wolf.R;
import com.roy.wolf.application.WolfApplication;

public abstract class BaseActivity extends FragmentActivity {

	/**
	 * left=左侧按钮<br/>
	 * right=右侧按钮
	 * 
	 */
	public static enum TITLE_OPERATE {
		LEFT, RIGHT
	};

	public FragmentManager fragmentManager;
	public FragmentTransaction fragmentTransaction;

	public View.OnClickListener clickListener = null;
	public WolfApplication wapp = null;

	public DrawerLayout drawerLayout = null;
 
	public Context context;
	
	public boolean isInit = false;
	
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
		}

	};

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		fragmentManager = getSupportFragmentManager();
		wapp = (WolfApplication) getApplication();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isInit) {
			initDrawerLayout();
			isInit = true;
		}
	}

	public void initDrawerLayout() {
		drawerLayout = (DrawerLayout) findViewById(R.id.normal_drawer_layout);
		if (drawerLayout != null) {
			initLeftMenu(drawerLayout);
		}
	}
	
	private void initLeftMenu(View v) {
		ListView lv = (ListView) v.findViewById(R.id.left_drawer_list);
		final String[] menu = getResources().getStringArray(R.array.base_left_menu_array);
		MenuAdapter ma = new MenuAdapter(menu);
		lv.setAdapter(ma);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(context, menu[arg2], Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void closeTitleBack() {
		View v = findViewById(R.id.normal_title_back);
		if (v != null) {
			v.setVisibility(View.INVISIBLE);
		}
	}

	public void setTitleBack(View v) {
		final View back = v.findViewById(R.id.normal_title_back);
		if (back == null) {
			return;
		}
		back.post(new Runnable() {

			@Override
			public void run() {
				Rect r = new Rect();
				back.getHitRect(r);
				r.left -= 50;
				r.right += 50;
				r.top -= 50;
				r.bottom += 50;
				((View) back.getParent()).setTouchDelegate(new TouchDelegate(r,
						back));
			}
		});

		if (clickListener != null) {
			back.setOnClickListener(clickListener);
		} else {
			back.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}

			});
		}

	}

	/**
	 * 初始化title name
	 * 
	 * @param titleName
	 */
	public TextView setTitleInfo(String titleName) {
		View v = findViewById(R.id.normal_title_layout);
		if (v != null) {
			setTitleBack(v);
			TextView tv = (TextView) v.findViewById(R.id.normal_title_name);
			if (tv != null) {
				tv.setText(titleName);
				return tv;
			}
		}

		return null;
	}

	/**
	 * 初始化title name
	 * 
	 * @param id
	 */
	public TextView setTitleInfo(int titleId) {
		String title = getString(titleId);
		return setTitleInfo(title);
	}

	/**
	 * 初始化title 右数第二个按钮
	 * 
	 * @param v
	 * @param operateId
	 *            按钮显示文字id，没有为-1
	 * @param operateBG
	 *            按钮显示背景，没有为-1
	 */
	public TextView setTitleOperateLeft(int operateId, int operateBG) {
		return initTitleOperate(operateId, operateBG, true);
	}

	/**
	 * 初始化title 右数第一个按钮
	 * 
	 * @param v
	 * @param operateId
	 *            按钮显示文字id，没有为-1
	 * @param operateBG
	 *            按钮显示背景，没有为-1
	 */
	public TextView setTitleOperateRight(int operateId, int operateBG) {
		return initTitleOperate(operateId, operateBG, false);
	}

	/**
	 * 页面需重写此方法，用以处理title栏右侧按钮
	 * 
	 * @param operate
	 */
	public abstract void setTitleOperate(TITLE_OPERATE operate);

	/**
	 * 开关Title栏右侧loading
	 * 
	 * @param true = open，false = close
	 */
	public void switchTitleLoading(final boolean open) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				View v = findViewById(R.id.normal_title_operate_loading);
				View l = findViewById(R.id.normal_title_operate_layout);
				if (v != null) {
					v.setVisibility(open ? View.VISIBLE : View.GONE);
				}
				if (l != null) {
					l.setVisibility(!open ? View.VISIBLE : View.GONE);
				}
			}
		});

	}

	private TextView initTitleOperate(int operateId, int operateBG,
			final boolean left) {
		View v = findViewById(R.id.normal_title_layout);
		if (v == null) {
			return null;
		}
		TextView tv = (TextView) v
				.findViewById(left ? R.id.normal_title_operate_left
						: R.id.normal_title_operate_right);
		if (tv == null) {
			return null;
		}
		if (operateId > 0) {
			tv.setText(operateId);
		}
		if (operateBG > 0) {
			tv.setBackgroundResource(operateBG);
		}
		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setTitleOperate(left ? TITLE_OPERATE.LEFT : TITLE_OPERATE.RIGHT);
			}

		});
		return tv;
	}

	@Override
	public void onBackPressed() {
		if (fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStackImmediate();
			return;
		}
		super.onBackPressed();
	}
	
	class MenuAdapter extends BaseAdapter {

		String[] menuList = null;
		
		public MenuAdapter(String[] menuList) {
			this.menuList = menuList;
		}
		
		@Override
		public int getCount() {
			return menuList.length;
		}

		@Override
		public Object getItem(int arg0) {
			return menuList[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int p, View cv, ViewGroup vg) {
			TextView item = new TextView(context);
			item.setText(menuList[p]);
			item.setTextColor(getResources().getColor(R.color.fuxk_base_color_black));
			item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			return item;
		}
		
	}
}
