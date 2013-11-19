package com.roy.wolf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.roy.wolf.R;
import com.roy.wolf.base.BaseActivity;

public class SplashActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash_layout);
		Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
		View v = findViewById(R.id.splash_img);
		a.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}
		});
		v.startAnimation(a);
	}

	@Override
	public void setTitleOperate(TITLE_OPERATE operate) {
		
	}

}
