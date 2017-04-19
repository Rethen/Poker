package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class MobilePhoneRegisterRequestInfo extends BaseRequestInfo{
	public MobilePhoneRegisterRequestInfo()
	{
		super();
		
		Content = new MobilePhoneRegisterRequestContent();
		ActionID = PayConst.MOBILEPHONEREGISTER_ACTION;
		RequestHeader.mActionID = ActionID;
	}
	
	public class MobilePhoneRegisterRequestContent extends BaseContent
	{
		public String mMobilePhone = "";
		public String mPassword = "";
		public String mVerifyCode = "";
		
		public MobilePhoneRegisterRequestContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("MobilePhone:");
				str.append(mMobilePhone);
				str.append(",");
				
				str.append("Password:");
				str.append(mPassword);
				str.append(",");
				
				str.append("VerifyCode:");
				str.append(mVerifyCode);

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
