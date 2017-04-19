package com.changdupay.protocol.action;

import android.app.Activity;

public class BaseAction implements IAction{
	private String mParams = "";

	public BaseAction(String params)
	{
		mParams = params;
	}
	
	@Override
	public void doAction(Activity activity, Boolean bQuit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] splitParams() {
		if (mParams == null)
		{
			return null;
		}
		
		String[] info = mParams.split(",");
		
		if (info.length == 2)
		{
			return info;
		}
		
		return null;
	}
}
