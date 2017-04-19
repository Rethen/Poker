package com.changdupay.app;

import android.os.Bundle;

import com.changdupay.util.Const;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.Channel;

public class iCDPayChooseMoneyVisaActivity extends iCDPayChooseMoneyActivtiy {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected String getPayName()
	{
		return "visacard";
	}
	
	@Override
	protected int getPayCode()
	{
		return Const.PayCode.savingscard;
	}
	

	@Override
	protected String getTopTitle()
	{
		return "ipay_credit_card";
	}
	
	@Override
	protected void doPayAction()
	{
		Channel item = PayConfigReader.getInstance().getPayChannelItemByPayCodeType(getPayCode(), -1);
		if (item != null)
		{
			gotoVisaPay(item.PayType, item.PayId);
		}
	}
	
	protected void gotoVisaPay(int nPayType, int nPayId)
	{
		doRequest(nPayType, nPayId);
	}

}
