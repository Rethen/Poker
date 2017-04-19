package com.changdupay.util;

import java.util.Locale;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

public class Deviceinfo {
	private static Context mContext;
	public static void setContext(Context context)
	{
		mContext = context;
	}
	
	public static void LogDeviceInfo()
	{
		TelephonyManager tm = (TelephonyManager)ContextUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);  
        StringBuilder sb = new StringBuilder();  
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());  
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());  
        sb.append("\nLine1Number = " + tm.getLine1Number());  
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());  
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());  
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());  
        sb.append("\nNetworkType = " + tm.getNetworkType());  
        sb.append("\nPhoneType = " + tm.getPhoneType());  
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());  
        sb.append("\nSimOperator = " + tm.getSimOperator());  
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());  
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());  
        sb.append("\nSimState = " + tm.getSimState());  
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());  
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber()); 
        sb.append("\nBuild.MODEL = " + Build.MODEL); 
        sb.append("\nBuild.VERSION.SDK = " + Build.VERSION.SDK); 
        sb.append("\nBuild.VERSION.RELEASE = " + Build.VERSION.RELEASE); 
        
        Log.e("info", sb.toString()); 
	}
	
	public static String getLocalLanguage()
	{
		return Locale.getDefault().getCountry();
	}
	
	public static String getPhoneIMEI()
	{
		String IMEI = new String();
		if(ContextUtil.getContext() != null)
		{
			TelephonyManager gTeleManager = (TelephonyManager)ContextUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			IMEI = gTeleManager.getDeviceId();
		}
		return IMEI;
	}
	
	public static String getPhoneIMSI()
	{
		String IMEI = new String();
		if(ContextUtil.getContext() != null)
		{
			TelephonyManager gTeleManager = (TelephonyManager)ContextUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			IMEI = gTeleManager.getSubscriberId();
		}
		return IMEI;
	}
	
	public static String getPhoneModel()
	{
		return Build.MODEL;
	} 
	
	public static String getPhoneSDKVersion()
	{
		return Build.VERSION.SDK;
	} 
	
	public static String getPhoneReleaseVersion()
	{
		return Build.VERSION.RELEASE;
	}
	
	public static String getDeviceSoftwareVersion()
	{
		String version = new String();
		if(ContextUtil.getContext() != null)
		{
			TelephonyManager gTeleManager = (TelephonyManager)ContextUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			version = gTeleManager.getDeviceSoftwareVersion();
		}
		return version;
	}
	
	public static int getScreenWidth()
	{
		DisplayMetrics dm = new DisplayMetrics();  
		dm = ContextUtil.getContext().getResources().getDisplayMetrics();
		
		return dm.widthPixels;
	}
	
	public static int getScreenHeight()
	{
		DisplayMetrics dm = new DisplayMetrics();  
		dm = ContextUtil.getContext().getResources().getDisplayMetrics();
		
		return dm.heightPixels;
	}
	
	public static String getPhoneNumber()
	{
		String phone = null;
		if(ContextUtil.getContext() != null)
		{
			TelephonyManager gTeleManager = (TelephonyManager)ContextUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			phone = gTeleManager.getLine1Number();
			if (phone != null && phone.length() > 11) {
				phone = phone.substring(phone.length() - 11);
			}
		}
		return phone;
	}

	public static int[] getSimOperators()
	{
		int[] operators = new int[1];
		operators[0] = -1; //Const.SimOperator.Unknow;
		if(ContextUtil.getContext() != null)
		{
			TelephonyManager gTeleManager = (TelephonyManager)ContextUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);
			String str = gTeleManager.getSimOperator();
			if (str!= null && !TextUtils.isEmpty(str)) {
				String[] opeStrs = str.split(",");
				try {
					if (opeStrs != null && opeStrs.length > 0) {
						int len = opeStrs.length;
						operators = new int[len];
						for (int i = 0; i < opeStrs.length; i++) {
							operators[i] = Integer.parseInt(opeStrs[i]);
						}
					}					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		
		return operators;
	}
	
	public static String getPhoneMac() {
		String strMac = "";
		try {
			WifiManager wifi = (WifiManager) ContextUtil.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			strMac = info.getMacAddress();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return strMac;
	}
}
