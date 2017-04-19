package com.changdupay.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.changdupay.commonInterface.BaseCommonStruct;
import com.changdupay.commonInterface.CommonConst;
import com.changdupay.commonInterface.CommonInterfaceManager;
import com.changdupay.encrypt.SmsUtils;
import com.changdupay.util.Const;
import com.changdupay.util.MResource;
import com.changdupay.util.ToastHelper;
import com.changdupay.util.Utils;

public class iCDPayCenterSmsPayActivity extends BaseActivity {
	protected String mReceiver = "";
	protected String mSendMessage = "";
	
	private TextView mReceiverTextView = null;
	private TextView mMessageTextView = null;
	private boolean mFirstStartSend = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplication(), "layout", "ipay_pay_center_sms"));
		initView();
		initEvent();
		
		registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(sendMessage);
	}

	private void initView()
	{
		Intent intent = getIntent();

		mReceiverTextView = (TextView) findViewById(MResource.getIdByName(getApplication(), "id", "smspaycenter_receiver"));
		mReceiver = intent.getExtras().getString(Const.ParamType.TypePaySmsReceiver);
		mReceiverTextView.setText(mReceiver);
		
		mMessageTextView = (TextView) findViewById(MResource.getIdByName(getApplication(), "id", "smspaycenter_message"));
		mSendMessage = intent.getExtras().getString(Const.ParamType.TypePaySendMessage);
		mMessageTextView.setText(mSendMessage);
		
		String phonenumber = intent.getExtras().getString(Const.ParamType.TypePayPhoneNumber);
		String hintText = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_smspay_sendmessagehint")), phonenumber);

		int index = hintText.indexOf(phonenumber); 
		SpannableStringBuilder style = new SpannableStringBuilder(hintText);     
        style.setSpan(new ForegroundColorSpan(Color.rgb(89, 178, 63)),index,index+phonenumber.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
        ((TextView) findViewById(MResource.getIdByName(getApplication(), "id", "ipay_smspay_sendmessagehint_label"))).setText(style);

		String title = intent.getExtras().getString(Const.ParamType.TypePayName);
		if (title != null && !TextUtils.equals(title, ""))
		{
			((TextView) findViewById(MResource.getIdByName(getApplication(), "id", "title_textview"))).setText(title);
		}
		else{
			boolean jumpUnicomShop = getIntent().getBooleanExtra(Const.ParamType.TypeJumpUnicomShop, false);
			if(jumpUnicomShop){
				((TextView) findViewById(MResource.getIdByName(getApplication(), "id", "title_textview"))).setText(getString(MResource.getIdByName(getApplication(), "string", "ipay_unicom_shop")));
			}
			else{
				((TextView) findViewById(MResource.getIdByName(getApplication(), "id", "title_textview"))).setText(getString(MResource.getIdByName(getApplication(), "string", "ipay_sms_pay_title")));
			}
		}
		showBackBtn();
	}
	
	protected void initEvent()
	{
		Button nextBtn = (Button)findViewById(MResource.getIdByName(getApplication(), "id", "smspay_generatesms_btn"));
		nextBtn.setOnClickListener(btnOnClickListener);
	}
	
	/**发送与接收的广播**/
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private BroadcastReceiver sendMessage = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BaseActivity.hideWaitCursor();
			if(mFirstStartSend){
				mFirstStartSend = false;
			}
			else{
				return;
			}
			//判断短信是否发送成功
            switch (getResultCode()) {
            case Activity.RESULT_OK:
            	gotoSuccessAActivity();
                //Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
                break;
            default:
            	ToastHelper.shortDefaultToast(getString(MResource.getIdByName(getApplication(), "string", "ipay_recharge_sms_send_error_tip")));	
                //Toast.makeText(mContext, "发送失败", Toast.LENGTH_LONG).show();
                break;
            }
			
		}
    };
    
    private void sendSMS(String phoneNumber, String message) 
    {
    	Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,0);
    	SmsUtils.sendSms(phoneNumber, message, sentPI);
    	mFirstStartSend = true;
    }
    
	private OnClickListener btnOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			BaseCommonStruct getViewClickStruct=new BaseCommonStruct();
//			getViewClickStruct.obj1=v;
//			int result = CommonInterfaceManager.INSTANCE.CommonInterfaceID(
//					CommonConst.MAIN_MODELID, CommonConst.MAIN_CHECK_VIEW_CLICK_10001, getViewClickStruct);
//			if (result != CommonConst.COM_RET_OK){
//				return;
//			}
			
			BaseActivity.showWaitCursor(null, getString(MResource.getIdByName(getApplication(), "string", "ipay_wait_for_request_data")));
			sendSMS(mReceiver, mSendMessage);
		}
		
	};
	
	private void gotoSuccessAActivity()
	{
//		Intent intent = new Intent(this, PayResultActivity.class);
//		intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, mNeedQuitWhenFinish);
//		startActivityForResult(intent, 1000);
		Utils.gotoPaySuccessActivity("", this);
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
		if (requestCode == Const.RequestCode.SendSms) {
			gotoSuccessAActivity();
			return;
		}
		
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
