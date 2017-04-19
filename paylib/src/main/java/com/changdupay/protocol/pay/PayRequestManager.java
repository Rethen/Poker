package com.changdupay.protocol.pay;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.changdupay.app.iCDPayCenterActivity;
import com.changdupay.net.netengine.BufferData;
import com.changdupay.net.netengine.CNetHttpTransfer;
import com.changdupay.open.iChangduPay;
import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.protocol.base.PostStruct;
import com.changdupay.protocol.base.RequestHandlerManager;
import com.changdupay.protocol.base.SessionManager;
import com.changdupay.util.Const;


public class PayRequestManager {
	private static PayRequestManager instance = null;
	
    public static PayRequestManager getInstance(){ 
    	if(instance == null){
    		instance = new PayRequestManager();
    	}
    	return instance;
    }
    
    public PayRequestManager()
    {
    	
    }

    //打开充值中心
    public int openPayCenter(Context ctx, boolean needQuitWhenFinish)
	{
    	int nRet = 1;
    	Intent intent = new Intent(ctx, iCDPayCenterActivity.class);

//		intent.putExtra(Const.ParamType.TypePayType, content.PayType);    	
//    	intent.putExtra(Const.ParamType.TypeUserID, String.valueOf(content.UserID));
//		intent.putExtra(Const.ParamType.TypeUserName, String.valueOf(content.UserName));
//    	intent.putExtra(Const.ParamType.TypePayOrderNumber, content.CooperatorOrderSerial);
//		intent.putExtra(Const.ParamType.TypePayMerchandiseID, content.MerchandiseID);
//		intent.putExtra(Const.ParamType.TypePayMoney, content.OrderMoney);
//		intent.putExtra(Const.ParamType.TypePayMerchandise, content.MerchandiseName);
		
		if (needQuitWhenFinish) {
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, needQuitWhenFinish);
		}
		
		if (ctx instanceof Activity) {
			((Activity)ctx).startActivityForResult(intent, iChangduPay.REQUEST_CODE_CREATEORDER);
		}
		else {
			ctx.startActivity(intent);
		}
    	
    	return nRet;
	}
    
//    //创建订单
//    public int createOrder(PayRequestInfo payRequestInfo, Activity activity, boolean needQuitWhenFinish)
//	{
//    	return createOrder((RequestContent)payRequestInfo.Content, activity, needQuitWhenFinish);
//	}
    
//    //合作方银行代码列表查询接口
//    public int requestOneClickBankList(Context ctx)
//	{
//    	int sessionid = 0;
//    	BufferData bufferData = new BufferData();
//		PostStruct ps = new PostStruct(PayConst.ONECLICK_BANKLIST_ACTION, bufferData);
//    	sessionid = CNetHttpTransfer.getInstance().netGetData(Const.ONECLICK_BANKCONFIG_URL, bufferData, null, RequestHandlerManager.getInstance().getMessageHandler(), ctx);
//    	
//    	SessionManager.getInstance().addSession(sessionid, ps);
//    	
//    	return sessionid;
//	}
    
	//支付
	public int requestPay(PayRequestInfo payRequestInfo, Context ctx)
	{
		byte[] bytesBuffer = payRequestInfo.GetPostData();
		int sessionid = 0;
		
		BufferData bufferData = new BufferData();
		PostStruct ps = new PostStruct(PayConst.PAY_ACTION, bufferData);
		ps.mContext = ctx;
		
		sessionid = CNetHttpTransfer.getInstance().netPostData(PayConst.URL_COMMON_REQUEST, bytesBuffer, null, RequestHandlerManager.getInstance().getMessageHandler(), ctx);
		
		SessionManager.getInstance().addSession(sessionid, ps);
		
		return sessionid;
	}
	
	//创建订单
	public int requestCreateOrder(OrderCreateRequestInfo orderCreateRequestInfo, Context ctx)
	{
		byte[] uploadBytesBuffer = orderCreateRequestInfo.GetPostData();
		BufferData bufferData = new BufferData();
		PostStruct ps = new PostStruct(PayConst.ORDERCREATE_ACTION, bufferData);
		
		int sessionid = CNetHttpTransfer.getInstance().netPostGetData(
				PayConst.URL_COMMON_REQUEST, uploadBytesBuffer, bufferData, null, 
				RequestHandlerManager.getInstance().getMessageHandler(), ctx);
		
		SessionManager.getInstance().addSession(sessionid, ps);
		
		return sessionid;
	}
	
	//充值
	public int requestRechargeResult(RechargeRequestInfo rechargeRequestInfo, Context ctx)
	{
		byte[] uploadBytesBuffer = rechargeRequestInfo.GetPostData();
		BufferData bufferData = new BufferData();
		PostStruct ps = new PostStruct(PayConst.RECHARGE_ACTION, bufferData);
		
		int sessionid = CNetHttpTransfer.getInstance().netPostGetData(PayConst.URL_COMMON_REQUEST, 
				uploadBytesBuffer, bufferData, null, 
				RequestHandlerManager.getInstance().getMessageHandler(), ctx);
		
		SessionManager.getInstance().addSession(sessionid, ps);
		
		return sessionid;
	}
	
	//支付订单单笔查询
	public OrderCreateResponseInfo requestOrderInquiry(OrderInquiryRequestInfo orderInquiryRequestInfo, Context ctx)
	{
		return null;
	}
	
	//反馈
	public int requestFeedBack(FeedbackRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}

	//加载推送服务
	public int requestPushServer(PushServiceRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}
	
	//通用请求接口
	public int requestByPostData(BaseRequestInfo info, Context ctx) {
		byte[] uploadBytesBuffer = info.GetPostData();		
		
		BufferData bufferData = new BufferData();
		PostStruct ps = new PostStruct(info.getActionID(), bufferData);
		
		int sessionid = CNetHttpTransfer.getInstance().netPostGetData(
				PayConst.URL_COMMON_REQUEST, uploadBytesBuffer, bufferData, null, 
				RequestHandlerManager.getInstance().getMessageHandler(), ctx);
		
		SessionManager.getInstance().addSession(sessionid, ps);
		
		return sessionid;
	}
	
	//请求账单信息
	public int requestBillInfo(BillRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}
	
	//请求账单详情
	public int requestBillDetail(BillDetailRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}

	//请求收入列表
	public int requestIncomeInfo(IncomeRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}

	//请求收入详情
	public int requestIncomeInfo(IncomeDetailRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}
	
	//请求数月支出订单列表
	public int requestMonthsOutLayList(MonthsOutlayRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}
	
	//请求数月收入订单列表
	public int requestMonthsIncomeList(MonthsIncomeRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}

	//请求应用账单详情
	public int requestAppBillInfo(AppBillRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}
	
	//请求数月应用账单列表
	public int requestMonthsAppBillList(MonthsAppBillRequestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}
	
	//请求校验中国移动充值短信验证码
	public int requestCheckChinaMobileVerifyCode(CheckChinaMobileVerifyCodeReuqestInfo info, Context ctx) {
		return requestByPostData(info, ctx);
	}
	
}
