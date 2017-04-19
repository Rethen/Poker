package com.changdupay.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.changdupay.android.lib.R;
import com.changdupay.open.iChangduPay;
import com.changdupay.open.iChangduPay.ResultCode;
import com.changdupay.protocol.LoginResponseManager;
import com.changdupay.protocol.ProtocolData.BaseProtocalData;
import com.changdupay.protocol.ProtocolData.PayEntity;
import com.changdupay.protocol.base.ResponseHandler.OnCreateOrderListener;
import com.changdupay.protocol.base.ResponseHandler.OnResponseListener;
import com.changdupay.protocol.pay.CheckChinaMobileVerifyCodeReuqestInfo;
import com.changdupay.protocol.pay.CheckChinaMobileVerifyCodeReuqestInfo.CheckChinaMobileVerifyCodeContent;
import com.changdupay.protocol.pay.OrderCreateRequestInfo;
import com.changdupay.protocol.pay.OrderCreateRequestInfo.OrderCreateContent;
import com.changdupay.protocol.pay.PayRequestManager;
import com.changdupay.protocol.pay.RechargeRequestInfo;
import com.changdupay.protocol.pay.RechargeRequestInfo.RechargeRequestContent;
import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.SmsReceiver;
import com.changdupay.util.ToastHelper;

public class iCDPayChinaMobileSmsVerifyCodeActivity extends BaseActivity implements OnClickListener, TextWatcher{
	Button mGetVerifyCodeBtn;
	EditText mGetVerifyCodeEditText;
	Button mOkButton;
	
	Channel mPayChannel;
	String mPhoneNumberString;
	double mRechargeMoney;
	PayEntity mPayEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_chinamobile_sms_verifycode"));

		initView();
				
		registerSmsReceiver();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
		initEvent();
		
		LoginResponseManager.getInstance().getResponseHandler().setCreateOrderRegisterListener(mOnCreateOrderListener);	
		LoginResponseManager.getInstance().getResponseHandler().setOnResponseListener(mOnResponseListener);
	}
	
	private void getData() {
		Intent intent = getIntent();
		mPayChannel = (Channel)intent.getSerializableExtra(Const.ParamType.TypePayChannelItem);
		mPhoneNumberString = intent.getStringExtra(Const.ParamType.TypePayPhoneNumber);
		mRechargeMoney = intent.getDoubleExtra(Const.ParamType.TypePayMoney, 0.0);
	}
	
	private void initView() {
		showBackBtn();
		setTitle(getString(MResource.getIdByName(getApplication(), "string", "ipay_sms_pay_title")));
		
		mGetVerifyCodeBtn = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "get_verify_code_btn"));
		mGetVerifyCodeEditText = (EditText)findViewById(MResource.getIdByName(getApplication(), "id", "verify_code_edittext"));
		mOkButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "ok"));
	}
	
	private void initEvent() {
		mGetVerifyCodeBtn.setOnClickListener(this);
		mGetVerifyCodeEditText.addTextChangedListener(this);
		mOkButton.setOnClickListener(this);
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		if (arg0.length() > 0) {
			mOkButton.setEnabled(true);
		}
		else {
			mOkButton.setEnabled(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == mGetVerifyCodeBtn) {
			doRequest();
		}
		else if (arg0 == mOkButton) {
			checkVerifyCode();
		}
	}
	
	private void doRequest()
	{		
		showWaitCursor(null, getString(MResource.getIdByName(getApplication(), "string", "ipay_wait_for_request_data")));
		
		int nPayType = mPayChannel.PayType;
		int nPayID = mPayChannel.PayId;		
		if (UserInfo.getInstance().RechargeFlag)
		{
			RechargeRequestInfo rechargeRequestInfo = new RechargeRequestInfo();
			((RechargeRequestContent)rechargeRequestInfo.Content).PayId = nPayID;
			((RechargeRequestContent)rechargeRequestInfo.Content).PayType = nPayType;
			((RechargeRequestContent)rechargeRequestInfo.Content).Amount = mRechargeMoney;
			((RechargeRequestContent)rechargeRequestInfo.Content).PhoneNumber = mPhoneNumberString;
			
			PayRequestManager.getInstance().requestRechargeResult(rechargeRequestInfo, this);
		}
		else
		{
			iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
			
			OrderCreateRequestInfo orderCreateRequestInfo = new OrderCreateRequestInfo();
			((OrderCreateContent)orderCreateRequestInfo.Content).PayId = nPayID;
			((OrderCreateContent)orderCreateRequestInfo.Content).PayType = nPayType;
			((OrderCreateContent)orderCreateRequestInfo.Content).MerchandiseID = payOrderInfo.MerchandiseID;
			((OrderCreateContent)orderCreateRequestInfo.Content).MerchandiseName = payOrderInfo.MerchandiseName;
			((OrderCreateContent)orderCreateRequestInfo.Content).CooperatorOrderSerial = payOrderInfo.CooperatorOrderSerial;
			((OrderCreateContent)orderCreateRequestInfo.Content).OrderMoney = String.valueOf(mRechargeMoney);
			((OrderCreateContent)orderCreateRequestInfo.Content).PhoneNumber = mPhoneNumberString;
			((OrderCreateContent)orderCreateRequestInfo.Content).UserName = payOrderInfo.UserName;
			((OrderCreateContent)orderCreateRequestInfo.Content).UserID = payOrderInfo.UserID;
			((OrderCreateContent)orderCreateRequestInfo.Content).ShopItemId = getShopItemIdByMoney(String.valueOf(mRechargeMoney));
			
			PayRequestManager.getInstance().requestCreateOrder(orderCreateRequestInfo, this);
		}
	}
	protected String getShopItemIdByMoney(String payMoney)
	{
		Channel payChannel = mPayChannel;
		if(payChannel == null){
			Log.e("getIndexByMoney", "payChannel null");
			return "";
		}
		String shopItemId = "";
		for (int i = 0; i < payChannel.AmountLimits.size(); i++)
		{
			try{
				if (Double.parseDouble(payMoney) == payChannel.AmountLimits.get(i).Value)
				{
	
					shopItemId = payChannel.AmountLimits.get(i).ShopItemId;
					break;
				}
			}catch(NumberFormatException e){
				e.printStackTrace();				
			}
		}
		
		return shopItemId;
	}
	
	private OnCreateOrderListener mOnCreateOrderListener = new OnCreateOrderListener()
	{

		@Override
		public void OnCreateOrder(Object createOrderInfo) {
			// TODO Auto-generated method stub
			hideWaitCursor();

			if (createOrderInfo instanceof PayEntity) 
			{
				PayEntity entity = (PayEntity)createOrderInfo;
				if (true == entity.result) {
					//成功
					ToastHelper.shortDefaultToast(MResource.getIdByName(getApplication(), "string", "ipay_verify_code_send_success"));
					
					mGetVerifyCodeEditText.setEnabled(true);
					mPayEntity = entity;
					return;
				}
				else {
					ToastHelper.shortDefaultToast(entity.errorMsg);
					iChangduPay.setResult(ResultCode.Failed, entity.errorMsg);
				}
			}
			else if (createOrderInfo instanceof BaseProtocalData)
			{
				BaseProtocalData entity = (BaseProtocalData)createOrderInfo;
				ToastHelper.shortDefaultToast(entity.errorMsg);
				iChangduPay.setResult(ResultCode.Failed, entity.errorMsg);
			}
			else {
				showResponseInfo((Integer)createOrderInfo);
			}
			
			
		}
		
	};
	
	private OnResponseListener mOnResponseListener = new OnResponseListener() {
		
		@Override
		public void OnResult(Object objResult) {
			// TODO Auto-generated method stub
			if (objResult instanceof BaseProtocalData) {
				BaseProtocalData entity = (BaseProtocalData)objResult;
				if (entity.result) {
					gotoFinishActivity();
				}
				else {
					ToastHelper.shortDefaultToast(entity.errorMsg);
					iChangduPay.setResult(ResultCode.Failed, entity.errorMsg);
				}
			}
			else {
				showResponseInfo((Integer)objResult);
			}
		}
	};
	
	private void checkVerifyCode() {
		if (mPayEntity == null || TextUtils.isEmpty(mGetVerifyCodeEditText.getText())) {
			return;
		}
		
		iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
		
		CheckChinaMobileVerifyCodeReuqestInfo info = new CheckChinaMobileVerifyCodeReuqestInfo();
		((CheckChinaMobileVerifyCodeContent)info.Content).PayId = mPayChannel.PayId;
		((CheckChinaMobileVerifyCodeContent)info.Content).UserName = payOrderInfo.UserName;
		((CheckChinaMobileVerifyCodeContent)info.Content).UserID = payOrderInfo.UserID;
		((CheckChinaMobileVerifyCodeContent)info.Content).OrderId = mPayEntity.Parameter;
		//((CheckChinaMobileVerifyCodeContent)info.Content).VerifyUrl = mPayEntity.VerifyUrl;
		((CheckChinaMobileVerifyCodeContent)info.Content).VerifyCode = mGetVerifyCodeEditText.getText().toString();
		//((CheckChinaMobileVerifyCodeContent)info.Content).OrderSerial = mPayEntity.OrderSerial;
		//((CheckChinaMobileVerifyCodeContent)info.Content).StartDateTime = mPayEntity.StartDateTime;
		
		PayRequestManager.getInstance().requestCheckChinaMobileVerifyCode(info, iCDPayChinaMobileSmsVerifyCodeActivity.this);
		
	}
	
	private void gotoFinishActivity()
	{
		Intent intent = new Intent(this, PayResultActivity.class);
		intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
		startActivityForResult(intent, 1000);
	}
	
	private void registerSmsReceiver() {
		SmsReceiver.setmOnReceiveSmsListener(onReceiveSmsListener);
	}
	
	private SmsReceiver.OnReceiveSmsListener onReceiveSmsListener = new SmsReceiver.OnReceiveSmsListener() {		
		@Override
		public void onReceive(Object info) {
			// TODO Auto-generated method stub
			if (info instanceof String) {
				showSmsContentDialog((String)info);
			}
		}
	};
	
	private void showSmsContentDialog(final String strContent) {
		try {
			new AlertDialog.Builder(this)
			.setTitle(R.string.ipay_sms_content)
			.setMessage(strContent)
			.setNegativeButton(R.string.ipay_ok, new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String strPreFix = getString(MResource.getIdByName(getApplication(), "string", "ipay_chinamobile_smscode_prefix"));
					int nStartIndex = strPreFix.length();
					if (strContent.contains(strPreFix)) {
						mGetVerifyCodeEditText.setText(strContent.substring(nStartIndex, nStartIndex + 3));
					}
				}
			})
			.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
