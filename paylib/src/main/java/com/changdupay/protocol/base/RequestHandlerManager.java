package com.changdupay.protocol.base;

import com.changdupay.net.netengine.Constant;
import com.changdupay.net.netengine.MessageHandler;
import com.changdupay.protocol.LoginResponseManager;

public class RequestHandlerManager {
	private static RequestHandlerManager mRequestHandlerManager= null;
	
	private static MessageHandler mRequestHandler = null;
//	private static BuildConnectListener mBuildConnectListener = null;
	private static HTTPStatusListener mHTTPStatusListener = null;
	private static ProcessCompleteListener mProcessCompleteListener = null;
	private static ProcessErrorListener mProcessErrorListener = null;
	
	public static RequestHandlerManager getInstance()
	{
		if (mRequestHandlerManager == null)
		{
			mRequestHandlerManager = new RequestHandlerManager();
		}
		
		return mRequestHandlerManager;
	}
	
	public RequestHandlerManager()
	{
		mRequestHandler = new MessageHandler();
		
//		mBuildConnectListener = new BuildConnectListener();
//		mRequestHandler.setOnBuildConnectListener(mBuildConnectListener);
		
		mHTTPStatusListener = new HTTPStatusListener();
		mRequestHandler.setOnHTTPStatusListener(mHTTPStatusListener);
		
		mProcessCompleteListener = new ProcessCompleteListener();
		mRequestHandler.setOnProcessCompleteListener(mProcessCompleteListener);
		
		mProcessErrorListener = new ProcessErrorListener();
		mRequestHandler.setOnProcessErrorListener(mProcessErrorListener);
	}
	
	public MessageHandler getMessageHandler()
	{
		return mRequestHandler;
	}
	
//	private class BuildConnectListener implements MessageHandler.OnBuildConnectListener
//	{
//		public void onBuildConnect(int sessionId,int errorCode)
//		{
//			if (Constant.CORRECT != errorCode)
//			{
//				PostStruct ps = SessionManager.getInstance().getSessionData(sessionId);
//				ps.errorCode = errorCode;
//				LoginResponseManager.getInstance().processResponse(ps);
//			}
//		}
//	}
	
	private class HTTPStatusListener implements MessageHandler.OnHTTPStatusListener
	{
		public void onHTTPStatus(int sessionId, int statusCode)
		{
			if (200 != statusCode)
			{
				PostStruct ps = SessionManager.getInstance().getSessionData(sessionId);
				ps.errorCode = Constant.CONNECT_ERROR;
				LoginResponseManager.getInstance().processResponse(ps);
			}
		}
	}
	
	private class ProcessCompleteListener implements MessageHandler.OnProcessCompleteListener
	{
		public void onProcessComplete(int sessionId,int arg)
		{
			PostStruct ps = SessionManager.getInstance().getSessionData(sessionId);
			ps.errorCode = Constant.CORRECT;
			switch (ps.mActionID)
			{
			case PayConst.PAY_ACTION: //支付成功	
			case PayConst.DYNAMICKEY_ACTION://获取动态秘钥
			case PayConst.LOGIN_ACTION: //登陆成功
			case PayConst.TOKEN_LOGIN_ACTION://令牌登陆成功
			case PayConst.MOBILEPHONECHECK_ACTION: //验证手机号是否可用
			case PayConst.EMAILCHECK_ACTION: //验证邮箱是否可用
			case PayConst.PASSWORDCHECK_ACTION: //支付成功
			case PayConst.VERIFYCODE_ACTION: //支付成功
			case PayConst.MOBILEPHONEREGISTER_ACTION: //手机成功
			case PayConst.EMAILREGISTER_ACTION: //邮箱成功
			case PayConst.SMALLPAY_ACTION: //小额支付免密设置
			case PayConst.ORDERCREATE_ACTION: //创建订单
			case PayConst.RECHARGE_ACTION: //充值
			case PayConst.FEEDBACK_ACTION://反馈
			case PayConst.PUSH_SERVICE_ACTION://加载推送服务
			case PayConst.GETBILL_ACTION://账单列表
			case PayConst.GETBILL_DETAIL_ACTION://账单详情
			case PayConst.ONECLICK_BANKLIST_ACTION://银行列表
			case PayConst.PREFERENTIAL_ICON_ACTION://活动图标
			case PayConst.GET_ACCOUNTINFO_ACTION://个人信息				
			case PayConst.GET_CHANGDUREADER_USERINFO_ACTION://相关用户信息：
			default:
				LoginResponseManager.getInstance().processResponse(ps);
				break;
			}
		}
	}
	
	private class ProcessErrorListener implements MessageHandler.OnProcessErrorListener
	{
		public void onProcessError(int sessionId, int errorCode, Exception e)
		{
			PostStruct ps = SessionManager.getInstance().getSessionData(sessionId);
			ps.errorCode = errorCode;
//			LoginResponseManager.getInstance().processResponse(ps);
		}
	}
}
