package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;

public class PayResultContent extends BaseContent{
	//用户ID
	public long UserID = 0;
	
	//商品ID
	public String MerchandiseID = "";
	
	//订单号
	public String OrderSerial = "";
	
	//订单金额(非必填)
	public String OrderMoney = "";
	
	//支付方式
	public int PayType = 0;
	
	//支付渠道,可不传。服务端根据PayType分配
	public int PayID = 0;
	
	public PayResultContent()
	{
		super();
	}

	public String toString()
	{
		try{
			StringBuilder str = new StringBuilder();
			str.append("UserID:");
			str.append(UserID);
			str.append(",");
			
			str.append("MerchandiseID:");
			str.append(MerchandiseID);
			str.append(",");
			
			str.append("OrderSerial:");
			str.append(OrderSerial);
			str.append(",");
			
			str.append("OrderMoney:");
			str.append(OrderMoney);
			str.append(",");
			
			str.append("PayType:");
			str.append(PayType);	
			str.append(",");
			
			str.append("PayID:");
			str.append(PayID);
			
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
