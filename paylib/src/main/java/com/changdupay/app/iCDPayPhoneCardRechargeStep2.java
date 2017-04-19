package com.changdupay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigs.AmountLimit;
import com.changdupay.util.PayConfigs.Channel;

public class iCDPayPhoneCardRechargeStep2 extends iCDPayChooseMoneyBaseActivity{	
	private double mPayMoney = 0.0;
	
	private GridView mMoneyGridView;
	private Button mNextButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPayChannel = (Channel)getIntent().getExtras().getSerializable(Const.ParamType.TypePayChannelItem);
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_rechargecard_recharge_step2"));
		initView();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		initEvent();
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
		mMoneyGridView = (GridView)findViewById(MResource.getIdByName(getApplication(), "id", "choosemoney_gridview"));
		mPayChooseMoneyAdapter = new iCDPayChooseMoneyAdapter(this, this, getApplication());
		mMoneyGridView.setAdapter(mPayChooseMoneyAdapter);
		mMoneyGridView.setOnItemClickListener(new PhonePayOnItemClickListener());
//		if (mPayChooseMoneyAdapter.getCount() > 0) {
//			mPayChooseMoneyAdapter.setInitialSelectedItem(0);
//			mPayMoney = ((AmountLimit)mPayChooseMoneyAdapter.getItem(0)).Value;
//		}
		if (getInitialSelectedMoney() != null)
			mPayMoney = Double.parseDouble(getInitialSelectedMoney());
		else
			mPayMoney = ((AmountLimit)mPayChooseMoneyAdapter.getItem(0)).Value;
		setInitialSelectedItem();
		
		mNextButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "next_btn"));
		mNextButton.setOnClickListener(btnOnClickListener);

    	checkNextBtnEnabled();
	}

	public class PhonePayOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mPayMoney = ((AmountLimit)arg0.getAdapter().getItem(arg2)).Value;
			((iCDPayChooseMoneyAdapter)arg0.getAdapter()).setOtherItemsUnSelected(arg2);
			((iCDPayChooseMoneyAdapter)arg0.getAdapter()).setItemSelected(arg2);

	    	checkNextBtnEnabled();
	    	
	    	storeSelectedMoney(String.valueOf(mPayMoney));
		}		
	}
	
	private void checkNextBtnEnabled() {
		if (mPayMoney > 0) {
			mNextButton.setEnabled(true);
		}
		else {
			mNextButton.setEnabled(false);
		}
	}
	
	private OnClickListener btnOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			nextStep();
		}
		
	};
	
	private void nextStep() {
		Intent intent = new Intent(this, iCDPayCenterPhonePayActivity.class);
		intent.putExtra(Const.ParamType.TypePayMoney, mPayMoney);
		intent.putExtra(Const.ParamType.TypePayChannelItem, mPayChannel);
		startActivityForResult(intent, Const.StepLevel.Step3);
	}
	
	@Override
	protected int getPayCode() {
		return Const.PayCode.rechargecard;
	}
	
	@Override
	protected String getPayName()
	{
		String cardType = Const.CardType.mobilecard;
		
		int payType = mPayChannel.PayType;
		if (payType == 10016)
		{
			cardType = Const.CardType.mobilecard;
		}
		else if (payType == 10017)
		{
			cardType = Const.CardType.unicomcard;
		}
		else if (payType == 10018)
		{
			cardType = Const.CardType.telcomcard;
		}
		return cardType;
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
