package com.roy.wolf.guanzi;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GuanZiActivity extends Activity {

	WebView wv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wv = new WebView(this);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
		});
		wv.loadUrl("http://www.saizeriya.com.cn");
		setContentView(wv);
	}

	@Override
	public void onBackPressed() {
		if (wv.canGoBack()) {
			wv.goBack();
			return;
		}
		super.onBackPressed();
	}

	
}
