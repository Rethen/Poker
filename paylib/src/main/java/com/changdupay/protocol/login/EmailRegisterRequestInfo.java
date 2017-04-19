package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class EmailRegisterRequestInfo extends BaseRequestInfo{
	public EmailRegisterRequestInfo()
	{
		super();
		
		Content = new EmailRegisterRequestContent();
		ActionID = PayConst.EMAILREGISTER_ACTION;
		RequestHeader.mActionID = ActionID;
	}

	public class EmailRegisterRequestContent extends BaseContent
	{
		public String mEmail = "";
		public String mPassword = "";
		
		public EmailRegisterRequestContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("Email:");
				str.append(mEmail);
				str.append(",");
				
				str.append("Password:");
				str.append(mPassword);

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
