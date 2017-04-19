package com.changdupay.protocol.base;


public class RequestHeader {
	public int mActionID = -1;
	
	//商户ID
	public long MerchantID = PayConst.MERCHANTID;
	
	//应用ID
	public long AppID = PayConst.APPID;
	
	//支付类型
	public int PayType = 1;
	
	//支付渠道
	public int PayID = 0;
	
	//版本
	public String Ver = PayConst.VER;
	
	//Content格式(1:JSON)
	public short Format = PayConst.DataFormat.BINARY;
	
	//Content格式(1:JSON)
	public short ReturnFormat = PayConst.DataFormat.BINARY;
	
	public String SessionID = PayConst.SessionID;
	
	//签名
	public String Sign = "";
	
	//平台类型
	public int OsType = PayConst.PLAT_ANDROID;
	
	/**加密格式
	*1.MD5
	*2:RSA
	*3:3DES
·	*/
	public short SignType = 1;
	
	//是否压缩
	public int HasCompress = 0;
	
	//IP地址
	public long IPAddress = 0;
/*	
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
*/	
	public RequestHeader()
	{
	}
	
	private void initData() {
/*
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
	*/	
	}
	
	
	public String getHeader()
	{		
		initData();		
		
		StringBuilder header = new StringBuilder();
		if (mActionID != -1)
		{
			header.append("ActionID=");
			header.append(this.mActionID);
			header.append("&");
		}
		header.append("MerchantID=");
		header.append(this.MerchantID);
		header.append("&");
		
		header.append("AppID=");
		header.append(this.AppID);
		header.append("&");
		
		header.append("Ver=");
		header.append(this.Ver);
		header.append("&");
		
		header.append("OsType=");
		header.append(this.OsType);
		header.append("&");
		
		header.append("Format=");
		header.append(this.Format);
		header.append("&");
		
		header.append("ReturnFormat=");
		header.append(this.ReturnFormat);
		header.append("&");
		
		header.append("SignType=");
		header.append(this.SignType);
		header.append("&");
		
		header.append("HasCompress=");
		header.append(this.HasCompress);
		header.append("&");
		
		header.append("IPAddress=");
		header.append(this.IPAddress);
		header.append("&");
				
		header.append("SessionID=");
		header.append(this.SessionID);
		header.append("&");
		
		header.append("Sign=");
		header.append(this.Sign);
		//header.append("&");
/*		
		header.append("PayType=");
		header.append(this.PayType);
		header.append("&");
		
		header.append("PayID=");
		header.append(this.PayID);
		header.append("&");
		
		header.append("ScreenWidth=");
		header.append(this.ScreenWidth);
		header.append("&");
		
		header.append("ScreenHeight=");
		header.append(this.ScreenHeight);
		header.append("&");
		
		header.append("IMEI=");
		header.append(this.IMEI);
		header.append("&");
		
		header.append("LocalLanguage=");
		header.append(this.LocalLanguage);
		header.append("&");
		
		header.append("PhoneModel=");
		header.append(this.PhoneModel);
		header.append("&");
		
		header.append("SDKVersion=");
		header.append(this.SDKVersion);
		header.append("&");
		
		header.append("ReleaseVersion=");
		header.append(this.ReleaseVersion);
*/		
		String content = header.toString();//.replaceAll(" ", "");
		
		return content;
	}
}
