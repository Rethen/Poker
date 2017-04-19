package com.changdupay.protocol.bill;

import java.io.Serializable;

import com.changdupay.protocol.ProtocolData.OrderEntity;

public class BillData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 100001L;
		
	public boolean result;
	public String errorMsg;
	public String ActionID;	
	public String ApplicationID;
	public long NextUpdateTimeSpan;
	// 应用名称
	public String AppName;
	// 商品名称
	public String MerchandiseName;
	// 金额
	public double OrderMoney;
	// 订单状态 
	public int OrderStatus;
	// 订单号
	public String OrderSerial;
	// 创建时间
	public String StartDateTime;
	// 发货时间 
	public String CooperatorDateTime;
	// 应用ID
	public long AppID;
	
	public void copy(OrderEntity orderEntity) {
		result 				= orderEntity.result;
		errorMsg 			= orderEntity.errorMsg;
		ActionID 			= orderEntity.ActionID;
		ApplicationID 		= orderEntity.ApplicationID;
		NextUpdateTimeSpan 	= orderEntity.NextUpdateTimeSpan;
		AppName 			= orderEntity.AppName;
		MerchandiseName 	= orderEntity.MerchandiseName;
		OrderMoney 			= orderEntity.OrderMoney;
		OrderStatus 		= orderEntity.OrderStatus;
		OrderSerial 		= orderEntity.OrderSerial;
		StartDateTime 		= orderEntity.StartDateTime;
		CooperatorDateTime 	= orderEntity.CooperatorDateTime;
		AppID				= orderEntity.AppID;
	}
}
