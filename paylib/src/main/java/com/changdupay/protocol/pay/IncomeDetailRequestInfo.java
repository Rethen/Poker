package com.changdupay.protocol.pay;

import com.changdupay.protocol.base.PayConst;

public class IncomeDetailRequestInfo extends BillDetailRequestInfo {

	public IncomeDetailRequestInfo() {
		super();
		
		ActionID = PayConst.GET_INCOME_ACTION;
		RequestHeader.mActionID = ActionID;
	}
}
