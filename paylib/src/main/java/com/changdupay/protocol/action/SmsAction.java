package com.changdupay.protocol.action;

import com.changdupay.encrypt.Base64;
import com.changdupay.encrypt.SmsUtils;

public class SmsAction extends BaseAction{
	
	public SmsAction(String params)
	{
		super(params);
	}
	
	public void doAction()
	{
		String[] info = splitParams();
		if (info != null)
		{
			String msg = Base64.decode(info[1]).toString();
			SmsUtils.sendMsg(msg, info[0]);
		}
	}
}
