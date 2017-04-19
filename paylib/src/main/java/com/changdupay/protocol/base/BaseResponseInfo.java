package com.changdupay.protocol.base;

import org.json.JSONObject;

public class BaseResponseInfo implements IBaseResponseInfo{
	//商户ID
	public long MerchantID = 0;
	
	//应用ID
	public long AppID = 0;
	
	/**操作结果编号
	*1：成功 
	*其它：失败
	**/
	public int ResultCode = 0;
	
	//操作结果描述
	public String ResultMsg = "";

	//签名
	public String Sign = "";
	
	/** 加密类型
	*	1:MD5
	*	2:RSA
	*	3:3DES
	*/
	public int SignType = 1;
	
	public int mActionID = -1;
	public JSONObject mJSONContent = null;
	
	public IBaseContent mContent = null;
	
	public BaseResponseInfo()
	{
		
	}
	
	public IBaseResponseInfo createResponseInfoFromJson(JSONObject objContent)
	{
		return null;
	}
	

	public int getResultCode()
	{	
		return this.ResultCode;
	}
	
	public String getSignString()
	{
		return "";
	}

	@Override
	public String getResultMsg() {
		return this.ResultMsg;
	}
}
