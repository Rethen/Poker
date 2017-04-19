package com.changdupay.protocol.bill;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MonthBillData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 100000L;

	public String mDateString;
	public Date mDate;//时间
	public float mIncome;//收入
	public float mOutlay;//支出
	public List<BillData> mBillDatas;//详细账单列表
	
}
