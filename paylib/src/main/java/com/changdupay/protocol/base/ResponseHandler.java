package com.changdupay.protocol.base;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ResponseHandler extends Handler{
	public ResponseHandler(Looper looper){
	    super(looper);
	}
	
	public ResponseHandler(Looper looper, Callback callback) {
		 super(looper,callback);
    }
	
	public ResponseHandler() {
		 super();
   }
	
	/**
	 * 处理接收到的消息
	 */
	@Override
	public void handleMessage(Message msg) { 
		switch (msg.what) {
		case PayConst.GET_CHANGDUREADER_USERINFO_ACTION:
			if (mOnGetChangduReaderUserInfoListener != null) {
				mOnGetChangduReaderUserInfoListener.OnResult(msg.obj);
			}
			break;
		
		case PayConst.DYNAMICKEY_ACTION:
			if (mOnGetDynamicKeyListener != null) {
				mOnGetDynamicKeyListener.onGetDynamicKey(msg.obj);
			}
			break;
			
		case PayConst.PAY_ACTION:
			if (mOnPayOrderListener != null) {
				mOnPayOrderListener.onPay(msg.obj);
			}
			break;
		
		case PayConst.LOGIN_ACTION: 
			if (mOnLoginListener != null){
				mOnLoginListener.onLogin(msg.obj);//
			}
			break;
		
		case PayConst.TOKEN_LOGIN_ACTION: 
			if (mOnTokenLoginListener != null){
				mOnTokenLoginListener.onTokenLogin(msg.obj);//
			}
			break;
			
		case PayConst.GET_MONTHS_OUTLAY_ACTION:
		case PayConst.GET_MONTHS_INCOME_ACTION:
		case PayConst.GET_INCOME_ACTION:
		case PayConst.GETBILL_ACTION:
			if (mOnGetBillListener != null) {
				mOnGetBillListener.onResult(msg.obj);
			}
			break;
			
		case PayConst.GET_INCOME_DETAIL_ACTION:
		case PayConst.GETBILL_DETAIL_ACTION:
			if (mOnGetBillDetailListener != null) {
				mOnGetBillDetailListener.onResult(msg.obj);
			}
			break;
		
		case PayConst.FEEDBACK_ACTION:
			if (mOnFeedBackListener != null) {
				mOnFeedBackListener.onResult(msg.obj);
			}
			break;			
		
		case PayConst.PUSH_SERVICE_ACTION:
			if (mOnPushServiceListener != null) {
				mOnPushServiceListener.onResult(msg.obj);
			}
			break;
	
		case PayConst.MOBILEPHONECHECK_ACTION: 
			if (mOnMobilePhoneCheckListener != null){
				mOnMobilePhoneCheckListener.onMobilePhoneCheck(msg.obj);//
			}
			break;
			
		case PayConst.EMAILCHECK_ACTION: 
			if (mOnEmailCheckListener != null){
				mOnEmailCheckListener.onEmailCheck(msg.obj);//
			}
			break;
			
		case PayConst.PASSWORDCHECK_ACTION: 
			if (mOnPasswordCheckListener != null){
				mOnPasswordCheckListener.onPasswordCheck(msg.obj);//
			}
			break;
			
		case PayConst.VERIFYCODE_ACTION: 
			if (mOnGetVerifyCodeListener != null){
				mOnGetVerifyCodeListener.onGetVerifyCodeCheck(msg.obj);//
			}
			break;
			
		case PayConst.MOBILEPHONEREGISTER_ACTION: 
			if (mOnPhoneRegisterListener != null){
				mOnPhoneRegisterListener.onPhoneRegister(msg.obj);//
			}
			break;
			
		case PayConst.EMAILREGISTER_ACTION: 
			if (mOnEmailRegisterListener != null){
				mOnEmailRegisterListener.onEmailRegister(msg.obj);//
			}
			break;
			
		case PayConst.SMALLPAY_ACTION: 
			if (mOnSmallPayListener != null){
				mOnSmallPayListener.OnSmallPay(msg.obj);//
			}
			break;
			
		case PayConst.ORDERCREATE_ACTION: 
			if (mOnCreateOrderListener != null){
				mOnCreateOrderListener.OnCreateOrder(msg.obj);//
			}
			break;
			
		case PayConst.RECHARGE_ACTION: 
			if (mOnRechargeListener != null){
				mOnRechargeListener.OnRecharge(msg.obj);//
			}
			break;
			
		case PayConst.ONECLICK_BANKLIST_ACTION: 
			if (mOnGetBankListListener != null){
				mOnGetBankListListener.OnGetBankList(msg.obj);//
			}
			break;
			
		case PayConst.PREFERENTIAL_ICON_ACTION: 
			if (mOnDownloadIconListener != null){
				mOnDownloadIconListener.OnDownloadIcon(msg.obj);//
			}
			break;
			
		case PayConst.GET_ACCOUNTINFO_ACTION: 
			if (mOnGetAccountInfoListener != null){
				mOnGetAccountInfoListener.OnGetAccountInfo(msg.obj);//
			}
			break;

		default:
			if (mOnResponseListener != null) {
				mOnResponseListener.OnResult(msg.obj);
			}
			break;
		}
	}
	
	// 定义获取动态秘钥事件
	public interface OnGetDynamicKeyListener {
		void onGetDynamicKey(Object info);
	}
	private OnGetDynamicKeyListener mOnGetDynamicKeyListener;
	public void setOnGetDynamicKeyListener(OnGetDynamicKeyListener listener) {
		mOnGetDynamicKeyListener = listener;
	}

	// 定义支付事件
	public interface OnPayOrderListener {
		void onPay(Object info);
	}
	private OnPayOrderListener mOnPayOrderListener;
	public void setOnPayOrderListener(OnPayOrderListener listener) {
		mOnPayOrderListener = listener;
	}	
	
	// 定义登录事件
	private OnLoginListener mOnLoginListener;
	public interface OnLoginListener {
		void onLogin(Object loginInfo);
	}
	public void setLoginListener(OnLoginListener listener) {
		mOnLoginListener = listener;
	}

	// 定义令牌登录事件
	private OnTokenLoginListener mOnTokenLoginListener;
	public interface OnTokenLoginListener {
		void onTokenLogin(Object tokenLoginInfo);
	}
	public void setOnTokenLoginListener(OnTokenLoginListener listener) {
		mOnTokenLoginListener = listener;
	}
	
	// 定义获取账单事件
	public interface OnGetBillListener {
		void onResult(Object info);
	}
	private OnGetBillListener mOnGetBillListener;
	public void setOnGetBillListener(OnGetBillListener listener) {
		mOnGetBillListener = listener;
	}	

	// 定义获取订单详情事件
	public interface OnGetBillDetailListener {
		void onResult(Object info);
	}
	private OnGetBillDetailListener mOnGetBillDetailListener;
	public void setOnPayOrderListener(OnGetBillDetailListener listener) {
		mOnGetBillDetailListener = listener;
	}	

	// 定义反馈回调
	public interface OnFeedBackListener {
		void onResult(Object info);
	}
	private OnFeedBackListener mOnFeedBackListener;
	public void setOnFeedBackListener(OnFeedBackListener listener) {
		mOnFeedBackListener = listener;
	}	

	// 定义推送服务回调
	public interface OnPushServiceListener {
		void onResult(Object info);
	}
	private OnPushServiceListener mOnPushServiceListener;
	public void setOnPushServiceListener(OnPushServiceListener listener) {
		mOnPushServiceListener = listener;
	}
	
	// 定义手机号是否可用事件
	private OnMobilePhoneCheckListener mOnMobilePhoneCheckListener;

	public interface OnMobilePhoneCheckListener {
		void onMobilePhoneCheck(Object mobilePhoneInfo);
	}

	public void setMobilePhoneCheckListener(OnMobilePhoneCheckListener listener) {
		mOnMobilePhoneCheckListener = listener;
	}
	
	//验证邮箱是否可用
	private OnEmailCheckListener mOnEmailCheckListener;

	public interface OnEmailCheckListener {
		void onEmailCheck(Object emailCheckInfo);
	}

	public void setEmailCheckListener(OnEmailCheckListener listener) {
		mOnEmailCheckListener = listener;
	}
	
	//验证密码是否可用
	private OnPasswordCheckListener mOnPasswordCheckListener;

	public interface OnPasswordCheckListener {
		void onPasswordCheck(Object passwordCheck);
	}

	public void setPasswordCheckListener(OnPasswordCheckListener listener) {
		mOnPasswordCheckListener = listener;
	}
	
	//获取手机验证码
	private OnGetVerifyCodeListener mOnGetVerifyCodeListener;

	public interface OnGetVerifyCodeListener {
		void onGetVerifyCodeCheck(Object loginInfo);
	}

	public void setGetVerifyCodeListener(OnGetVerifyCodeListener listener) {
		mOnGetVerifyCodeListener = listener;
	}
	
	// 定义手机注册事件
	private OnPhoneRegisterListener mOnPhoneRegisterListener;


	public interface OnPhoneRegisterListener {
		void onPhoneRegister(Object phoneRegisterInfo);
	}

	public void setPhoneRegisterListener(OnPhoneRegisterListener listener) {
		mOnPhoneRegisterListener = listener;
	}
	
	// 定义邮箱注册事件
	private OnEmailRegisterListener mOnEmailRegisterListener;


	public interface OnEmailRegisterListener {
		void onEmailRegister(Object emailRegisterInfo);
	}

	public void setEmailRegisterListener(OnEmailRegisterListener listener) {
		mOnEmailRegisterListener = listener;
	}
	
	//小额支付免密设置
	private OnSmallPayListener mOnSmallPayListener;


	public interface OnSmallPayListener {
		void OnSmallPay(Object smallPayInfo);
	}

	public void setSmallPayRegisterListener(OnSmallPayListener listener) {
		mOnSmallPayListener = listener;
	}
	
	//创建订单
	private OnCreateOrderListener mOnCreateOrderListener;


	public interface OnCreateOrderListener {
		void OnCreateOrder(Object createOrderInfo);
	}

	public void setCreateOrderRegisterListener(OnCreateOrderListener listener) {
		mOnCreateOrderListener = listener;
	}
	
	//充值
	private OnRechargeListener mOnRechargeListener;


	public interface OnRechargeListener {
		void OnRecharge(Object rechargeInfo);
	}

	public void setRechargeRegisterListener(OnRechargeListener listener) {
		mOnRechargeListener = listener;
	}
	
	//获取银行列表
	private OnGetBankListListener mOnGetBankListListener;

	public interface OnGetBankListListener {
		void OnGetBankList(Object bankList);
	}

	public void setOnGetBankListListener(OnGetBankListListener listener) {
		mOnGetBankListListener = listener;
	}

	//通用回调接口
	private OnResponseListener mOnResponseListener;
	public interface OnResponseListener {
		void OnResult(Object objResult);
	}
	public void setOnResponseListener(OnResponseListener listener) {
		mOnResponseListener = listener;
	}
	
	//获取优惠活动图标
	private OnDownloadIconListener mOnDownloadIconListener;

	public interface OnDownloadIconListener {
		void OnDownloadIcon(Object icon);
	}

	public void setOnDownloadIconListener(OnDownloadIconListener listener) {
		mOnDownloadIconListener = listener;
	}

	//加载帐户信息
	private OnGetAccountInfoListener mOnGetAccountInfoListener;

	public interface OnGetAccountInfoListener {
		void OnGetAccountInfo(Object info);
	}

	public void setOnGetAccountInfoListener(OnGetAccountInfoListener listener) {
		mOnGetAccountInfoListener = listener;
	}
	

	//加载帐户信息
	private OnGetChangduReaderUserInfoListener mOnGetChangduReaderUserInfoListener;

	public interface OnGetChangduReaderUserInfoListener {
		void OnResult(Object info);
	}

	public void setOnGetChangduReaderInfoListener(OnGetChangduReaderUserInfoListener listener) {
		mOnGetChangduReaderUserInfoListener = listener;
	}
}
