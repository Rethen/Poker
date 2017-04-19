package com.changdupay.protocol.action;

import android.text.TextUtils;

public class ActionFactory {
	private static ActionFactory mActionFactory = null;
	
	//短信action
	public String SMSACTION = "ndaction:sms";
	
	//调用支付渠道SDK action
	public String LAUNCHACTION = "ndaction:launch";
	
	//支付action
	//public String PAYACTION = "ndaction:result";
	
	//支付结果action
	public String RESULTACTION = "ndaction:result";
	
	public static ActionFactory getInstance()
	{
		if (mActionFactory == null)
		{
			mActionFactory = new ActionFactory();;
		}
		
		return mActionFactory;
	}
	
	public ActionFactory()
	{
		
	}
	
	public IAction createAction(String action, String params)
	{
		IAction product = null;
		if (TextUtils.equals(SMSACTION, action))
		{
			product = new SmsAction(params);
		}
		else if(TextUtils.equals(LAUNCHACTION, action))
		{
			//product = new ChannelAction(params);
		}
		else if(TextUtils.equals(RESULTACTION, action))
		{
			product = new ResultAction(params);
		}
		return product;
	}
}
