package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class CheckEmailValidRequestInfo extends BaseRequestInfo{
	public CheckEmailValidRequestInfo()
	{
		super();
		
		Content = new CheckEmailValidContent();
		ActionID = PayConst.EMAILCHECK_ACTION;
		RequestHeader.mActionID = ActionID;
	}
	
	public class CheckEmailValidContent extends BaseContent
	{
		public String mEmail = "";
		
		public CheckEmailValidContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("Email:");
				str.append(mEmail);

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
