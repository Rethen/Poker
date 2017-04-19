package com.changdupay.protocol.pay;

import com.changdupay.protocol.base.BaseRequestInfo;

/** 支付结果请求信息
 * 
 * @author Administrator
 *
 */
public class PayResultRequestInfo extends BaseRequestInfo{
	//支付结果信息具体内容
	public PayResultRequestInfo()
	{
		super();
		
		Content = new PayResultContent();
	}
}
