package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class CheckChinaMobileVerifyCodeReuqestInfo extends BaseRequestInfo {


	public CheckChinaMobileVerifyCodeReuqestInfo()
	{
		super();

		Content = new CheckChinaMobileVerifyCodeContent();
		ActionID = PayConst.CHECK_CHINAMOBILE_VERIFYCODE_ACTION;
		RequestHeader.mActionID = ActionID;
	}
	
	public class CheckChinaMobileVerifyCodeContent extends BaseContent
	{
		public int PayId;
		public long UserID;
		public String UserName = "";
		public String OrderId = "";
		public String VerifyUrl = "";
		public String VerifyCode = "";
		//订单号
		public String OrderSerial = "";
		//订单提交时间
		public String StartDateTime = "";

		public CheckChinaMobileVerifyCodeContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("PayId:");
				str.append(PayId);
				str.append(",");

				str.append("UserID:");
				str.append(UserID);
				str.append(",");

				str.append("UserName:");
				str.append(UserName);
				str.append(",");

				str.append("OrderId:");
				str.append(OrderId);
				str.append(",");

				str.append("VerifyUrl:");
				str.append(VerifyUrl);
				str.append(",");

				str.append("VerifyCode:");
				str.append(VerifyCode);
				str.append(",");

				str.append("OrderSerial:");
				str.append(OrderSerial);
				str.append(",");

				str.append("StartDateTime:");
				str.append(StartDateTime);

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
