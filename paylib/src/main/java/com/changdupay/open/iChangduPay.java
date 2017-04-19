package com.changdupay.open;

import android.content.Context;

import com.changdupay.protocol.pay.PayRequestManager;
import com.changdupay.util.Const;
import com.changdupay.util.ContextUtil;

public class iChangduPay {
	public interface IPayCallback {
		//resultCode为0代表成功
		void onPayCallback(int resultCode, String errorMsg);
	}
	
	public static int REQUEST_CODE_CREATEORDER = Const.RequestCode.CreateOrder;
	public static int REQUEST_CODE_RUNCDPAY = Const.RequestCode.RunCDPay;
	public static String PACKAGE_NAME	= "com.changdupay.android.app";
	
	private static IPayCallback mCallback = null;
	private static Context mContext = null;	
//	private static RequestContent mRequestContent = null;
	private static int mResultCode = ResultCode.Failed;
	private static String mErrorMsg = "";


//	public static boolean pay(Context ctx, RequestContent content, IPayCallback callback) {
//		if (ctx == null || content == null || callback == null) {
//			return false;
//		}		
//		ContextUtil.setContext(ctx);
//		mCallback = callback;		
//		mContext = ctx;
//		setResult(ResultCode.Canceled, "user canceled");
//
//		requestCreateOrder(content);		
//		return true;
//	}
	
	public static boolean openPayCenter(Context ctx) {
		if (ctx == null) {
			return false;
		}		
		ContextUtil.setContext(ctx);	
		mContext = ctx;

//		mRequestContent = content;
//		SkinChangeManager.getInstance().setColorMatrix(content.ColorMatrix);
		
		PayRequestManager.getInstance().openPayCenter(ContextUtil.getContext(), false);
	
		return true;
	}
		
//	private static void requestCreateOrder(RequestContent content) {
//		mRequestContent = content;
//		
//		//特别修改
//		PayConst.APPID = content.AppID;
//		PayConst.MERCHANTID = content.MerchantID;
//		PayConst.MERCHANDISEID = content.MerchandiseID;
//		PayConst.VER = content.Ver;
//		PayConst.MERCHANDISENAME = content.MerchandiseName;
//		
//		SkinChangeManager.getInstance().setColorMatrix(content.ColorMatrix);
//		
//		PayRequestManager.getInstance().openPayCenter(mRequestContent, ContextUtil.getContext(), true);
//		
//		//BaseActivity.showWaitCursor(null, mContext.getString(MResource.getIdByName(mContext, "string", "ipay_wait_for_initializing")));
//		//LoginRequestManager.getInstance().requestGetDynamicKey(mContext, merchantID);
//	}

	public static IPayCallback getCallback() {
		return mCallback;
	}
	
	public static int getResultCode() {
		return mResultCode;
	}
	
	public static String getErrorMsg() {
		return mErrorMsg;
	}
	
	public static void setResult(int resultCode, String errMsg) {
		mResultCode = resultCode;
		mErrorMsg = errMsg;
	}
	
	public interface ResultCode {
		int Failed = -1;
		int Success = 0;
		int Canceled = 1;
		int LoginFailed = 2;
	}
	
}
