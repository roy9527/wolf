package com.roy.wolf.util;

import android.content.Context;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 
 * 包含设备处理方法的工具类
 * 
 */
public class DeviceUtil {
	
	public static final int SDK_VERSION_1_5 = 3;
	public static final int SDK_VERSION_1_6 = 4;
	public static final int SDK_VERSION_2_0 = 5;
	public static final int SDK_VERSION_2_0_1 = 6;
	public static final int SDK_VERSION_2_1 = 7;
	public static final int SDK_VERSION_2_2 = 8;
	public static final int SDK_VERSION_2_3 = 9;
	public static final int SDK_VERSION_2_3_3 = 10;
	public static final int SDK_VERSION_3_0 = 11;
	
	public static final float DENSITY_LOW = 0.75f;
	public static final float DENSITY_MEDIUM = 1.0f;
	public static final float DENSITY_HIGH = 1.5f;
	public static final float DENSITY_EXTRA_HIGH = 2.0f;
	
	public static final int DPI_LOW = 120;
	public static final int DPI_MEDIUM = 160;
	public static final int DPI_HIGH = 240;
	public static final int DPI_EXTRA_HIGH = 320;
	
	public static final int SCREEN_HEIGHT_SMALL = 320;       //related default screens are at least 240*320(px)
	public static final int SCREEN_HEIGHT_NORMAL = 480;    //related default screens are at least 320*480(px)
	public static final int SCREEN_HEIGHT_LARGE = 800;     //related default screens are at least 480*800(px)
	public static final int SCREEN_HEIGHT_XLARGE = 1024;     //related default screens are at least 1024*600(px)
	public static final int SCREEN_HEIGHT_XXLARGE = 1280;    //related default screens are at least 1280*768(px)
	
	/**
	 * 获得设备型号
	 * @return
	 */
	public static String getDeviceModel() {
        return StringUtil.makeSafe(Build.MODEL);
    }
	
	/**
	 * 获得设备的固件版本号
	 */
	public static String getReleaseVersion() {
		return StringUtil.makeSafe(Build.VERSION.RELEASE);
	}
	
	/**
	 * 获得国际移动设备身份码即DeviceId
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
        return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
	
	/**
	 * 获得MSISDN
	 * @param context
	 * @return
	 */
	public static String getMSISDN(Context context) {
		if(context == null)
			return null;
		String msisdn = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		if(msisdn == null)
			return "No phone number";
        return (msisdn.equals("") ? "No phone number" : msisdn);
    }
	
	/**
	 * 获得国际移动用户识别码
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context) {
        return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }
	
	/**
	 * 获得设备屏幕矩形区域范围
	 * @param context
	 * @return
	 */
	public static Rect getScreenRect(Context context) {
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        return new Rect(0, 0, w, h);
    }
	
	/**
	 * 获得设备屏幕密度
	 */
	public static float getScreenDensity(Context context) {
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		return metrics.density;
	}
	
	public static int getScreenDensityDpi(Context context) {
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		return (int)(metrics.density * 160);
	}
	
	/** 
	 * 获得系统版本
	 */
	public static String getSDKVersion(){
		return android.os.Build.VERSION.SDK;
	}
	
	public static int getSDKVersionInt(){
		try {
			return Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (Exception e) {
			return 0;
		}
		//return android.os.Build.VERSION.SDK_INT;
	}
	
	/**
	 * 获得android_id
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}
	
	/**
	 * 获得屏幕尺寸
	 * @param context
	 * @return
	 */
	public static String getResolution(Context context) {
		Rect rect = getScreenRect(context);
		return rect.right + "x" + rect.bottom;
	}
	
	public static String getSerialNumber() {
		String serialNumber = "";
		
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class);
			serialNumber = (String) get.invoke(c, "ro.serialno");
			
			if(serialNumber.equals("")) {
				serialNumber = "?";
			}
		} catch (Exception e) {
			if(serialNumber.equals("")) {
				serialNumber = "?";
			}
		}
		
		return serialNumber;
	}
	
	/**
     * 获取系统cpu的abi(application binary interface)信息
     * @return
     */
    public static String getCpuAbi() {
        try {
            return StringUtil.makeSafe(Build.CPU_ABI); 
        } catch(Error e) {
            return "";
        }
    }
    
    /**
     * 获取wifi mac地址
     * @return wifi mac地址(xx:xx:xx:xx:xx)
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        
        return info.getMacAddress();
    }
	
}
