package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class SmallPayRequestInfo extends BaseRequestInfo{
	public SmallPayRequestInfo()
	{
		super();
		
		Content = new SmallPayRequestContent();
		ActionID = PayConst.SMALLPAY_ACTION;
		RequestHeader.mActionID = ActionID;
	}
	
	public class SmallPayRequestContent extends BaseContent
	{
		public String mUserID = "";
		public int mAvoidPassword = 0;
		public double mMoney = 0.0;
		public String mPayPassword = "";
		
		public SmallPayRequestContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("UserID:");
				str.append(mUserID);
				str.append(",");
				
				str.append("AvoidPassword:");
				str.append(mAvoidPassword);
				str.append(",");
				
				str.append("Money:");
				str.append(String.format("%.2f", mMoney));
				str.append(",");
				
				str.append("PayPassword:");
				str.append(mPayPassword);

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
