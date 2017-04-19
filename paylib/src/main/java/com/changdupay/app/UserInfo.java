package com.changdupay.app;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.changdupay.util.ICallback;

public class UserInfo implements ICallback{
	private static UserInfo mUserInfo = null;
	
	public String mUserID = "";
	public String mUserName = "";
	public String mLoginToken = "";
	public int mCurrentVipLevel = 0;
	public double mAccountTotalMoney = 0;
	//小额免密是否开启，0-未开启小额免密支付
	public int mAvoidPasswordStatus = 0;
	public double mAvoidPasswordMoney = 0;
	//支付密码是否设置，0-未设置支付密码
	public int mPayPasswordStatus = 0;
	//小额支付开户状态，0-未开户
	public int mOpenAccountStatus = 0;
	//充值标志
	public Boolean RechargeFlag = false;
	
	public ArrayList<SimPayChannelItem> mSimPayChannels;
	
	public class SimPayChannelItem
	{
		public int PayType;
		public int PayId;
		
		public SimPayChannelItem(int nPayType, int nPayId)
		{
			PayType = nPayType;
			PayId = nPayId;
		}
	}
	
	public static UserInfo getInstance()
	{
		if (mUserInfo == null)
		{
			mUserInfo = new UserInfo();
		}
		
		return mUserInfo;
	}
	
	public UserInfo()
	{
		mSimPayChannels = new ArrayList<SimPayChannelItem>();
	}

	@Override
	public void onFinish(Bitmap bitmap, int tag) {
		// TODO Auto-generated method stub
		
	}
	
	public void addPayChannel(int nPayType, int nPayId)
	{
		mSimPayChannels.add(new SimPayChannelItem(nPayType, nPayId));
	}
}
