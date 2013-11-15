package com.roy.wolf.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class PreferenceUtil {
	
	public static String getSharedString(Context context, String key, String defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, defValue);
	}
	
	public static void putSharedString(Context context, String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString(key, value).commit();
	}
	
	public static int getSharedInt(Context context, String key, int defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(key, defValue);
	}
	
	public static void putSharedInt(Context context, String key, int value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt(key, value).commit();
	}
	
	public static long getSharedLong(Context context, String key, long defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getLong(key, defValue);
	}
	
	public static void putSharedLong(Context context, String key, long value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putLong(key, value).commit();
	}
	
	public static boolean getSharedBoolean(Context context, String key, boolean defValue){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(key, defValue);
	}
	
	public static void putSharedBoolean(Context context, String key, boolean value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean(key, value).commit();
	}
	
	public static void removeShare(Context context, String key){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.remove(key).commit();
	}
	
	public static boolean getBoolean(Activity ac, String key, boolean defValue) {
		return ac.getPreferences(Context.MODE_PRIVATE)
				.getBoolean(key, defValue);
	}
	
	public static void putBoolean(Activity ac, String key, boolean value) {
		ac.getPreferences(Context.MODE_PRIVATE)
			.edit()
			.putBoolean(key, value)
			.commit();
	}
	
	public static int getInt(Activity ac, String key, int defValue) {
		return ac.getPreferences(Context.MODE_PRIVATE)
				.getInt(key, defValue);
	}
	
	public static void putInt(Activity ac, String key, int value) {
		ac.getPreferences(Context.MODE_PRIVATE)
			.edit()
			.putInt(key, value)
			.commit();
	}
	
	public static boolean updateShareInt(Intent data, Activity ac, String key,
			int defValue) {
		if (data != null) {
			int t = data.getIntExtra(key, defValue);
			if (t != getSharedInt(ac, key, defValue)) {
				putSharedInt(ac, key, t);
				return true;
			}
		}
		return false;
	}
	
	public static boolean updateShareBoolean(Intent data, Activity ac, String key) {
		return updateShareBoolean(data, ac, key, true);
	}
	
	public static boolean updateShareBoolean(Intent data, Activity ac, String key,
			boolean defValue) {
		if (data != null) {
			boolean b = data.getBooleanExtra(key, defValue);
			if (b != getSharedBoolean(ac, key, defValue)) {
				putSharedBoolean(ac, key, b);
				return true;
			}
		}
		return false;
	}
	
	public static boolean updateShareString(Intent data, Activity ac, String key) {
		if (data != null) {
			String s = data.getStringExtra(key);

			if (s != null) {
				s = s.trim();

				if (s.length() == 0) {
					s = null;
				}
			}

			if (!TextUtils.equals(s, getSharedString(ac, key, null))) {
				putSharedString(ac, key, s);
				return true;
			}
		}
		return false;
	}
	
}
