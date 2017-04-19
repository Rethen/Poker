package com.changdupay.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.changdupay.protocol.base.PayConst;
import com.changdupay.util.Const;
import com.changdupay.util.Deviceinfo;
import com.changdupay.util.MResource;
import com.changdupay.util.PayConfigReader;
import com.changdupay.util.PayConfigs.AmountLimit;
import com.changdupay.util.PayConfigs.Channel;
import com.changdupay.util.PayConfigs.Merchandise;
import com.changdupay.util.Utils;
import com.changdupay.widget.IAdapterDataWrap;

public class iCDPayChooseMoneyBaseActivity extends BaseActivity implements IAdapterDataWrap{
	protected Channel mPayChannel = null;
	protected int mViewType = -1;
	protected ChoosePayTypeDataWrapper mChoosePayTypeDataWrapper = null;
	protected iCDPayChooseMoneyAdapter mPayChooseMoneyAdapter = null;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewType = getIntent().getIntExtra(Const.ParamType.TypePayViewType, -1);
		initData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
	
	private void initData()
	{
		if(mPayChannel == null){
			mPayChannel = getPayChannel();
		}
	}
	
	protected String getPayName()
	{
		return null;
	}
	
	protected int getPayCode()
	{
		return -1;
	}
	
	protected int getPayType()
	{
		return -1;
	}

	protected Channel getPayChannel()
	{
		int payCode = getPayCode();
		if (payCode == -1)
			return null;
		
		int payType = getPayType();
		
		return PayConfigReader.getInstance().getPayChannelItemByPayCodeType(payCode, payType);
	}
	
	protected String getInitialSelectedMoney()
	{
		int payType = getPayType();
		if (payType <= 0)
		{
			return null;
		}
		
		String value = Utils.getStringValue(payType + "");
		if (value == null || TextUtils.equals(value, ""))
			return null;
		
		return value;
	}
	
	protected void storeSelectedMoney(String payMoney)
	{
		int payType = getPayType();
		if (payType <= 0)
		{
			return;
		}
		
		Utils.setStringValue(payType+"", payMoney);
	}
	
	protected void setInitialSelectedItem()
	{
		String money = getInitialSelectedMoney();
		if (money != null)
		{
			int index = getIndexByMoney(money);
			mPayChooseMoneyAdapter.setInitialSelectedItem(index);
		}
		else
		{
			mPayChooseMoneyAdapter.setInitialSelectedItem(0);
		}
	}
	
	protected int getInitialSelectedIndex()
	{
		String money = getInitialSelectedMoney();
		if (money != null)
		{
			int index = getIndexByMoney(money);
			return index;
		}
		else
		{
			return 0;
		}
	}
	
	protected int setSelectedByMoney(String payMoney)
	{
		int index = -1;
		if (payMoney == null || TextUtils.isEmpty(payMoney))
		{
			mPayChooseMoneyAdapter.setOtherItemsUnSelected(0);
			return index;
		}
		index = getIndexByMoney(payMoney);
		mPayChooseMoneyAdapter.setOtherItemsUnSelected(index);
		if (index != -1)
		{
			mPayChooseMoneyAdapter.setItemSelected(index);
		}
		
		return index;
	}
	
	protected int setSelectedByIndex(int index)
	{
		if(index == -1)
		{
			return 0;
		}
		
		mPayChooseMoneyAdapter.setOtherItemsUnSelected(index);
		if (index != -1)
		{
			mPayChooseMoneyAdapter.setItemSelected(index);
		}
		
		return index;
	}
	
	protected int getIndexByMoney(String payMoney)
	{
		int index = -1;
		Channel payChannel = getPayChannel();
		if(payChannel == null){
			Log.e("getIndexByMoney", "payChannel null");
			return index;
		}
		
		for (int i = 0; i < payChannel.AmountLimits.size(); i++)
		{
			try{
				if (Double.parseDouble(payMoney) == payChannel.AmountLimits.get(i).Value)
				{
					index = i;
					
					break;
				}
			}catch(NumberFormatException e){
				e.printStackTrace();				
			}
		}
		
		return index;
	}
	
	protected String getShopItemIdByMoney(String payMoney)
	{
		Channel payChannel = getPayChannel();
		if(payChannel == null){
			Log.e("getIndexByMoney", "payChannel null");
			return "";
		}
		String shopItemId = "";
		for (int i = 0; i < payChannel.AmountLimits.size(); i++)
		{
			try{
				if (Double.parseDouble(payMoney) == payChannel.AmountLimits.get(i).Value)
				{
	
					shopItemId = payChannel.AmountLimits.get(i).ShopItemId;
					break;
				}
			}catch(NumberFormatException e){
				e.printStackTrace();				
			}
		}
		
		return shopItemId;
	}
	
	protected String getShopItemIdByIndex(int index, String money)
	{
		Channel payChannel = getPayChannel();
		if(payChannel == null){
			Log.e("getIndexByMoney", "payChannel null");
			return "";
		}
		String shopItemId = "";
		for (int i = 0; i < payChannel.AmountLimits.size(); i++)
		{
			try{
				if (i == index && Double.parseDouble(money) == payChannel.AmountLimits.get(i).Value)
				{
	
					shopItemId = payChannel.AmountLimits.get(i).ShopItemId;
					break;
				}
			}catch(NumberFormatException e){
				e.printStackTrace();				
			}
		}
		
		return shopItemId;
	}
	
	protected String getPhoneNumber()
	{
		return Deviceinfo.getPhoneNumber();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mPayChannel == null || mPayChannel.AmountLimits == null) {
			return 0;
		}
		return mPayChannel.AmountLimits.size();
	}

	@Override
	public ChoosePayTypeDataWrapper getData(int position) {
		// TODO Auto-generated method stub
		if (mPayChannel == null || mPayChannel.AmountLimits == null) {
			return null;
		}
		if (mChoosePayTypeDataWrapper == null)
		{
			mChoosePayTypeDataWrapper = new ChoosePayTypeDataWrapper();
		}
		AmountLimit payChannelItem = mPayChannel.AmountLimits.get(position);
		Merchandise merchandise = PayConfigReader.getInstance().getMerchandiseById(PayConst.MERCHANDISEID);
		int nMount = mPayChannel.AmountLimit == 0 ? payChannelItem.Value * merchandise.Rate : payChannelItem.Premium * merchandise.Rate;
		String name = String.format(getString(MResource.getIdByName(getApplication(), "string", "ipay_input_money_ratio_btn")), payChannelItem.Value, nMount);
		if(TextUtils.isEmpty(payChannelItem.Text)){
			mChoosePayTypeDataWrapper.Name = name;
		}else{
			mChoosePayTypeDataWrapper.Name = payChannelItem.Text;			
		}
		mChoosePayTypeDataWrapper.chargeMessage = payChannelItem.chargeMessage;
		mChoosePayTypeDataWrapper.giftType = payChannelItem.giftType;
		return mChoosePayTypeDataWrapper;
	}

	@Override
	public Object getObject(int position) {
		// TODO Auto-generated method stub
		if (mPayChannel == null || mPayChannel.AmountLimits == null
				|| position >= mPayChannel.AmountLimits.size()) {
			return null;
		}
		return mPayChannel.AmountLimits.get(position);
	}
	
}
