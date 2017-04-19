package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class BillDetailRequestInfo extends BaseRequestInfo {

	public BillDetailRequestInfo()
	{
		super();
		
		Content = new BillDetailContent();
		ActionID = PayConst.GETBILL_ACTION;
		RequestHeader.mActionID = ActionID;
	}
		
	public class BillDetailContent extends BaseContent
	{
		public String OrderSerial = "";
		public String Date = "";//时间 ，格式2010-10-01 23:59:59:888
		
		public BillDetailContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("OrderSerial:");
				str.append(OrderSerial);
				str.append(",");
				
				str.append("Date:");
				str.append(Date);
				
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
