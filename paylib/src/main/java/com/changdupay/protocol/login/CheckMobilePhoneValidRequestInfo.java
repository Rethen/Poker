package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.PayConfigReader;

public class CheckMobilePhoneValidRequestInfo  extends BaseRequestInfo{
	public CheckMobilePhoneValidRequestInfo()
	{
		super();
		
		Content = new CheckMobilePhoneValidContent();
	}
	
	public String getSign()
	{
		StringBuilder str = new StringBuilder();
		str.append(PayConst.MOBILEPHONECHECK_ACTION);
		str.append(PayConst.MERCHANTID);
		str.append(PayConst.APPID);
		str.append(PayConst.VER);
		str.append(PayConst.FORMAT);
		if (Content != null)
			str.append(Content.toBase64String());
		str.append(PayConfigReader.getInstance().getPayConfigs().DynamicKey);
		
		return str.toString();
	}
	
	public class CheckMobilePhoneValidContent extends BaseContent
	{
		public String mMobilePhone = "";
		
		public CheckMobilePhoneValidContent()
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
