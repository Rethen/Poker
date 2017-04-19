package com.changdupay.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
public class SmsReceiver extends BroadcastReceiver{
	private String TAG = "SmsReceiver";
	private static OnReceiveSmsListener mOnReceiveSmsListener;
	
	public static void setmOnReceiveSmsListener(OnReceiveSmsListener mOnReceiveSmsListener) {
		SmsReceiver.mOnReceiveSmsListener = mOnReceiveSmsListener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, ">>>>>>>onReceive start");
		
		StringBuilder body = new StringBuilder();// 短信内容
		StringBuilder number = new StringBuilder();// 短信发件人
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object[] _pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] message = new SmsMessage[_pdus.length];
			for (int i = 0; i < _pdus.length; i++) {
				message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
			}
			String strContentString = "";
			String strSenderString = "";
			boolean flags_filter = false;
			for (SmsMessage currentMessage : message) {
				body.append(currentMessage.getDisplayMessageBody());
				number.append(currentMessage.getDisplayOriginatingAddress());

				strContentString = currentMessage.getMessageBody();
				strSenderString = currentMessage.getOriginatingAddress();
				if (strSenderString.contains("+86")) {
					strSenderString = strSenderString.substring(3);
				}
				for (String phone : Const.VerifyCodePhoneNumbers) {
					if (strSenderString.equals(phone)) {
						flags_filter = true;
						break;
					}
				}
			}
			
			if (flags_filter) {
				if (mOnReceiveSmsListener != null) {
					mOnReceiveSmsListener.onReceive(body.toString());
				}
				this.abortBroadcast();
			}
		}
		Log.v(TAG, ">>>>>>>onReceive end");
	}
	
	public interface OnReceiveSmsListener {
		void onReceive(Object info);
	}
}