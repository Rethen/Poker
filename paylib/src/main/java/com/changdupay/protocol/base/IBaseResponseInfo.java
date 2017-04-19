package com.changdupay.protocol.base;

import org.json.JSONObject;

public interface IBaseResponseInfo {
	public IBaseResponseInfo createResponseInfoFromJson(JSONObject objContent);
	public int getResultCode();
	public String getResultMsg();
	public String getSignString();

}
