package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.protocol.base.BaseResponseInfo;
import com.changdupay.protocol.base.IBaseResponseInfo;
import com.changdupay.util.Utils;

/**支付结果通知
 * 
 * @author Administrator
 *
 */
public class PayResultResponseInfo extends BaseResponseInfo{

	//完成时间
	public String ResultDate = "";
	
	public PayResultResponseInfo()
	{
		super();
	}
	
	public IBaseResponseInfo createResponseInfoFromJson(JSONObject objContent)
	{
		try
		{
			this.ResultCode = objContent.getInt("ResultCode");
			this.ResultMsg = Utils.filterQuotationmarks(objContent.getString("ResultMsg"));
			this.ResultDate = Utils.filterQuotationmarks(objContent.getString("ResultMsg"));
			this.Sign = "";
		}
		catch(Exception e)
		{
			this.ResultCode = PayResponseResultCode.EXCEPTIONTHROW;
			e.printStackTrace();
		}
		
		return this;
	}
}
