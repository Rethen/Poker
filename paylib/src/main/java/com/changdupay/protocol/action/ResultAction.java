package com.changdupay.protocol.action;

import android.app.Activity;

import com.changdupay.encrypt.Base64;
import com.changdupay.util.Utils;

public class ResultAction extends BaseAction{	
	public ResultAction(String params)
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
		if (info[0].equals("true")) {
			Utils.gotoPaySuccessActivity(payinfo, activity);
		}
		else {
			Utils.gotoPayFailedActivity(payinfo, activity);
		}
	}
}
