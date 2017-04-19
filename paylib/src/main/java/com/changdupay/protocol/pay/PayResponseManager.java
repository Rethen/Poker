package com.changdupay.protocol.pay;
import org.json.JSONObject;

import android.content.Context;

import com.changdupay.encrypt.JsonUtils;
import com.changdupay.encrypt.SmsUtils;
import com.changdupay.net.netengine.NetReader;
import com.changdupay.protocol.ProtocolData.BaseProtocalData;
import com.changdupay.protocol.ProtocolData.PayEntity;
import com.changdupay.protocol.ProtocolParser;
import com.changdupay.protocol.base.IBaseResponseInfo;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.ContextUtil;

/** 返回值管理器
 * 
 * @author Administrator
 *
 */
public class PayResponseManager {
	private static PayResponseManager mPayResponseManager = null;
	public static PayResponseManager getInstance() {
		if (mPayResponseManager == null) {
			mPayResponseManager = new PayResponseManager();
		}
		return mPayResponseManager;
	}
	
	public static short ResponseType = PayResponseType.PAY;
	public static IBaseResponseInfo getResponseObjFromString(String result, String split)
	{
		IBaseResponseInfo response = null;
		try
		{
			JSONObject objContent = JsonUtils.string2JSON(result, split);
			switch(ResponseType)
			{
			case PayResponseType.ORDERCREATE:
				response = new OrderCreateResponseInfo();
				response.createResponseInfoFromJson(objContent);
				break;
			case PayResponseType.INQUIRY:
				response = new OrderInquiryResponseInfo();
				response.createResponseInfoFromJson(objContent);
				break;
			case PayResponseType.PAYRESULT:
				response = new PayResultResponseInfo();
				response.createResponseInfoFromJson(objContent);
				break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return response;
	}
	

	public void processResponse(int actionid, byte[] data, Context context)
	{		
		if (data == null) {
			//sendMessage(actionid, null);
			return;
		}
		
		NetReader reader = new NetReader(data);
		//reader.openDebugMode();
		BaseProtocalData resultData = ProtocolParser.parseProtocal(reader);
		if (resultData != null) {
			resultData.result = reader.getResult();
			resultData.errorMsg = reader.getErrorMsg();
			resultData.ActionID = String.valueOf(reader.getActionId());
			resultData.ApplicationID = String.valueOf(reader.getAppId());
			resultData.NextUpdateTimeSpan = System.currentTimeMillis();
		}
		
		switch (actionid)
		{
		case PayConst.PAY_ACTION: //支付成功				
			processPayResult(resultData, context);
			break;
		}
		//sendMessage(actionid, mBaseProtocalData);
	}
	
	private void processPayResult(Object info, Context context) {
		if (info instanceof PayEntity) {
			PayEntity entity = (PayEntity)info;
			Context ctx = context != null ? context : ContextUtil.getContext();
			switch (entity.ExecType) {
			case PayConst.PayExecType.LaunchApp:
				com.changdupay.util.Utils.launchApp(ctx, entity.PackageName, entity.Parameter);
				break;
			case PayConst.PayExecType.SendSms:
				SmsUtils.sendMsg(entity.Message, entity.Receiver);
				break;
			default:
				break;
			}
		}
	}
}
