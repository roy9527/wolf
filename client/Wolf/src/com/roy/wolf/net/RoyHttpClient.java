package com.roy.wolf.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Locale;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.roy.wolf.R;
import com.roy.wolf.util.NetworkUtil;
import com.roy.wolf.util.StringUtil;

public class RoyHttpClient extends DefaultHttpClient {
	public static final int CONNECTION_TIMEOUT = 45 * 1000;

	public Context context;

	public RoyHttpClient(Context context) {
		this.context = context;
		HttpParams params = this.getParams();
		initProxy(params);
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, CONNECTION_TIMEOUT);
		ConnManagerParams.setTimeout(params, CONNECTION_TIMEOUT);
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		HttpParams params = this.getParams();
		ConnManagerParams.setMaxTotalConnections(params, 10);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", newSslSocketFactory(), 8682));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		return new ThreadSafeClientConnManager(params, schemeRegistry);
	}

	private SSLSocketFactory newSslSocketFactory() {
		KeyStore trusted = null;
		try {
			trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources().openRawResource(
					R.raw.client1);
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

	private void initProxy(HttpParams params) {
		if (!NetworkUtil.hasMoreThanOneConnection(context)
				&& !NetworkUtil.isWiFiActive(context)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
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
}
