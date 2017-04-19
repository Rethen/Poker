package com.changdupay.protocol.pay;

import com.changdupay.protocol.base.PayConst;

public class IncomeRequestInfo extends BillRequestInfo {

	public IncomeRequestInfo() {
		super();

		ActionID = PayConst.GET_INCOME_ACTION;
		RequestHeader.mActionID = ActionID;
	}
}
