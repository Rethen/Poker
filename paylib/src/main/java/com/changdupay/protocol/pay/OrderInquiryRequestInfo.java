package com.changdupay.protocol.pay;

import com.changdupay.protocol.base.BaseRequestInfo;

/** 订单查询请求信息
 * 
 * @author Administrator
 *
 */
public class OrderInquiryRequestInfo extends BaseRequestInfo{
	public OrderInquiryRequestInfo()
	{
		super();
		
		Content = new OrderInquiryContent();
	}
}
