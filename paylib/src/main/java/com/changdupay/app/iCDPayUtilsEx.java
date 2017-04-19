package com.changdupay.app;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.changdupay.channel.alipay.AlipayHelper;
import com.changdupay.net.netengine.Constant;
import com.changdupay.open.iChangduPay;
import com.changdupay.open.iChangduPay.ResultCode;
import com.changdupay.protocol.LoginResponseManager;
import com.changdupay.protocol.ProtocolData.BaseProtocalData;
import com.changdupay.protocol.ProtocolData.PayEntity;
import com.changdupay.protocol.base.ResponseHandler.OnCreateOrderListener;
import com.changdupay.protocol.base.ResponseHandler.OnRechargeListener;
import com.changdupay.protocol.pay.OrderCreateRequestInfo;
import com.changdupay.protocol.pay.PayRequestManager;
import com.changdupay.protocol.pay.RechargeRequestInfo;
import com.changdupay.protocol.pay.OrderCreateRequestInfo.OrderCreateContent;
import com.changdupay.protocol.pay.RechargeRequestInfo.RechargeRequestContent;
import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;
import com.changdupay.util.PayConfigs.Category;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class iCDPayUtilsEx {
	
	public static  Activity sContent;
	public static boolean mNeedQuitWhenFinish = true;
	public static int sPayCode = 0;
	public static String sTitle="";
	
	public static boolean bRequestNoUI = false;
	public static void doRequest(Activity content, int payCode, String title, int payType, int payId)
	{
		bRequestNoUI = true;
		sContent = content;
		sPayCode = payCode;
		sTitle = title;
		LoginResponseManager.getInstance().getResponseHandler().setRechargeRegisterListener(sOnRechargeListener);
		LoginResponseManager.getInstance().getResponseHandler().setCreateOrderRegisterListener(sOnCreateOrderListener);
		doRequest(payType, payId);
	}
	

	public static void doRequest(int nPayType, int nPayID)
	{
		BaseActivity.showWaitCursor(null, sContent.getString(MResource.getIdByName(sContent.getApplication(), "string", "ipay_wait_for_request_data")));
		
		iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
		
		OrderCreateRequestInfo orderCreateRequestInfo = new OrderCreateRequestInfo();
		((OrderCreateContent)orderCreateRequestInfo.Content).PayId = nPayID;
		((OrderCreateContent)orderCreateRequestInfo.Content).PayType = nPayType;
		((OrderCreateContent)orderCreateRequestInfo.Content).MerchandiseID = payOrderInfo.MerchandiseID;
		((OrderCreateContent)orderCreateRequestInfo.Content).MerchandiseName = payOrderInfo.MerchandiseName;
		((OrderCreateContent)orderCreateRequestInfo.Content).CooperatorOrderSerial = payOrderInfo.CooperatorOrderSerial;
		((OrderCreateContent)orderCreateRequestInfo.Content).PhoneNumber = "";
		((OrderCreateContent)orderCreateRequestInfo.Content).OrderMoney = "" + payOrderInfo.nPayMoney;
		((OrderCreateContent)orderCreateRequestInfo.Content).UserName = payOrderInfo.UserName;
		((OrderCreateContent)orderCreateRequestInfo.Content).UserID = payOrderInfo.UserID;
	
		
		//Log.e("shopItem", shopItem);
		((OrderCreateContent)orderCreateRequestInfo.Content).ShopItemId = payOrderInfo.shopItemId;
		
		PayRequestManager.getInstance().requestCreateOrder(orderCreateRequestInfo, sContent);

		
		//更新首页排序
		int payCode = sPayCode;
		String payName = "";
		if(payCode != -1){
			Category cate = PayConfigReader.getInstance().getPayCategoryByCode(payCode);
			payName = cate.Name;
		}
		if (!TextUtils.isEmpty(payName))
		{
			Utils.resetPayOrder(payName, 1);
			Utils.setStringValue(Const.ConfigKeys.LastPaytype,payName);
		}
	}


	private static void prcoessResponseData(PayEntity entity)
	{
		if (!TextUtils.equals(entity.JumpUrl, ""))
		{
			Intent intent = new Intent(sContent, WebViewActivity.class);
			//
			intent.putExtra(Const.ParamType.TypeTitle, 	sTitle);
			intent.putExtra(Const.ParamType.TypeUrl, 	entity.JumpUrl);
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
			intent.putExtra(Const.ParamType.TypeHideWaitCursor, true);
			
			sContent.startActivityForResult(intent, 1000);
		}
		else if(!TextUtils.equals(entity.PackageName, ""))
		{
			//调用alipay支付
			if (TextUtils.equals(Const.ALIPAY_PACKAGE_NAME, entity.PackageName))
			{
				AlipayHelper ah = new AlipayHelper();
				
				ah.excutePay(entity.Parameter, sContent, mNeedQuitWhenFinish);
				sContent.finish();
			} 

		}
		else if (!TextUtils.equals(entity.Receiver, ""))
		{

		}
		else if (5 == entity.ExecType && !TextUtils.isEmpty(entity.Parameter)){

		}
		else if(7== entity.ExecType && !TextUtils.isEmpty(entity.Parameter))
		{
			if(Const.PayCode.weixin == sPayCode)
			{
				try
				{
					JSONObject obj = new JSONObject(entity.Parameter);
					IWXAPI msgApi;
					msgApi = WXAPIFactory.createWXAPI(sContent, null);
					msgApi.registerApp(PayOrderInfoManager.getPayOrderInfoManager().mPayOrderInfo.WxKey);
					if(msgApi.isWXAppInstalled())
					{
						Log.e("wxKey", PayOrderInfoManager.getPayOrderInfoManager().mPayOrderInfo.WxKey);
						PayReq request = new PayReq();
						request.appId = obj.getString("appid");
						request.partnerId = obj.getString("partnerid");
						request.prepayId = obj.getString("prepayid");
						request.packageValue = obj.getString("package");
						request.nonceStr = obj.getString("noncestr");
						request.timeStamp = obj.getString("timestamp");
						request.sign = obj.getString("sign");
						msgApi.sendReq(request);
						
						
						sContent.finish();
					}
					else
					{

						int id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_mobile_wxnotinstall");
				
						String message = sContent.getString(id);
						ToastHelper.shortDefaultToast(message);
					
						
					}
				}
				catch(Exception e)
				{
					
				}
				
			}
			else
			{
				
			}
			
		}
		else
		{
			Toast.makeText(sContent, "Unknow Type" + entity.ExecType, 1);
		}

	}
	 
	private static OnRechargeListener sOnRechargeListener = new OnRechargeListener()
	{

		@Override
		public void OnRecharge(Object rechargeInfo) {
			// TODO Auto-generated method stub
			BaseActivity.hideWaitCursor();

			
			if (rechargeInfo instanceof PayEntity) 
			{
				PayEntity entity = (PayEntity)rechargeInfo;
				if (true == entity.result) {
					prcoessResponseData(entity);
				}
				else {
					ToastHelper.shortDefaultToast(entity.errorMsg);
				}
			}
			else if (rechargeInfo instanceof BaseProtocalData)
			{
				BaseProtocalData entity = (BaseProtocalData)rechargeInfo;
				ToastHelper.shortDefaultToast(entity.errorMsg);
			}
			else
			{
				showResponseInfo((Integer)rechargeInfo);
			}
		}
		
	};
	
	private static  OnCreateOrderListener sOnCreateOrderListener = new OnCreateOrderListener()
	{

		@Override
		public void OnCreateOrder(Object createOrderInfo) {
			// TODO Auto-generated method stub
			BaseActivity.hideWaitCursor();
			
			if (createOrderInfo == null) {
				return;
			}
			
			if (createOrderInfo instanceof PayEntity) 
			{
				PayEntity entity = (PayEntity)createOrderInfo;
				if (true == entity.result) {
					prcoessResponseData(entity);
				}
				else {
					ToastHelper.shortDefaultToast(entity.errorMsg);
				}
			}
			else if (createOrderInfo instanceof BaseProtocalData)
			{
				BaseProtocalData entity = (BaseProtocalData)createOrderInfo;
				ToastHelper.shortDefaultToast(entity.errorMsg);
			}
			else if (createOrderInfo instanceof Integer) {
				showResponseInfo((Integer)createOrderInfo);
			}
		}
		
	};
	
	public static void  showResponseInfo(int errorCode)
	{
		int id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_connect_to_server_failed");
		switch(errorCode)
		{
		case Constant.ERROR:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_connect_to_server_failed");
			break;
		
		case Constant.PARM_ERROR:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_net_param_error");
			break;
			
		case Constant.MEMORY_ERROR:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_net_memory_error");
			break;
			
		case Constant.FILE_ERROR:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_net_file_error");
			break;
			
		case Constant.CONNECT_ERROR:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_connect_to_server_failed");
			break;
			
		case Constant.READ_ERROR:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_net_read_error");
			break;
			
		case Constant.SPACE_INSUFFICIENT:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_net_read_error");
			break;
			
		case Constant.INTERNAL_ERROR:
			id = MResource.getIdByName(sContent.getApplication(), "string", "ipay_net_internal_error");
			break;
		}
		
		String message = sContent.getString(id);
		ToastHelper.shortDefaultToast(message);
		
		iChangduPay.setResult(ResultCode.Failed, message);
	}
	
	
}
