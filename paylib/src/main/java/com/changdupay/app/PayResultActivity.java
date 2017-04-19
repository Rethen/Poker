package com.changdupay.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.changdupay.open.iChangduPay;
import com.changdupay.open.iChangduPay.ResultCode;
import com.changdupay.util.Const;
import com.changdupay.util.MResource;

public class PayResultActivity extends BaseActivity {
	boolean mSuccess = true;
	Button mRechargeButton;
	String mResultMsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_pay_result"));

		mSuccess = getIntent().getBooleanExtra(Const.ParamType.TypePayResult, true);
		mResultMsg = getIntent().getStringExtra(Const.ParamType.TypePayResultMsg);
		
		mNeedQuitWhenFinish = true;
		if (mSuccess) {
			iChangduPay.setResult(ResultCode.Success, "success");
		}
		else {
			if (TextUtils.isEmpty(mResultMsg)) {
				mResultMsg = getString(MResource.getIdByName(getApplication(), "string", "ipay_recharge_error_tip"));
			}
			iChangduPay.setResult(ResultCode.Failed, mResultMsg);
		}
		
		initView();
		initEvent();
	}
	
	private void initView() {		
		mRechargeButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "recharge"));	

		TextView mTipTitleTextView = (TextView)findViewById(MResource.getIdByName(getApplication(), "id", "tip_title"));
		Drawable drawable = null;
		
		if (!mSuccess) {
			TextView mTipTextView = (TextView)findViewById(MResource.getIdByName(getApplication(), "id", "tip"));	
			
			mTipTitleTextView.setText(MResource.getIdByName(getApplication(), "string", "ipay_recharge_error_tip"));			
			mTipTextView.setText(mResultMsg);	
			if(iCDPayUtilsEx.bRequestNoUI)
			{
			
			}
			else
			{
				mRechargeButton.setText(MResource.getIdByName(getApplication(), "string", "ipay_continue_to_recharge"));
			}
			drawable = getResources().getDrawable(MResource.getIdByName(getApplication(), "drawable", "ipay_pay_failed"));
		}
		else {
			drawable = getResources().getDrawable(MResource.getIdByName(getApplication(), "drawable", "ipay_check"));
		}
		
		if (drawable != null) {
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mTipTitleTextView.setCompoundDrawables(drawable, null, null, null);
		}
		
		showBackBtn();
	}
	
	private void initEvent() {
		mRechargeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				doWhenFinish();
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mNeedQuitWhenFinish) {
				doWhenFinish();
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void doWhenFinish() {
		if (mNeedQuitWhenFinish) {
			Intent intent = new Intent();
			intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
			setResult(RESULT_OK, intent);
		}
	}
}
