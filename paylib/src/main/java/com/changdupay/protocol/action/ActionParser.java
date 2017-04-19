package com.changdupay.protocol.action;

import java.net.URLDecoder;

import android.text.TextUtils;

public class ActionParser {
	public String mAction = "";
	private String ACTIONNAME = "ndaction:";

	public ActionParser()
	{
		
	}
	
	public void parser(String strAction)
	{
		if (!isAction(strAction))
		{
			return;
		}
		try
		{
			mAction = URLDecoder.decode(strAction, "utf-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getAction()
	{
		try
		{
			String action = null;
			int idx = mAction.indexOf('(');
			if (idx > 0) {
				action = mAction.substring(0, idx);
			}
			
			return action;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String getParams()
	{
		String params = null;
		try
		{
			int idx = mAction.indexOf('(');
			int idx2 = mAction.indexOf(')');
			if (idx > 0 && idx2 > idx + 1) {
				params = mAction.substring(idx + 1, idx2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return params;
	}
	
	private Boolean isAction(String strAction)
	{
		String actionname = strAction.substring(0, ACTIONNAME.length());
		if (TextUtils.equals(ACTIONNAME, actionname))
		{
			return true;
		}
		
		return false;
	}
	
}
