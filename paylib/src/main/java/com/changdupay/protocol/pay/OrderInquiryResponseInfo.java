package com.changdupay.protocol.pay;

import org.json.JSONObject;

import com.changdupay.encrypt.Base64;
import com.changdupay.encrypt.MD5Utils;
import com.changdupay.protocol.base.BaseContent;
import com.changdupay.protocol.base.BaseResponseInfo;
import com.changdupay.protocol.base.IBaseResponseInfo;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.Utils;

/** 订单查询返回值
 * 
 * @author Administrator
 *
 */
public class OrderInquiryResponseInfo extends BaseResponseInfo{
	public OrderInquiryResponseInfo()
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
			String baseContent = Utils.filterQuotationmarks(objContent.getString("Content"));
			this.mContent = createContentFromJson(baseContent);
			
		}
		catch(Exception e)
		{
			this.ResultCode = PayResponseResultCode.EXCEPTIONTHROW;
			e.printStackTrace();
		}
		
		return this;
	}
	
	public OrderInquiryResponseContent createContentFromJson(String baseContent)
	{	
		OrderInquiryResponseContent content = null;
		try
		{
			String strContent = new String(Base64.decode(baseContent));
			//this.mJSONContent = JsonUtils.string2JSON(strContent, ",");
			this.mJSONContent = new JSONObject(strContent); 
			content = new OrderInquiryResponseContent();
			content.OrderSeiral = Utils.filterQuotationmarks(this.mJSONContent.getString("OrderSeiral"));
			content.CooperatorOrderSerial = Utils.filterQuotationmarks(this.mJSONContent.getString("CooperatorOrderSerial"));
			content.OrderMoney = Utils.filterQuotationmarks(this.mJSONContent.getString("OrderMoney"));
			content.OrderStatus = this.mJSONContent.getInt("OrderStatus");
			content.PayType = this.mJSONContent.getInt("PayType");
			content.PayID = this.mJSONContent.getInt("PayID");
			content.StartDateTime = Utils.filterQuotationmarks(this.mJSONContent.getString("StartDateTime"));
			content.EndDateTime = Utils.filterQuotationmarks(this.mJSONContent.getString("EndDateTime"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return content;
	}
	
	public String getSignString()
	{
		MD5Utils md5 = new MD5Utils();
		StringBuilder str = new StringBuilder();
		str.append(this.MerchantID);
		str.append(this.AppID);
		str.append(this.ResultCode);
		if (mJSONContent != null)
			str.append(this.mJSONContent.toString());
		str.append(PayConfigReader.getInstance().getPayConfigs().DynamicKey);
		String sign = md5.MD5_Encode(str.toString());
		md5 = null;
		str = null;
		
		return sign;
	}
	
	public class OrderInquiryResponseContent extends BaseContent{
		//订单号
		public String OrderSeiral = "";
		
		//商户订单号
		public String CooperatorOrderSerial = "";
		
		//订单金额
		public String OrderMoney = "";
		
		/**订单状态
		*1：成功
		*0：成功
		*/
		public int OrderStatus = 1;
		
		//支付方式
		public int PayType = 1;
		
		//支付渠道
		public int PayID = 1;
		
		//订单创建时间
		public String StartDateTime = "";
		
		//订单完成时间
		public String EndDateTime = "";
		
		public OrderInquiryResponseContent()
		{
			
		}
		
		public String toString()
		{
			StringBuilder str = new StringBuilder();
			str.append("OrderSeiral:");
			str.append(OrderSeiral);
			str.append(",");
			
			str.append("CooperatorOrderSerial:");
			str.append(CooperatorOrderSerial);
			str.append(",");
			
			str.append("OrderMoney:");
			str.append(OrderMoney);
			str.append(",");
			
			str.append("OrderStatus:");
			str.append(OrderStatus);
			str.append(",");
			
			str.append("PayType:");
			str.append(PayType);
			str.append(",");
			
			str.append("PayID:");
			str.append(PayID);
			str.append(",");
			
			str.append("StartDateTime:");
			str.append(StartDateTime);
			str.append(",");
			
			str.append("EndDateTime:");
			str.append(EndDateTime);
			
			return str.toString();
		}
	}
	
	
}
