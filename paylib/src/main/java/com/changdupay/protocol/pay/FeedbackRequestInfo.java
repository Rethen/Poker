package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

public class FeedbackRequestInfo extends BaseRequestInfo {

	public FeedbackRequestInfo()
	{
		super();
		
		Content = new FeedbackContent();
		ActionID = PayConst.FEEDBACK_ACTION;
		RequestHeader.mActionID = ActionID;
	}
		
	public class FeedbackContent extends BaseContent
	{
		public long UserID = 0;
		public String Message = "";
		
		public FeedbackContent()
		{
			
		}
		
		public String toString()
		{
			try{
				StringBuilder str = new StringBuilder();
				str.append("UserID:");
				str.append(UserID);
				str.append(",");
				
				str.append("Message:");
				str.append(Message);

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
