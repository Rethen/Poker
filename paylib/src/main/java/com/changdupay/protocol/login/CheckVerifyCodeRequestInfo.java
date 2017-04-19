package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class CheckVerifyCodeRequestInfo extends BaseRequestInfo {

	public CheckVerifyCodeRequestInfo() {
		super();

		Content = new CheckVerifyCodeContent();
		ActionID = PayConst.CHECK_VERIFYCODE_ACTION;
		RequestHeader.mActionID = ActionID;
	}
	
	public class CheckVerifyCodeContent extends BaseContent
	{
		public String VerifyCode = "";
		public String MobilePhone = "";
		
		public CheckVerifyCodeContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("VerifyCode:");
				str.append(VerifyCode);
				str.append(",");
				
				str.append("MobilePhone:");
				str.append(MobilePhone);

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
