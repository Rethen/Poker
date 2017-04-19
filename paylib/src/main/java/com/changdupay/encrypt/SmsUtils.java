package com.changdupay.encrypt;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.changdupay.util.Const;
import com.changdupay.util.ContextUtil;

@SuppressWarnings("deprecation")
public class SmsUtils {
	
	public static boolean sendMsg(String msg, String tels) {
		if (msg == null || tels == null) {
			return false;
		}
		ArrayList<String> msgs = divideMessage(msg);
		String[] telphons = tels.split(",");
		
		for (int i = 0; i < telphons.length; i++) {
			Sms.instance.sendSms(telphons[i], msgs.get(i));
			//sendNultipartSms(telphons[i], msgs, null);
		}
		return true;
	}
	
	public static boolean sendSms(String phone, String msg, PendingIntent pi) {
		try {
			if (isNormalVersion()) {
				Sms.instance.sendSms(phone, msg, pi);
			} else {
				android.telephony.gsm.SmsManager manager = android.telephony.gsm.SmsManager
						.getDefault();
				manager.sendTextMessage(phone, null, msg, pi, null);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			try {
				android.telephony.gsm.SmsManager manager = android.telephony.gsm.SmsManager
						.getDefault();
				manager.sendTextMessage(phone, null, msg, pi, null);
			} catch (Throwable t) {
				t.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static ArrayList<String> divideMessage(String text) {
		try {
			if (isNormalVersion()) {
				return Sms.instance.dividerMessage(text);
			} else {
				android.telephony.gsm.SmsManager manager = android.telephony.gsm.SmsManager
				.getDefault();
				return manager.divideMessage(text);
			}
		} catch(Throwable e) {
			try {
				android.telephony.gsm.SmsManager manager = android.telephony.gsm.SmsManager
				.getDefault();
				return manager.divideMessage(text);
				
			}catch(Throwable t) {
				t.printStackTrace();
				return null;
			}
		}
	}

	public static boolean sendNultipartSms(String phone,
			ArrayList<String> msgs, ArrayList<PendingIntent> pis) {
		try {
			if (isNormalVersion()) {
				Sms.instance.sendNultipartSms(phone, msgs, pis);
			} else {
				android.telephony.gsm.SmsManager manager = android.telephony.gsm.SmsManager
						.getDefault();
				manager.sendMultipartTextMessage(phone, null, msgs, pis, null);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			try {
				android.telephony.gsm.SmsManager manager = android.telephony.gsm.SmsManager
						.getDefault();
				manager.sendMultipartTextMessage(phone, null, msgs, pis, null);
			} catch (Throwable t) {
				t.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private static class Sms {
		public static final Sms instance = new Sms();

		public void sendSms(String phone, String msg, PendingIntent pi) {
			
			android.telephony.SmsManager sm = android.telephony.SmsManager
					.getDefault();
			sm.sendTextMessage(phone, null, msg, pi, null);
		}

		public void sendSms(String phone, String msg)
		{
			Uri uri = Uri.parse("smsto:" + phone);            
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);    
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putExtra("sms_body", msg);   
			try
			{
				if (ContextUtil.getContext() instanceof Activity) {
					((Activity)ContextUtil.getContext()).startActivityForResult(it, Const.RequestCode.SendSms); 
				}
				else {
					ContextUtil.getContext().startActivity(it); 
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public ArrayList<String> dividerMessage(String text) {
			return android.telephony.SmsManager.getDefault()
					.divideMessage(text);
		}

		public void sendNultipartSms(String phone, ArrayList<String> msgs,
				ArrayList<PendingIntent> pi) {
			android.telephony.SmsManager sm = android.telephony.SmsManager
					.getDefault();
			sm.sendMultipartTextMessage(phone, null, msgs, pi, null);
		}
	}
	
	public static boolean isNormalVersion() {
		try {
			 int VERSION = Integer.valueOf(Build.VERSION.SDK);
			 return VERSION > 3;
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
}
