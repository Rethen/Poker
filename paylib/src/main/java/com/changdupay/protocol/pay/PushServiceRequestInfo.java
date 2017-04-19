package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class PushServiceRequestInfo extends BaseRequestInfo {

	public PushServiceRequestInfo()
	{
		super();
		
		Content = new PushServiceContent();
		ActionID = PayConst.PUSH_SERVICE_ACTION;
		RequestHeader.mActionID = ActionID;
	}
		
	public class PushServiceContent extends BaseContent
	{
		public long UserID = 0;
		public int PageSize = 0;
		public int PageIndex = 0;
		
		public PushServiceContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("UserID:");
				str.append(UserID);
				str.append(",");
				
				str.append("PageSize:");
				str.append(PageSize);
				str.append(",");
				
				str.append("PageIndex:");
				str.append(PageIndex);
				
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
