package com.changdupay.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.changdupay.util.Const;
import com.changdupay.util.MResource;

public class SmsRechargeResultActivity extends BaseActivity {
	boolean mSuccess = true;
	Button mRechargeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_sms_recharge_result"));
		
		mNeedQuitWhenFinish = true;
		
		initView();
		initEvent();
	}
	
	private void initView() {		
		mRechargeButton = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "recharge"));
		
		showBackBtn();
	}
	
	private void initEvent() {
		mRechargeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mNeedQuitWhenFinish) {
				Intent intent = new Intent();
				intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
