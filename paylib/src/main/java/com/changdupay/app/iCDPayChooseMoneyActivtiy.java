package com.changdupay.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.changdupay.channel.alipay.AlipayHelper;
import com.changdupay.open.iChangduPay;
import com.changdupay.protocol.LoginResponseManager;
import com.changdupay.protocol.ProtocolData.BaseProtocalData;
import com.changdupay.protocol.ProtocolData.PayEntity;
import com.changdupay.protocol.base.PayConst;
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
import com.changdupay.util.PayConfigs.AmountLimit;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;
import com.changdupay.widget.EditTextInputWraper;
import com.changdupay.widget.ITextChanged;

public class iCDPayChooseMoneyActivtiy extends iCDPayChooseMoneyBaseActivity implements ITextChanged{
	protected String mPayErrorMsg = "";
	protected int mPayResultCode = iChangduPay.ResultCode.Canceled;
	
	protected GridView mChooseMoneyGridView = null;
	
	protected EditText mMoneyEdit = null;
	protected Boolean mInitClearEditText = true;
	protected String mPayMoney = "";
	
	private Button mNextBtn = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_choose_money_view"));

		initView();
		//initEvent();
		
		mPayErrorMsg = getString(MResource.getIdByName(getApplication(), "string", "ipay_user_canceled"));
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		initEvent();
		LoginResponseManager.getInstance().getResponseHandler().setRechargeRegisterListener(mOnRechargeListener);
		LoginResponseManager.getInstance().getResponseHandler().setCreateOrderRegisterListener(mOnCreateOrderListener);
	}
	
	private void prcoessResponseData(PayEntity entity)
	{
		if (!TextUtils.equals(entity.JumpUrl, ""))
		{
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra(Const.ParamType.TypeTitle, 	getString(MResource.getIdByName(getApplication(), "string", getTopTitle())));
			intent.putExtra(Const.ParamType.TypeUrl, 	entity.JumpUrl);
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
			intent.putExtra(Const.ParamType.TypeHideWaitCursor, true);
			
			startActivityForResult(intent, 1000);
		}
		else if(!TextUtils.equals(entity.PackageName, ""))
		{
			//调用alipay支付
			if (TextUtils.equals(Const.ALIPAY_PACKAGE_NAME, entity.PackageName))
			{
				AlipayHelper ah = new AlipayHelper();
				
				ah.excutePay(entity.Parameter, this, mNeedQuitWhenFinish);
			} 
//			//调用财付通支付
//			if (TextUtils.equals(Const.ONECLCIK_PACKAGE_NAME, entity.PackageName))
//			{
//				PayRequestManager.getInstance().requestOneClickBankList(this);
//			}
		}
		else if (!TextUtils.equals(entity.Receiver, ""))
		{
			iCDPayOrderInfo payOrderInfo = PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
			
			//短信支付
			Intent intent = new Intent(this, iCDPayCenterSmsPayActivity.class);
			
			if (UserInfo.getInstance().RechargeFlag)
			{
				intent.putExtra(Const.ParamType.TypePayOrderNumber, UserInfo.getInstance().mUserName);
			}
			else
				intent.putExtra(Const.ParamType.TypePayOrderNumber, payOrderInfo.CooperatorOrderSerial);
			intent.putExtra(Const.ParamType.TypePayMoney, getPayMoney());
			intent.putExtra(Const.ParamType.TypePayMerchandise, payOrderInfo.MerchandiseName);
			intent.putExtra(Const.ParamType.TypePayMerchandiseID, payOrderInfo.MerchandiseID);
			intent.putExtra(Const.ParamType.TypePaySmsReceiver, entity.Receiver);
			intent.putExtra(Const.ParamType.TypePaySendMessage, entity.Message);
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);

			startActivityForResult(intent, 1000);
		}
		else if (5 == entity.ExecType && !TextUtils.isEmpty(entity.Parameter)){
			String[] arrStr = entity.Parameter.split("\\|");
			if(arrStr.length == 4){
				//启动参数 	appid|appkey| paycode| extData				
				doMMPayAction(arrStr);
				
			}
		}
		else if(6== entity.ExecType && !TextUtils.isEmpty(entity.Parameter))
		{
			doRequestResult(entity);
		}
		else if(7== entity.ExecType)
		{
			doRequestResult(entity);
		}
	}
	 
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
	
	private OnCreateOrderListener mOnCreateOrderListener = new OnCreateOrderListener()
	{

		@Override
		public void OnCreateOrder(Object createOrderInfo) {
			// TODO Auto-generated method stub
			hideWaitCursor();
			
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
	
	protected void initView()
	{
		if (getInitialSelectedMoney() != null)
			mPayMoney = getInitialSelectedMoney();
		else
		{
			Channel channel = getPayChannel();
			if (channel != null)
			{
				mPayMoney = String.valueOf((int)channel.AmountLimits.get(0).Value);
			}
		}
		View view = ((View)findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_input_layout")));
		view.setVisibility(View.INVISIBLE);
		
		mMoneyEdit = ((EditText) findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_inputedit")));
		mMoneyEdit.setVisibility(View.VISIBLE);
		mMoneyEdit.setText(mPayMoney);
		mMoneyEdit.setTag(Integer.valueOf(-1));
		
		TextView tv_input_money_ratio = (TextView)findViewById(MResource.getIdByName(getApplication(), "id", "tv_input_money_ratio"));
		String text = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_input_money_ratio")), PayConst.COIN_NAME);
		tv_input_money_ratio.setText(text);
		
		EditTextInputWraper inputWraper = new EditTextInputWraper(this, mMoneyEdit, -1);
		inputWraper.setIsDoubleType(true);
		mMoneyEdit.addTextChangedListener(inputWraper);
		mMoneyEdit.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mHandler.post(mRunnable);
				return false;
			}
		});

		((TextView) findViewById(MResource.getIdByName(getApplication(), "id", "title_textview")))
		.setText(getString(MResource.getIdByName(getApplication(), "string", getTopTitle())));
		showBackBtn();
	}
	
	Handler mHandler = new Handler();
	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			if (mInitClearEditText)
			{
				mInitClearEditText = false;
				mPayMoney = "";
				mMoneyEdit.setTag(Integer.valueOf(-1));
				mMoneyEdit.setText(mPayMoney);
				setSelectedByMoney(mPayMoney);
			}
		}
	
	};
	
	Runnable mForbidInputRunnable = new Runnable() {
		@Override
		public void run() {
			mMoneyEdit.setTag( Integer.valueOf(-1));
			mMoneyEdit.setText(mPayMoney);
			mMoneyEdit.setSelection(mPayMoney.length());
			}
		};
		
	Runnable mSetInputRunnable = new Runnable() {
		@Override
		public void run() {
			   Integer tag = (Integer)mMoneyEdit.getTag();
			   
				if(tag >= 0 )
				{
					setSelectedByIndex(tag);
				}
				else
				{
					
					mMoneyEdit.setTag(Integer.valueOf(-1));
					setSelectedByMoney(mPayMoney);
				}
				storeSelectedMoney(mPayMoney);
			}
		};
		
	protected void initEvent()
	{
		mChooseMoneyGridView = (GridView)findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_gridview"));
		mPayChooseMoneyAdapter = new iCDPayChooseMoneyAdapter(this, this, getApplication());
		mChooseMoneyGridView.setAdapter(mPayChooseMoneyAdapter);
		Utils.setGridViewHeightBasedOnChildren(iCDPayChooseMoneyActivtiy.this, mChooseMoneyGridView, 2);
		mChooseMoneyGridView.setOnItemClickListener(new ChooseMoneyOnItemClickListener());
		mChooseMoneyGridView.scrollTo(0, 0);
		
		mNextBtn = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_next_btn"));
		mNextBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doNext(mPayMoney);
			}
			
		});
		setInitialSelectedItem();
	}

	private class ChooseMoneyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			((iCDPayChooseMoneyAdapter)arg0.getAdapter()).setOtherItemsUnSelected(arg2);
			((iCDPayChooseMoneyAdapter)arg0.getAdapter()).setItemSelected(arg2);
			mPayMoney = String.valueOf((int)((AmountLimit)arg0.getAdapter().getItem(arg2)).Value);
		
			mMoneyEdit.setTag(Integer.valueOf(arg2));
			mMoneyEdit.setText(mPayMoney);
			
			mMoneyEdit.setSelection(mPayMoney.length());
			storeSelectedMoney(mPayMoney);
		}
	}
		
	protected String getPayMoney()
	{
		return mPayMoney;
	}
	
	
	protected int getSelectIndex()
	{
		Integer tag = (Integer)mMoneyEdit.getTag();
		return tag;
	}
	
	
	protected String getTopTitle()
	{
		return "ipay_appname_title";
	}
	
	private boolean checkMoneyValid(String strMoney) {
		if (!TextUtils.isEmpty(strMoney)) {
			try {
				double dMoney = Double.parseDouble(strMoney);
				double nMaxLimit = PayConfigReader.getInstance().mRechargeAmount.UpperLimit;
				double nMinLinit = PayConfigReader.getInstance().mRechargeAmount.LowerLimit;
				
				if (dMoney < nMinLinit) {
					String strMsg = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_input_money_too_few")), 
							nMinLinit);
					ToastHelper.shortDefaultToast(strMsg);
				}
				else if (dMoney > nMaxLimit){
					String strMsg = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_input_money_too_much")), 
							nMaxLimit);
					ToastHelper.shortDefaultToast(strMsg);
				}
				else {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {			
			ToastHelper.shortDefaultToast(MResource.getIdByName(getApplication(), "string", "ipay_money_invalid"));
		}
		
		return false;
	}

	protected void doPayAction()
	{
		
	}
	
	private void doNext(String sMoney)
	{
		if (mPayChannel == null)
			return;
		
		if (!checkMoneyValid(sMoney)) {
			return;
		}
		
		doPayAction();
	}
	
	public void doRequestEx(int nPayType, int nPayID)
	{
		this.doRequest(nPayType, nPayID);
	}
	
	protected void doRequest(int nPayType, int nPayID)
	{
		showWaitCursor(null, getString(MResource.getIdByName(getApplication(), "string", "ipay_wait_for_request_data")));
		if (UserInfo.getInstance().RechargeFlag)
		{
			RechargeRequestInfo rechargeRequestInfo = new RechargeRequestInfo();
			((RechargeRequestContent)rechargeRequestInfo.Content).PayId = nPayID;
			((RechargeRequestContent)rechargeRequestInfo.Content).PayType = nPayType;
			((RechargeRequestContent)rechargeRequestInfo.Content).Amount = Double.parseDouble(getPayMoney());
			
			PayRequestManager.getInstance().requestRechargeResult(rechargeRequestInfo, iCDPayChooseMoneyActivtiy.this);
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
			((OrderCreateContent)orderCreateRequestInfo.Content).PhoneNumber = getPhoneNumber();
			((OrderCreateContent)orderCreateRequestInfo.Content).OrderMoney = getPayMoney();
			((OrderCreateContent)orderCreateRequestInfo.Content).UserName = payOrderInfo.UserName;
			((OrderCreateContent)orderCreateRequestInfo.Content).UserID = payOrderInfo.UserID;
			String shopItem = this.getShopItemIdByIndex(getSelectIndex(), getPayMoney());
			if(shopItem.length() == 0)
			{
				shopItem = this.getShopItemIdByMoney(getPayMoney());
			}
			//Log.e("shopItem", shopItem);
			((OrderCreateContent)orderCreateRequestInfo.Content).ShopItemId = shopItem;
			
			PayRequestManager.getInstance().requestCreateOrder(orderCreateRequestInfo, iCDPayChooseMoneyActivtiy.this);
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

	@Override
	public EditText getEditView() {
		// TODO Auto-generated method stub
		return mMoneyEdit;
	}

	@Override
	public void textChanged(EditText edit, String s, int maxLength) {
		// TODO Auto-generated method stub
		if (TextUtils.equals(s, "")
		   || TextUtils.equals(s, ".")
		   || TextUtils.equals(s, "0"))
		{
			mPayMoney = "";
			return;
		}
		if (s.length() == 2)
		{
			if ((s.charAt(0) == '0' && s.charAt(1) != '.'))
			{
				mPayMoney = s.substring(1);
				mHandler.post(mForbidInputRunnable);
				return;
			}
		}
		
		mPayMoney = s;
		
		mHandler.post(mSetInputRunnable);
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
			//finish();
			//System.exit(0);
			if (data == null) {
				data = new Intent();
			}
			data.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
			setResult(RESULT_OK, data);
			finish();
			doWhenFinish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
//	protected void doWhenFinish() {
//		IPayCallback callback = ipay.getCallback();
//		if (callback != null) {
//			callback.onPayCallback(mPayResultCode, mPayErrorMsg);
//		}
//	}
	
	protected void doMMPayAction(String[] arrStr)
	{
		
	}
	
	protected void doRequestResult(PayEntity payEntity)
	{
		
	}
}
