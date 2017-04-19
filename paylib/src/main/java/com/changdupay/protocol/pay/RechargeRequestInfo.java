package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class RechargeRequestInfo extends BaseRequestInfo{
	
	public RechargeRequestInfo()
	{
		super();
		
		Content = new RechargeRequestContent();
		ActionID = PayConst.RECHARGE_ACTION;
		RequestHeader.mActionID = PayConst.RECHARGE_ACTION;
	}
	
	public class RechargeRequestContent extends RequestContent
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public double Amount  = 50.0;
		public RechargeRequestContent()
		{
			super();
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
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
				
				str.append("Amount:");
				str.append(Amount);
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
