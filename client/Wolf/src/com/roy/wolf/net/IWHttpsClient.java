package com.roy.wolf.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.roy.wolf.R;
import com.roy.wolf.util.NetworkUtil;
import com.roy.wolf.util.StringUtil;

public class IWHttpsClient extends DefaultHttpClient {
	public static final int CONNECTION_TIMEOUT = 5 * 1000;
	private Context context;

	public IWHttpsClient(Context ctx) {
		context = ctx;
		HttpParams params = this.getParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, CONNECTION_TIMEOUT);
		ConnManagerParams.setTimeout(params, CONNECTION_TIMEOUT);
		
		/*
		 * 移动		cmwap		10.0.0.172	80
		 * 联通2G	uniwap		10.0.0.172	80
		 * 联通3G	3gwap		10.0.0.172	80
		 * 电信		ctwap:cdma	10.0.0.200	80
		 */
		if (!NetworkUtil.hasMoreThanOneConnection(ctx)
				&& !NetworkUtil.isWiFiActive(context)) {
			ConnectivityManager manager = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			String extraInfo = null;
			if (info != null) {
				extraInfo = StringUtil.strip(info.getExtraInfo());
			}
			if (!StringUtil.isEmptyOrWhitespace(extraInfo)) {
				extraInfo = extraInfo.toLowerCase();
				if (extraInfo.equals("cmwap") || extraInfo.equals("uniwap")
						|| extraInfo.equals("3gwap")) {
					params.setParameter(ConnRoutePNames.DEFAULT_PROXY,
							new HttpHost("10.0.0.172", 80, "http"));
				} else if (extraInfo.equals("ctwap:cdma")) {
					params.setParameter(ConnRoutePNames.DEFAULT_PROXY,
							new HttpHost("10.0.0.200", 80, "http"));
				}
			}
		}
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", newSslSocketFactory(), 8682));
		return new SingleClientConnManager(getParams(), registry);
	}

	private SSLSocketFactory newSslSocketFactory() {
		KeyStore trusted = null;
		try {
			trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources().openRawResource(R.raw.client1);
			trusted.load(in, "wolf9527".toCharArray());
			in.close();
			return new SSLSocketFactory(trusted);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (CertificateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
}
