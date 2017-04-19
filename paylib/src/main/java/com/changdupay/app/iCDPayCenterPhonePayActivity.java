package com.changdupay.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;
import com.changdupay.widget.EditTextInputWraper;
import com.changdupay.widget.ITextChanged;

public class iCDPayCenterPhonePayActivity extends BaseActivity implements ITextChanged{	
	private double mPayMoney = 0.0;
	protected Channel mPayChannel = null;
	
	private Button mNextButton;
	
	private EditText mCardNumber = null;
	private EditText mCardPassword = null;

	private int mCardNumberMaxLen = 17;
	private int mCardPwdMaxLen = 18;
	
	private EditTextInputWraper mCardNumberInputWraper = null;
	private EditTextInputWraper mCardPasswordWraper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPayChannel = (Channel)getIntent().getExtras().getSerializable(Const.ParamType.TypePayChannelItem);
		mPayMoney = getIntent().getExtras().getDouble(Const.ParamType.TypePayMoney);
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_rechargecard_recharge_step3"));
		initView();
		initEvent();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();

		LoginResponseManager.getInstance().getResponseHandler().setRechargeRegisterListener(mOnRechargeListener);
		LoginResponseManager.getInstance().getResponseHandler().setCreateOrderRegisterListener(mOnCreateOrderListener);
	}
	
	private void initView()
	{
		showBackBtn();
		setTitle(getString(MResource.getIdByName(getApplication(), "string", "ipay_phonecard_recharge_title")));
		
		TextView tvPhoneType = (TextView)findViewById(MResource.getIdByName(this.getApplication(), "id", "phonetype_textview"));
		if (tvPhoneType != null) {
			tvPhoneType.setText(String.format(getString(MResource.getIdByName(this.getApplication(), "string", "ipay_choose_rechargecard_money")), mPayChannel.Name));
		}
	}
	
	private void initEvent()
	{
		mCardNumber = (EditText)findViewById(MResource.getIdByName(getApplication(), "id", "card_edittext"));
		mCardNumberInputWraper = new EditTextInputWraper(this, mCardNumber, 1);
		mCardNumber.addTextChangedListener(mCardNumberInputWraper);
		
		mCardPassword = (EditText)findViewById(MResource.getIdByName(getApplication(), "id", "pwd_edittext"));
		mCardPasswordWraper = new EditTextInputWraper(this, mCardPassword, 1);
		mCardPassword.addTextChangedListener(mCardPasswordWraper);
		
		mNextButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "next_btn"));
		mNextButton.setOnClickListener(btnOnClickListener);

    	updateTextMaxLength();
	}
	
	private void checkNextBtnEnabled() {
		if (mPayMoney > 0 && mCardNumberMaxLen == mCardNumber.getText().length() 
				&& mCardPwdMaxLen == mCardPassword.getText().length()) {
			mNextButton.setEnabled(true);
		}
		else {
			mNextButton.setEnabled(false);
		}
	}
	
	private void updateTextMaxLength() {
		if (mPayChannel == null) {
			return;
		}
		
		if (mPayChannel.PayType == 10016)
		{
			mCardNumberMaxLen = 17;
			mCardPwdMaxLen = 18;
		}
		else if (mPayChannel.PayType == 10017) 
		{
			mCardNumberMaxLen = 15;
			mCardPwdMaxLen = 19;
		}
		else if (mPayChannel.PayType == 10018) 
		{
			mCardNumberMaxLen = 19;
			mCardPwdMaxLen = 18;
		}

		mCardNumber.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(mCardNumberMaxLen)});
		mCardNumberInputWraper.setMaxLength(mCardNumberMaxLen);
		mCardPassword.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(mCardPwdMaxLen)});
		mCardPasswordWraper.setMaxLength(mCardPwdMaxLen);
	}
	
	private OnClickListener btnOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (checkCardLengthValid()) {
				gotoPay();
			}
		}
		
	};
	
	private boolean checkCardLengthValid() {
		updateTextMaxLength();
		String number = mCardNumber.getText().toString().trim();
		String card = mCardPassword.getText().toString().trim();
		if (number.length() != mCardNumberMaxLen) {
			mCardNumber.requestFocus();
			mCardNumber.setText(number);
			String hint = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_recharge_card_number_len_error")), mPayChannel.Name, mCardNumberMaxLen);
			ToastHelper.shortDefaultToast(hint);
			return false;
		}
		if (card.length() != mCardPwdMaxLen) {
			mCardPassword.requestFocus();
			mCardPassword.setText(card);;
			String hint = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_recharge_card_pwd_len_error")), mPayChannel.Name, mCardPwdMaxLen);
			ToastHelper.shortDefaultToast(hint);
			return false;
		}
		return true;
	}
	
	private void gotoPay()
	{
		showWaitCursor(null, getString(MResource.getIdByName(getApplication(), "string", "ipay_wait_for_request_data")));
		if (UserInfo.getInstance().RechargeFlag)
		{
			RechargeRequestInfo rechargeRequestInfo = new RechargeRequestInfo();
			((RechargeRequestContent)rechargeRequestInfo.Content).PayId = mPayChannel.PayId;
			((RechargeRequestContent)rechargeRequestInfo.Content).PayType = mPayChannel.PayType;
			((RechargeRequestContent)rechargeRequestInfo.Content).Amount = mPayMoney;
			((RechargeRequestContent)rechargeRequestInfo.Content).CardNumber = mCardNumber.getText().toString();
			((RechargeRequestContent)rechargeRequestInfo.Content).CardPassword = mCardPassword.getText().toString();
			
			PayRequestManager.getInstance().requestRechargeResult(rechargeRequestInfo, iCDPayCenterPhonePayActivity.this);
		}
		else
		{
			iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
			
			OrderCreateRequestInfo orderCreateRequestInfo = new OrderCreateRequestInfo();
			((OrderCreateContent)orderCreateRequestInfo.Content).PayId = mPayChannel.PayId;
			((OrderCreateContent)orderCreateRequestInfo.Content).PayType = mPayChannel.PayType;
			((OrderCreateContent)orderCreateRequestInfo.Content).MerchandiseID = payOrderInfo.MerchandiseID;
			((OrderCreateContent)orderCreateRequestInfo.Content).MerchandiseName = payOrderInfo.MerchandiseName;
			((OrderCreateContent)orderCreateRequestInfo.Content).CooperatorOrderSerial = payOrderInfo.CooperatorOrderSerial;
			((OrderCreateContent)orderCreateRequestInfo.Content).OrderMoney = String.valueOf(mPayMoney);
			((OrderCreateContent)orderCreateRequestInfo.Content).CardNumber = mCardNumber.getText().toString();;
			((OrderCreateContent)orderCreateRequestInfo.Content).CardPassword = mCardPassword.getText().toString();;
			((OrderCreateContent)orderCreateRequestInfo.Content).UserName = payOrderInfo.UserName;
			((OrderCreateContent)orderCreateRequestInfo.Content).UserID = payOrderInfo.UserID;
			((OrderCreateContent)orderCreateRequestInfo.Content).ShopItemId = this.getShopItemIdByMoney(String.valueOf(mPayMoney));
			
			PayRequestManager.getInstance().requestCreateOrder(orderCreateRequestInfo, iCDPayCenterPhonePayActivity.this);
		}
		//更新首页排序
		int payCode = Const.PayCode.rechargecard;
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
	
	private void prcoessResponseData(BaseProtocalData entity)
	{
		if (true == entity.result) {
			Utils.gotoPaySuccessActivity(entity.errorMsg, this);
		}
		else {
			ToastHelper.shortDefaultToast(entity.errorMsg);
			//showFailedDailog();
		}
	}
	
	protected void onCloseFailedDialog() {
		onActivityResult(0, RESULT_OK, null);
	}
	
	private OnRechargeListener mOnRechargeListener = new OnRechargeListener()
	{

		@Override
		public void OnRecharge(Object rechargeInfo) {
			// TODO Auto-generated method stub
			hideWaitCursor();

			if (rechargeInfo instanceof PayEntity) 
			{
				prcoessResponseData((PayEntity)rechargeInfo);
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
				prcoessResponseData((PayEntity)createOrderInfo);
			}
			else if (createOrderInfo instanceof BaseProtocalData)
			{
				BaseProtocalData entity = (BaseProtocalData)createOrderInfo;
				prcoessResponseData(entity);
			}
			else {
				showResponseInfo((Integer)createOrderInfo);
			}
		}
		
	};

	protected int getPayType() {
		return mPayChannel.PayType;
	}
	
	@Override
	public EditText getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void textChanged(EditText edit, String s, int maxLength) {
		// TODO Auto-generated method stub
		if (s.length() >= maxLength)
		{
			Utils.hideSoftKeyPanel(iCDPayCenterPhonePayActivity.this);
		}
		checkNextBtnEnabled();
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
	}
}
