package com.changdupay.protocol.base;

import org.json.JSONObject;

import com.changdupay.app.PayOrderInfoManager;
import com.changdupay.encrypt.JsonUtils;
import com.changdupay.util.Deviceinfo;
import com.changdupay.util.Utils;

public class DeviceInfo extends BaseContent {
	private static DeviceInfo mDeviceInfo = null;
	
	public static DeviceInfo getInstance() {
		if (mDeviceInfo == null) {
			mDeviceInfo = new DeviceInfo();
		}
		
		return mDeviceInfo;
	}

	public String getContent() {
		StringBuilder content = new StringBuilder();
		content.append("DeviceInfo=");
		content.append(Utils.replaceMd5Data(this.toBase64String()));
		
		return content.toString();
	}

	//屏幕宽度
	public int ScreenWidth = 0;
	
	//屏幕高度
	public int ScreenHeight = 0;
	
	//IMEI
	public String IMEI = null;
	
	//本地语言
	public String LocalLanguage = null;
	
	//本地语言
	public String PhoneModel = null;
	
	//SDK版本
	public String SDKVersion = null;
	
	//SDK版本
	public String ReleaseVersion = null;
	
	public DeviceInfo()
	{
		
	}

	private void initData() {
		if (ScreenWidth == 0) {
			ScreenWidth = Deviceinfo.getScreenWidth();
		}

		if (ScreenHeight == 0) {
			ScreenHeight = Deviceinfo.getScreenHeight();
		}

		if (IMEI == null) {
			IMEI = Deviceinfo.getPhoneIMEI();
		}

		if (LocalLanguage == null) {
			LocalLanguage = Deviceinfo.getLocalLanguage();
		}

		if (PhoneModel == null) {
			PhoneModel = Deviceinfo.getPhoneModel().replaceAll(" ", "");
		}

		if (SDKVersion == null) {
			SDKVersion = Deviceinfo.getPhoneSDKVersion();
		}

		if (ReleaseVersion == null) {
			ReleaseVersion = Deviceinfo.getPhoneReleaseVersion();
		}
		
	}
	
	public String toString()
	{
		initData();
		
		try{
			StringBuilder str = new StringBuilder();

			str.append("ScreenWidth:");
			str.append(this.ScreenWidth);
			str.append("&");
			
			str.append("ScreenHeight:");
			str.append(this.ScreenHeight);
			str.append("&");
			
			str.append("IMEI:");
			str.append(this.IMEI);
			str.append("&");
			
			str.append("LocalLanguage:");
			str.append(this.LocalLanguage);
			str.append("&");
			
			str.append("PhoneModel:");
			str.append(this.PhoneModel);
			str.append("&");
			
			str.append("SDKVersion:");
			str.append(this.SDKVersion);
			str.append("&");
			
			str.append("ReleaseVersion:");
			str.append(this.ReleaseVersion);
			str.append("&");
			
			str.append("ServerId:");
			str.append(PayOrderInfoManager.getPayOrderInfoManager().mPayOrderInfo.serverId);

			String content = str.toString();//.replaceAll(" ", "");
			JSONObject jsonObj = JsonUtils.string2JSON(content, "&");

			str = null;
			return jsonObj.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
