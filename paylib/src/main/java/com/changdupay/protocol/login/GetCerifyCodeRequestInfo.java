package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class GetCerifyCodeRequestInfo extends BaseRequestInfo{
	public GetCerifyCodeRequestInfo()
	{
		super();
		
		Content = new GetCerifyCodeContent();
		ActionID = PayConst.VERIFYCODE_ACTION;
		RequestHeader.mActionID = ActionID;
	}
		
	public class GetCerifyCodeContent extends BaseContent
	{
		public String mMobilePhone = "";
		
		public GetCerifyCodeContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("MobilePhone:");
				str.append(mMobilePhone);

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
