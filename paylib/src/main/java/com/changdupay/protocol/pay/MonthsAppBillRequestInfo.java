package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class MonthsAppBillRequestInfo extends BaseRequestInfo {

	public MonthsAppBillRequestInfo()
	{
		super();
		
		Content = new MonthsAppBillRequestContent();
		ActionID = PayConst.GET_MONTHS_APPBILL_ACTION;
		RequestHeader.mActionID = ActionID;
	}
		
	public class MonthsAppBillRequestContent extends BaseContent
	{
		public long AppID = 0;
		public long UserID = 0;
		public int PageSize = 0;//每页显示条数
		
		public MonthsAppBillRequestContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				
				str.append("AppID:");
				str.append(AppID);
				str.append(",");
				
				str.append("UserID:");
				str.append(UserID);
				str.append(",");
				
				str.append("PageSize:");
				str.append(PageSize);

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
