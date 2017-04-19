package com.changdupay.protocol.action;

import android.app.Activity;
import android.text.TextUtils;

import com.changdupay.encrypt.Base64;
import com.changdupay.util.Utils;

public class PayAction  extends BaseAction{
	public PayAction(String params)
	{
		super(params);
	}
	
	public void doAction(Activity activity, Boolean bQuit)
	{
		String[] info = splitParams();
		if (info == null)
		{
			return;
		}
		
		String payinfo = new String(Base64.decode(info[1]));
		//调用支付成功页面
		if (TextUtils.equals("true", info[0]))
		{
			Utils.gotoPaySuccessActivity(payinfo, activity);
		} 
		//调用支付失败页面
		else if (TextUtils.equals("false", info[0]))
		{
			Utils.gotoPayFailedActivity(payinfo, activity);
		}
	}
}
