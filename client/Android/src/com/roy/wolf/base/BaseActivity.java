package com.roy.wolf.base;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
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
	
	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
		}

	};

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		fragmentManager = getSupportFragmentManager();
		wapp = (WolfApplication) getApplication();
	}

	private void initTitleLayout() {
		
	}
	
	public void setTitleBack(View v) {
		final View back = v.findViewById(R.id.normal_title_back);
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
		setTitleBack(findViewById(R.id.normal_title_layout));
		TextView tv = (TextView) findViewById(R.id.normal_title_layout)
				.findViewById(R.id.normal_title_name);
		tv.setText(titleName);
		return tv;
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
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				findViewById(R.id.normal_title_operate_loading).setVisibility(
						open ? View.VISIBLE : View.GONE);
				findViewById(R.id.normal_title_operate_layout).setVisibility(
						!open ? View.VISIBLE : View.GONE);
			}
		});

	}

	private TextView initTitleOperate(int operateId, int operateBG,
			final boolean left) {
		TextView tv = (TextView) findViewById(R.id.normal_title_layout)
				.findViewById(
						left ? R.id.normal_title_operate_left
								: R.id.normal_title_operate_right);
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
}
