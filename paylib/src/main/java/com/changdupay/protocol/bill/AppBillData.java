package com.changdupay.protocol.bill;

import java.io.Serializable;
import java.util.List;

public class AppBillData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10002L;
	
	public List<MonthBillData> mMonthBillDatas;
}
