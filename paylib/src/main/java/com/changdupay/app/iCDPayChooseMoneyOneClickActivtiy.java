package com.changdupay.app;

import android.os.Bundle;

import com.changdupay.util.ChannelPayType;
import com.changdupay.util.Const;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Channel;

public class iCDPayChooseMoneyOneClickActivtiy extends iCDPayChooseMoneyActivtiy{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected String getPayName()
	{
		return "oneclick";
	}
	
	@Override
	protected int getPayCode()
	{
		return Const.PayCode.oneclick;
	}
	
	@Override
	protected String getTopTitle()
	{
		return "ipay_tenpay";
	}
	
	protected Channel getPayChannel()
	{	
//		ChannelPayType payType = Const.ChannelType.oneclickwap;
//		if (payType == null)
//			return null;
//		
//		return PayConfigReader.getInstance().getPayChannelItemPairs(payType.PayType, payType.PayId);
		Channel item = PayConfigReader.getInstance().getPayChannelItemByPayCodeType(getPayCode(), -1);
		return item;
	}
	
	@Override
	protected void doPayAction()
	{
		gotoOneClickPay();
	}
	
	private void gotoOneClickPay()
	{
//		ChannelPayType payType = Const.ChannelType.oneclickwap;
		Channel item = PayConfigReader.getInstance().getPayChannelItemByPayCodeType(getPayCode(), -1);
		doRequest(item.PayType, item.PayId);
	}
}
