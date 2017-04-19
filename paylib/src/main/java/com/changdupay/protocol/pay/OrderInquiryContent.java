package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;

public class OrderInquiryContent extends BaseContent{
	//订单号
	public String OrderSerial = "";
	
	public String toString()
	{
		try{
			StringBuilder str = new StringBuilder();
			str.append("OrderSerial:");
			str.append(OrderSerial);
			
			String content = str.toString();
			JSONObject jsonObj = JsonUtils.string2JSON(content, ",");
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
