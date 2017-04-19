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

import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;
import com.changdupay.widget.IAdapterDataWrap;

public class iCDPayPhoneCardRechargeActivity extends BaseActivity implements IAdapterDataWrap, OnClickListener{
	private Button	mNextButton;
	private GridView mGridView;
	
	private String mPhoneNumberString = "";
//	private String mOperatorsName = SimTypeName.simmobile;//移动运营商
	private int mLastSelectedIndex = 0;
	
	private Channel mPayChannelItem = null;

	protected Category mPayCategory = null;	
	protected ChoosePayTypeDataWrapper mChoosePayTypeDataWrapper = null;
	protected iCDPayChooseOperatorAdapter mGridAdapter = null;	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_rechargecard_recharge_step1"));
		
		mPayCategory = getPayCategory();
		initView();
		initEvent();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initEvent();
	}
	
	private void initView() {
		showBackBtn();
		setTitle(getString(MResource.getIdByName(getApplication(), "string", "ipay_phonecard_recharge_title")));
		
		mNextButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "next_btn"));
	}
	
	private void initEvent() {
		mGridView = (GridView)findViewById(MResource.getIdByName(getApplication(), "id", "operators_gridview"));
		mGridAdapter = new iCDPayChooseOperatorAdapter(this, this, this.getApplication());
		mGridView.setAdapter(mGridAdapter);
		
		mLastSelectedIndex = Utils.getIntegerValue(Const.ParamType.TypePhoneCardType);
		if (mGridAdapter.getCount() > 0) {
			mGridAdapter.setInitialSelectedItem(mLastSelectedIndex);
		}
		
		mGridView.setOnItemClickListener(new CardRechargeOnItemClickListener());
		mNextButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == mNextButton) {

			Utils.setStringValue(Const.ConfigKeys.LastPhoneNumber, mPhoneNumberString);
			
			if (mLastSelectedIndex < 0)
			{
				ToastHelper.shortDefaultToast(getString(MResource.getIdByName(getApplication(), "string", "ipay_channel_info_error")));				
			
				return;
			}
			mPayChannelItem = (Channel)mGridAdapter.getItem(mLastSelectedIndex);
			if (mPayChannelItem != null) {
				Intent intent = new Intent(this, iCDPayPhoneCardRechargeStep2.class);
				intent.putExtra(Const.ParamType.TypePayChannelItem, mPayChannelItem);
				startActivityForResult(intent, 1000);
			}
			else {
				ToastHelper.shortDefaultToast(getString(MResource.getIdByName(getApplication(), "string", "ipay_channel_info_error")));					
			}
		}
	}

	private class CardRechargeOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
	    	if (mLastSelectedIndex > -1 && mLastSelectedIndex != arg2) {
		    	mGridAdapter.unselectItem(mLastSelectedIndex);
			}
			mLastSelectedIndex = arg2;
	    	mGridAdapter.selectItem(mLastSelectedIndex);
	    	
	    	mNextButton.setEnabled(true);
	    	
	    	Utils.setIntegerValue(Const.ParamType.TypePhoneCardType, mLastSelectedIndex);
		}
	}

	protected int getPayCode()
	{		
		return Const.PayCode.rechargecard;
	}

	protected Category getPayCategory()
	{
		int payCode = getPayCode();
		if (payCode == -1)
			return null;
		
		return PayConfigReader.getInstance().getPayCategoryByCode(payCode);
	}

	@Override
	public ChoosePayTypeDataWrapper getData(int position) {
		// TODO Auto-generated method stub
		if (mChoosePayTypeDataWrapper == null)
		{
			mChoosePayTypeDataWrapper = new ChoosePayTypeDataWrapper();
		}
		Channel payChannelItem = mPayCategory.Channels.get(position);
		mChoosePayTypeDataWrapper.Name = payChannelItem.Name;
		
		return mChoosePayTypeDataWrapper;
	}

	@Override
	public Object getObject(int position) {
		// TODO Auto-generated method stub
		return mPayCategory.Channels.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mPayCategory == null || mPayCategory.Channels == null) {
			return 0;
		}
		return mPayCategory.Channels.size();
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
