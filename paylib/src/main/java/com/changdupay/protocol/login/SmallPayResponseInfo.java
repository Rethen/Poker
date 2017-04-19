package com.changdupay.protocol.login;

import org.json.JSONObject;

import com.changdupay.encrypt.MD5Utils;
import com.changdupay.protocol.base.BaseResponseInfo;
import com.changdupay.protocol.base.IBaseResponseInfo;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.protocol.pay.PayResponseResultCode;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.Utils;

public class SmallPayResponseInfo extends BaseResponseInfo{
	public SmallPayResponseInfo()
	{
		super();
	}
	
	public IBaseResponseInfo createResponseInfoFromJson(JSONObject objContent)
	{
		try
		{
			this.ResultCode = objContent.getInt("ResultCode");
			this.ResultMsg = Utils.filterQuotationmarks(objContent.getString("ResultMsg"));
			this.MerchantID = objContent.getLong("MerchantID");
			this.AppID = objContent.getLong("AppID");
			this.SignType = objContent.getInt("SignType");
			this.Sign = Utils.filterQuotationmarks(objContent.getString("Sign"));
		}
		catch(Exception e)
		{
			this.ResultCode = PayResponseResultCode.EXCEPTIONTHROW;
			e.printStackTrace();
		}
		
		return this;
	}
	
	public String getSignString()
	{
		MD5Utils md5 = new MD5Utils();
		StringBuilder str = new StringBuilder();
		str.append(PayConst.SMALLPAY_ACTION);
		str.append(this.MerchantID);
		str.append(this.AppID);
		str.append(this.ResultCode);
		if (this.mJSONContent != null)
			str.append(this.mJSONContent.toString());
		str.append(PayConfigReader.getInstance().getPayConfigs().DynamicKey);
		String sign = md5.MD5_Encode(str.toString());
		md5 = null;
		str = null;
		
		return sign;
	}
}
