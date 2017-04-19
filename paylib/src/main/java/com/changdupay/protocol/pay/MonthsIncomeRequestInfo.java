package com.changdupay.protocol.pay;

import com.changdupay.protocol.base.PayConst;

public class MonthsIncomeRequestInfo extends MonthsOutlayRequestInfo {

	public MonthsIncomeRequestInfo()
	{
		super();
		
		Content = new MonthsOrdersRequestContent();
		ActionID = PayConst.GET_MONTHS_INCOME_ACTION;
		RequestHeader.mActionID = ActionID;
	}
}
