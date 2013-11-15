package com.roy.wolf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.http.HttpHost;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ProxyManager {
	private static final String TAG = ProxyManager.class.getSimpleName();
	
	private static ProxyManager mProxyManager;
	private static Context mContext;
	
	public static synchronized ProxyManager getnstance(Context context) {
		if(mProxyManager == null) {
			mContext = context;
			mProxyManager = new ProxyManager();
		}
		
		return mProxyManager;
	}
	
	//set proxy for not WIFI network
	public void setProxy() {
		HttpHost proxyServer = getProxyServer();
		if(proxyServer == null) {
			return;
		}
		
		boolean isSuccess = setProxyHostField(proxyServer);
	}
	
	private HttpHost getProxyServer() {
		HttpHost proxyServer = null;
		
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		String extraInfo = null;
		if (info != null) {
			extraInfo = StringUtil.strip(info.getExtraInfo());
		}
		if (!StringUtil.isEmptyOrWhitespace(extraInfo)) {
			extraInfo = extraInfo.toLowerCase();
			
			if (extraInfo.equals("cmwap") || extraInfo.equals("uniwap") || extraInfo.equals("3gwap")) {
				proxyServer = new HttpHost("10.0.0.172", 80, "http");
			} else if (extraInfo.equals("ctwap:cdma")) {
				proxyServer = new HttpHost("10.0.0.200", 80, "http");
			}
		}
		
		return proxyServer;
	}
	
	private boolean setProxyHostField(HttpHost proxyServer) {
	    // Getting network      
	    Class<?> networkClass = null;
	    Object network = null;
	    try {
	        networkClass = Class.forName("android.webkit.Network");
	        if(networkClass != null) {
	        	network = invokeMethod(networkClass, "getInstance", new Object[]{mContext}, Context.class);
	        }
//	        Field networkField = networkClass.getDeclaredField("sNetwork");
//	        network = getFieldValueSafely(networkField, null);
	    } catch (Exception ex) {
	        Log.e(TAG, "error getting network");
	        return false;
	    }
	    if (network == null) {
	        Log.e(TAG, "error getting network : null");
	        return false;
	    }
	    Object requestQueue = null;
	    try {
	        Field requestQueueField = networkClass.getDeclaredField("mRequestQueue");
	        requestQueue = getFieldValueSafely(requestQueueField, network);
	    } catch (Exception ex) {
	        Log.e(TAG, "error getting field value");
	        return false;
	    }
	    if (requestQueue == null) {
	        Log.e(TAG, "Request queue is null");
	        return false;
	    }
	    Field proxyHostField = null;
	    try {
	        Class<?> requestQueueClass = Class.forName("android.net.http.RequestQueue");
	        proxyHostField = requestQueueClass
	                .getDeclaredField("mProxyHost");
	    } catch (Exception ex) {
	        Log.e(TAG, "error getting proxy host field");
	        return false;
	    }       
	    synchronized (mProxyManager) {
	        boolean temp = proxyHostField.isAccessible();
	        try {
	            proxyHostField.setAccessible(true);
	            proxyHostField.set(requestQueue, proxyServer);
	        } catch (Exception ex) {
	            Log.e(TAG, "error setting proxy host");
	        } finally {
	            proxyHostField.setAccessible(temp);
	        }
	    }
	    return true;
	}

	private Object getFieldValueSafely(Field field, Object classInstance) {
	    boolean oldAccessibleValue = field.isAccessible();
	    field.setAccessible(true);
	    Object result = null;
		try {
			result = field.get(classInstance);
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	    field.setAccessible(oldAccessibleValue);
	    return result;      
	}
	
	private Object invokeMethod(Object object, String methodName, Object[] params, Class<?>... types) throws Exception {
        Object out = null;
        Class<?> c = object instanceof Class ? (Class<?>) object : object.getClass();
        if (types != null) {
            Method method = c.getMethod(methodName, types);
            out = method.invoke(object, params);
        } else {
            Method method = c.getMethod(methodName);
            out = method.invoke(object);
        }
        return out;
    }
}
