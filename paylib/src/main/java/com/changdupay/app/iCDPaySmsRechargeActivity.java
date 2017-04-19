package com.changdupay.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.changdupay.protocol.LoginResponseManager;
import com.changdupay.protocol.ProtocolData.BaseProtocalData;
import com.changdupay.protocol.ProtocolData.PayEntity;
import com.changdupay.protocol.base.ResponseHandler.OnCreateOrderListener;
import com.changdupay.protocol.base.ResponseHandler.OnRechargeListener;
import com.changdupay.protocol.pay.OrderCreateRequestInfo;
import com.changdupay.protocol.pay.OrderCreateRequestInfo.OrderCreateContent;
import com.changdupay.protocol.pay.PayRequestManager;
import com.changdupay.protocol.pay.RechargeRequestInfo;
import com.changdupay.protocol.pay.RechargeRequestInfo.RechargeRequestContent;
import com.changdupay.util.Const;
import com.changdupay.util.Const.SimTypeName;
import com.changdupay.util.Const.StepLevel;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.AmountLimit;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;

public class iCDPaySmsRechargeActivity extends iCDPayChooseMoneyBaseActivity implements TextWatcher, OnClickListener{	
	int mStepLevel = Const.StepLevel.Step1; //当前阶段
	
	private EditText mPhoneEditText;
	private Button	mNextButton;
	private Button mClearButton;
	private GridView mGridView;
	
	private String mPhoneNumberString = "";
//	private String mOperatorsName = SimTypeName.simunknow;//移动运营商
	private String mPayName = SimTypeName.simunknow;
	private int mPayType = -1;
	private double mRechargeMoney = 0.0;
	private int mLastSelectedIndex = 0;
	
	private Boolean mJumpUnicomShop = false;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		mStepLevel = getIntent().getIntExtra(Const.ParamType.TypeStepLevel, Const.StepLevel.Step1);	
		mJumpUnicomShop = getIntent().getBooleanExtra(Const.ParamType.TypeJumpUnicomShop, false);
		
		super.onCreate(savedInstanceState);
		
		switch (mStepLevel) {
		case Const.StepLevel.Step1:
			setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_sms_recharge_step1"));
			mPhoneNumberString = Utils.getStringValue(Const.ConfigKeys.LastPhoneNumber);
			break;
		case Const.StepLevel.Step2:
			setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_sms_recharge_step2"));
			mPhoneNumberString = getIntent().getStringExtra(Const.ParamType.TypeMobilePhone);
			if(TextUtils.isEmpty(mPhoneNumberString)){
				mPhoneNumberString = "";
			}
			Utils.setStringValue(Const.ConfigKeys.LastPhoneNumber, mPhoneNumberString);
			break;
		default:
			break;
		}
		
		if ((mPhoneNumberString != null && mPhoneNumberString.length() > 2)) {
//			 getOperatorByPhoneNumber(mPhoneNumberString);
			getPayTypeByNumber(mPhoneNumberString);			
		}
		
		initView();
		updateButtonByPhoneNum();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mPayChannel = getPayChannel();
		initEvent();
		
		if (mStepLevel == Const.StepLevel.Step2) {
			LoginResponseManager.getInstance().getResponseHandler().setRechargeRegisterListener(mOnRechargeListener);
			LoginResponseManager.getInstance().getResponseHandler().setCreateOrderRegisterListener(mOnCreateOrderListener);
			if(mPhoneEditText!=null){
				try{
					mPhoneEditText.setFocusable(false);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(mPhoneEditText.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initView() {
		showBackBtn();
		mNextButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "next_btn"));

		if(mJumpUnicomShop){
			setTitle(getString(MResource.getIdByName(getApplication(), "string", "ipay_unicom_shop")));
		}
		else{			
			setTitle(getString(MResource.getIdByName(getApplication(), "string", "ipay_sms_pay_title")));
		}

		
		mPhoneEditText = (EditText)findViewById(MResource.getIdByName(getApplication(), "id", "phone_number_edittext"));
		
		if (StepLevel.Step2 == mStepLevel) {
			mGridView = (GridView)findViewById(MResource.getIdByName(getApplication(), "id", "sms_recharge_gridview"));
			mClearButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "clear_btn"));
			initAdapter();
//			if (mPayChooseMoneyAdapter.getCount() > 0) {
//				mPayChooseMoneyAdapter.setInitialSelectedItem(mLastSelectedIndex);
//			}
		}
		
		if (!TextUtils.isEmpty(mPhoneNumberString)) {
			mPhoneEditText.setText(mPhoneNumberString);
			mPhoneEditText.setSelection(mPhoneNumberString.length());
		}		
	}

	private void initEvent() {
		if (StepLevel.Step2 == mStepLevel) {
			mClearButton.setOnClickListener(this);
			mGridView.setOnItemClickListener(new SmsRechargeOnItemClickListener());
		}
		mNextButton.setOnClickListener(this);
		mPhoneEditText.addTextChangedListener(this);
	}
	
	private void initAdapter()
	{
		mPayChooseMoneyAdapter = new iCDPayChooseMoneyAdapter(this, this, getApplication());
		mGridView.setAdapter(mPayChooseMoneyAdapter);
		setInitialSelectedItem();
		mLastSelectedIndex = getInitialSelectedIndex();
	}
	
	private void updateGridView() {
		mLastSelectedIndex = getInitialSelectedIndex();
		if (mPayChooseMoneyAdapter.getCount() > 0) {
			mPayChooseMoneyAdapter.setOtherItemsUnSelected(0);
			mPayChooseMoneyAdapter.setInitialSelectedItem(mLastSelectedIndex);
		}
		mPayChooseMoneyAdapter.notifyDataSetChanged();
		mPayChooseMoneyAdapter.setItemSelected(mLastSelectedIndex);
	}
	
	private void updateButtonByPhoneNum()
	{
		if (mPhoneNumberString.length() == 11)
			//&& !TextUtils.equals(getOperatorByPhoneNumber(mPhoneNumberString), Const.SimTypeName.simunknow)
		{
			mNextButton.setEnabled(true);
		}
		else
		{
			mNextButton.setEnabled(false);
		}
	}
	
	@Override
	public void afterTextChanged(Editable arg0) {
		mPhoneEditText.requestFocus();
		
		mPhoneNumberString = mPhoneEditText.getText().toString().trim();
		if (mPhoneNumberString.length() < 11) {
			mNextButton.setEnabled(false);
		}
		
//		String operator = Const.SimTypeName.simunknow;
		if (mPhoneNumberString.length() > 2)
		{
//			getOperatorByPhoneNumber(mPhoneNumberString);
			getPayTypeByNumber(mPhoneNumberString);
		}
		
		if (TextUtils.equals(mPayName, Const.SimTypeName.simunknow))
		{
			if (mPhoneNumberString.length() == 3)
			{
				ToastHelper.shortDefaultToast(MResource.getIdByName(getApplication(), "string", "ipay_phone_invalid"));
			}
			mNextButton.setEnabled(false);
			return;
		}

		if (mPhoneNumberString.length() == 11) {
			mNextButton.setEnabled(true);
		}

		if (mStepLevel == Const.StepLevel.Step1)
		{
//			mOperatorsName = operator;
		}
		else if (mStepLevel == Const.StepLevel.Step2) {
//			if (!mOperatorsName.equals(operator)) 
//			{
//				mOperatorsName = operator;
				initAdapter();
				mPayChannel = getPayChannel();
				
				updateGridView();
//			}
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
		if (arg0 == mNextButton) {
			if (StepLevel.Step1 == mStepLevel) {				
				if (TextUtils.isEmpty(mPayName) || mPayName.equals(Const.SimTypeName.simunknow)) {
					ToastHelper.shortDefaultToast(MResource.getIdByName(getApplication(), "string", "ipay_phone_invalid"));
					return;
				}
				
				startStep2();
			}
			else if (StepLevel.Step2 == mStepLevel) {
				if (TextUtils.isEmpty(mPhoneNumberString))
				{
					ToastHelper.shortDefaultToast(getString(MResource.getIdByName(getApplication(), "string", "ipay_phone_empty")));
					return;
				}
				Utils.setStringValue(Const.ConfigKeys.LastPhoneNumber, mPhoneNumberString);
				
				if (mLastSelectedIndex < 0)
				{
					ToastHelper.shortDefaultToast(getString(MResource.getIdByName(getApplication(), "string", "ipay_channel_info_error")));				
				
					return;
				}
				
				mPayChannel = getPayChannel();
				mRechargeMoney = ((AmountLimit)mPayChooseMoneyAdapter.getItem(mLastSelectedIndex)).Value;
				if (mPayChannel != null) {
					if(mJumpUnicomShop){
						doRequest(mPayChannel.PayType, mPayChannel.PayId);
					}
					else{
						if (mPayName.equals(Const.SimTypeName.simmobile)) {
							//移动充值
							doChinaMobilePay();
						}
						else if (mPayName.equals(Const.SimTypeName.simtelcom)) {
							doTeleComPay();
						}
						else if (mPayName.equals(Const.SimTypeName.simunicom)) {
							doUniComPay();
						}
					}
				}
				else {
					ToastHelper.shortDefaultToast(getString(MResource.getIdByName(getApplication(), "string", "ipay_channel_info_error")));					
				}
			}
		}
		else if (arg0 == mClearButton) {
			//finish();
			try{
				mPhoneEditText.setText("");
				mPhoneEditText.setFocusable(true);
				mPhoneEditText.setFocusableInTouchMode(true);
				mPhoneEditText.setSelected(true);
				mPhoneEditText.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(mPhoneEditText, InputMethodManager.SHOW_FORCED);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//移动充值
	private void doChinaMobilePay() {
//		if (Const.ChannelType.mobilephone.PayType == mPayChannel.PayType
//				&& Const.ChannelType.mobilephone.PayId == mPayChannel.PayId)
		if(Const.SMSPayViewType.toGetSmsVerifyCodeActivity == mPayChannel.ViewType)
		{
			Intent intent = new Intent(this, iCDPayChinaMobileSmsVerifyCodeActivity.class);
			intent.putExtra(Const.ParamType.TypePayChannelItem, mPayChannel);
			intent.putExtra(Const.ParamType.TypePayPhoneNumber, mPhoneNumberString);
			intent.putExtra(Const.ParamType.TypePayMoney, mRechargeMoney);
			startActivityForResult(intent, Const.RequestCode.ChinaMobileSms);
		}
		else
		{
			doRequest(mPayChannel.PayType, mPayChannel.PayId);
		}
	}

	//电信充值
	private void doTeleComPay() {
		doRequest(mPayChannel.PayType, mPayChannel.PayId);
	}

	//联通充值
	private void doUniComPay() {
		doRequest(mPayChannel.PayType, mPayChannel.PayId);
	}
	
	private void startStep2() {
		Intent intent = null;
//		if (mJumpUnicomShop)
//		{
//			intent = new Intent(this, iCDPayChooseMoneyUnicomShopActivity.class);
//			intent.putExtra(Const.ParamType.TypeMobilePhone, mPhoneNumberString);
//			startActivityForResult(intent, Const.StepLevel.Step2);
//		}
//		else
//		{
			if(mPhoneEditText!=null){
				try{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(mPhoneEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			intent = new Intent(this, iCDPaySmsRechargeActivity.class);
			intent.putExtra(Const.ParamType.TypeMobilePhone, mPhoneNumberString);
			intent.putExtra(Const.ParamType.TypeJumpUnicomShop, mJumpUnicomShop);
			intent.putExtra(Const.ParamType.TypeStepLevel, Const.StepLevel.Step2);
			startActivityForResult(intent, Const.StepLevel.Step2);
//		}
	}

//	private void startStep3() {
//		Intent intent = new Intent(this, iCDPayCenterSmsPayActivity.class);
//		intent.putExtra(Const.ParamType.TypePayPhoneNumber, mPhoneNumberString);
//		intent.putExtra(Const.ParamType.TypeStepLevel, Const.StepLevel.Step3);
//		startActivityForResult(intent, Const.StepLevel.Step3);
//	}
		
	public static void startSmsRecharge(Activity activity, int requestCode) {
		Intent intent = new Intent(activity, iCDPaySmsRechargeActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected int getPayCode()
	{
		if(mJumpUnicomShop){
			return Const.PayCode.unicom_woshop;
		}else{
			return Const.PayCode.sms;
		}

	}
	
	@Override
	protected int getPayType()
	{		
//		int payType = 10013;
//		if (mOperatorsName.equals(Const.SimTypeName.simmobile)) {
//			//移动充值
//			payType = 10013;
//		}
//		else if (mOperatorsName.equals(Const.SimTypeName.simunicom)) {
//			payType = 10014;
//		}
//		else if (mOperatorsName.equals(Const.SimTypeName.simtelcom)) {
//			payType = 10015;
//		}
//		return payType;
		return mPayType;
	}

	@Override
	protected String getPayName()
	{		
//		String simType = Const.SimType.simmobile;
//		
//		int payType = getPayType();
//		if (payType == 10013)
//		{
//			simType = Const.SimType.simmobile;
//		}
//		else if (payType == 10014)
//		{
//			simType = Const.SimType.simunicom;
//		}
//		else if (payType == 10015)
//		{
//			simType = Const.SimType.simtelcom;
//		}
//		return simType;
		return mPayName;
	}
	
	private class SmsRechargeOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mLastSelectedIndex = arg2;
			((iCDPayChooseMoneyAdapter)arg0.getAdapter()).setOtherItemsUnSelected(arg2);
			((iCDPayChooseMoneyAdapter)arg0.getAdapter()).setItemSelected(arg2);
			
			String money = String.valueOf((int)((AmountLimit)arg0.getAdapter().getItem(arg2)).Value);
			storeSelectedMoney(money);
		}
	}
	
//	private String getOperatorByPhoneNumber(String phoneNumb) {
//		return getOperatorNameByType(getPayTypeByNumber(phoneNumb));
//	}
	
	private void getPayTypeByNumber(String phonenum)
	{
//		int operator = Const.SimOperator.Unknow;
//		TMobile tMobile = PayConfigReader.getInstance().mTMobile;
//		for (int i = 0; i < tMobile.Items.size(); i++)
//		{
//			TMobileItem item = tMobile.Items.get(i);
//			if (item.mlValue.contains(phonenum.substring(0, 3)))
//			{
//				operator = item.SimOperator;
//				break;
//			}
//		}
		
//		int payType = -1;
		Category cate = PayConfigReader.getInstance().getPayCategoryByCode(getPayCode());
		Channel channelItem;
		for (int i = 0; i < cate.Channels.size(); i++)
		{
			channelItem = cate.Channels.get(i);
			if (channelItem.MobileTypeList.contains(phonenum.substring(0, 3)))
			{
				mPayType = channelItem.PayType;
				mPayName = channelItem.Name;
				break;
			}
		}
		
		return;
	}
	
//	private String getOperatorNameByType(int nPayType) {
//		String simpaytype = Const.SimTypeName.simmobile;
//		switch(nPayType)
//		{
//		case 10013:
//			simpaytype = Const.SimTypeName.simmobile;
//			break;
//		case 10014:	
//			simpaytype = Const.SimTypeName.simunicom;
//			break;			
//		case 10015:	
//			simpaytype = Const.SimTypeName.simtelcom;
//			break;			
//		default:	
//			simpaytype = Const.SimTypeName.simunknow;
//			break;
//		}
//		
//		return simpaytype;
//	}
	

	private OnRechargeListener mOnRechargeListener = new OnRechargeListener()
	{

		@Override
		public void OnRecharge(Object rechargeInfo) {
			// TODO Auto-generated method stub
			hideWaitCursor();

			if (rechargeInfo instanceof PayEntity) 
			{
				PayEntity entity = (PayEntity)rechargeInfo;
				if (true == entity.result) {
					gotoSmsPay(entity);
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
			else {
				showResponseInfo((Integer)rechargeInfo);
			}
		}
		
	};
	
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
					gotoSmsPay(entity);
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
			else {
				showResponseInfo((Integer)createOrderInfo);
			}
		}
		
	};

	private void doRequest(int nPayType, int nPayID)
	{		
		showWaitCursor(null, getString(MResource.getIdByName(getApplication(), "string", "ipay_wait_for_request_data")));
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
			String money = String.valueOf(mRechargeMoney);
			((OrderCreateContent)orderCreateRequestInfo.Content).ShopItemId = this.getShopItemIdByMoney(money);
			PayRequestManager.getInstance().requestCreateOrder(orderCreateRequestInfo, this);
		}
		//更新首页排序

		int payCode = getPayCode();
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

	private void gotoSmsPay(PayEntity entity)
	{
		if (entity.JumpUrl != null && !TextUtils.isEmpty(entity.JumpUrl))
		{
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra(Const.ParamType.TypeTitle, 	getString(MResource.getIdByName(getApplication(), "string", "ipay_sms_pay_title")));
			intent.putExtra(Const.ParamType.TypeUrl, 	entity.JumpUrl);
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
			
			startActivityForResult(intent, 1000);
		}
		else
		{
			Intent intent = new Intent(this, iCDPayCenterSmsPayActivity.class);
			
			if (UserInfo.getInstance().RechargeFlag)
			{
				intent.putExtra(Const.ParamType.TypePayOrderNumber, UserInfo.getInstance().mUserName);
			}
			else
			{
				intent.putExtra(Const.ParamType.TypePayOrderNumber, PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo().CooperatorOrderSerial);
			}
			intent.putExtra(Const.ParamType.TypePayMoney, mRechargeMoney);
			intent.putExtra(Const.ParamType.TypePayMerchandise, PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo().MerchandiseName);
			intent.putExtra(Const.ParamType.TypePayPhoneNumber, mPhoneNumberString);
			intent.putExtra(Const.ParamType.TypePayMerchandiseID, PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo().MerchandiseID);
			intent.putExtra(Const.ParamType.TypePaySmsReceiver, entity.Receiver);
			intent.putExtra(Const.ParamType.TypePaySendMessage, entity.Message);
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
			intent.putExtra(Const.ParamType.TypeJumpUnicomShop, mJumpUnicomShop);
			
			startActivityForResult(intent, 1000);
		}
	}	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mNeedQuitWhenFinish) {
				Intent intent = new Intent();
				intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (data != null) {
			boolean quit = data.getBooleanExtra(Const.ParamType.TypeNeedQuitOrNot, false);
			if (quit) {
				mNeedQuitWhenFinish = quit;
			}
		}
		if (mNeedQuitWhenFinish) {
			if (data == null) {
				data = new Intent();
			}
			data.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
			setResult(RESULT_OK, data);
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Const.StepLevel.Step2:
			if (RESULT_OK == resultCode) {
				finish();
			}
			break;

		default:
			break;
		}
	}
	
}
