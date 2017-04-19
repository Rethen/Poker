package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class GetAccountInfoRequestInfo extends BaseRequestInfo{
	public GetAccountInfoRequestInfo()
	{
		super();
		
		Content = new GetAccountInfoRequestContent();
		ActionID = PayConst.GET_ACCOUNTINFO_ACTION;
		RequestHeader.mActionID = PayConst.GET_ACCOUNTINFO_ACTION;
	}
	
	public class GetAccountInfoRequestContent extends BaseContent
	{
		public String mUserID = "";
		public GetAccountInfoRequestContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("UserID:");
				str.append(mUserID);

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
}
