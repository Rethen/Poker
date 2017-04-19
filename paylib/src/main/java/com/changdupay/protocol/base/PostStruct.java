package com.changdupay.protocol.base;

import android.content.Context;

public class PostStruct {
	public Integer mActionID = 0;
	public Object mData = 0;
	public Context mContext = null;
	public Object mTag = null;
	public int errorCode = 0;
	
	public PostStruct(Integer actionID, Object data)
	{
		mActionID = actionID;
		mData = data;
	}
}
