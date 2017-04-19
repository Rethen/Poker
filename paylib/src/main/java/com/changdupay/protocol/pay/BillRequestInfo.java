package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class BillRequestInfo extends BaseRequestInfo {

	public BillRequestInfo()
	{
		super();
		
		Content = new BillRequestContent();
		ActionID = PayConst.GETBILL_ACTION;
		RequestHeader.mActionID = PayConst.GETBILL_ACTION;
	}
		
	public class BillRequestContent extends BaseContent
	{
		public long UserID = 0;
		public String Date = "";//时间 ，格式2010-10-01 23:59:59:888
		public int PageSize = 0;//每页显示条数
		public int PageIndex = 0;//当前页
		
		public BillRequestContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("UserID:");
				str.append(UserID);
				str.append(",");
				
				str.append("Date:");
				str.append(Date);
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
