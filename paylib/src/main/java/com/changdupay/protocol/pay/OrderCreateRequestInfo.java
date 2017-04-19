package com.changdupay.protocol.pay;

import org.json.JSONObject;

import android.util.Log;

import com.changdupay.app.UserInfo;
import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.Deviceinfo;

/**创建订单信息
 * 
 * @author Administrator
 *
 */
public class OrderCreateRequestInfo extends BaseRequestInfo{
	//支付方式
	public int PayType = 0;
	
	//支付渠道,可不传。服务端根据PayType分配
	public int PayID = 0;

	public OrderCreateRequestInfo()
	{
		super();
		
		Content = new OrderCreateContent();
		ActionID = PayConst.ORDERCREATE_ACTION;
		RequestHeader.mActionID = PayConst.ORDERCREATE_ACTION;
	}
	
	/*
	public String getSign()
	{
		StringBuilder str = new StringBuilder();
		str.append(PayConst.MERCHANTID);
		str.append(PayConst.APPID);
		str.append(PayConst.VER);
		str.append(PayConst.FORMAT);
		str.append(this.PayType);
		str.append(this.PayID);
		if (Content != null)
			str.append(Content.toBase64String());
		str.append(PayConfigReader.getInstance().getPayConfigs().DynamicKey);
		
		return str.toString();
	}*/
	
	public class OrderCreateContent extends RequestContent
	{
		public String LoginToken = "";
		public String MAC = Deviceinfo.getPhoneMac();
		public String IMEI = Deviceinfo.getPhoneIMEI();
		//必传
		public String IMSI = Deviceinfo.getPhoneIMSI();
		public OrderCreateContent()
		{
			super();
			
			LoginToken = UserInfo.getInstance().mLoginToken;
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("LoginToken:");
				str.append(LoginToken);
				str.append(",");
				
				str.append("MAC:");
				str.append(MAC);
				str.append(",");
				
				str.append("IMEI:");
				str.append(IMEI);
				str.append(",");
				
				str.append("IMSI:");
				str.append(IMSI);
				str.append(",");
				
				str.append("PayType:");
				str.append(PayType);
				str.append(",");
				
				str.append("PayId:");
				str.append(PayId);
				str.append(",");
				
				str.append("UserID:");
				str.append(UserID);
				str.append(",");
				
				str.append("UserName:");
				str.append(UserName);
				str.append(",");
				
				str.append("MerchandiseID:");
				str.append(MerchandiseID);
				str.append(",");
				
				str.append("MerchandiseName:");
				str.append(MerchandiseName);
				str.append(",");
				
				str.append("CooperatorOrderSerial:");
				str.append(CooperatorOrderSerial);
				str.append(",");
				
				str.append("CardNumber:");
				str.append(CardNumber);
				str.append(",");
				
				str.append("CardPassword:");
				str.append(CardPassword);	
				str.append(",");
				
				str.append("PhoneNumber:");
				str.append(PhoneNumber);
				str.append(",");
				
				str.append("BankCode:");
				str.append(BankCode);	
				str.append(",");
				
				str.append("CheckPayPassword:");
				str.append(CheckPayPassword);
				str.append(",");
				
				str.append("PayPassword:");
				str.append(PayPassword);	
				str.append(",");
				
				str.append("OrderMoney:");
				str.append(OrderMoney);
				str.append(",");

				str.append("ReturnUrl:");
				str.append(ReturnUrl);
				str.append(",");
				
				str.append("NotifyUrl:");
				str.append(NotifyUrl);
				str.append(",");
				
				str.append("ExtInfo:");
				str.append(ExtInfo);
				str.append(",");
				
				str.append("IPAddress:");
				str.append(IPAddress);
				str.append(",");
				
				if(ShopItemId.length() > 0)
				{
					str.append("ShopItemId:");
					str.append(ShopItemId);
					str.append(",");
				}
				
			
				str.append("Remark:");
				str.append(Remark);
				String content = str.toString();//.replaceAll(" ", "");
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
}
