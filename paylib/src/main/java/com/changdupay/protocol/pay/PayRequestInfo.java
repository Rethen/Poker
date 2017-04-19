package com.changdupay.protocol.pay;

import com.changdupay.protocol.base.BaseRequestInfo;
import com.changdupay.protocol.base.PayConst;

/** 支付信息
 * 
 * @author Administrator
 *
 */
public class PayRequestInfo extends BaseRequestInfo{
	public PayRequestInfo()
	{
		super();
		
		Content = new RequestContent(); 
		ActionID = PayConst.PAY_ACTION;
		RequestHeader.mActionID = PayConst.PAY_ACTION;
	}
}
