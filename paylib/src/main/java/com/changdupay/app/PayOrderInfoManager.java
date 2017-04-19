package com.changdupay.app;
import android.os.Bundle;

public class PayOrderInfoManager {
	private static PayOrderInfoManager mPayOrderInfoManager = null;
	public iCDPayOrderInfo mPayOrderInfo = null;
	
	//��ƷID
	public String MerchantID = "MerchantID";
	
	//��ƷID
	public String MerchandiseID = "MerchandiseID";
	
	//��Ʒ���
	public String MerchandiseName = "MerchandiseName";

	//�û�ID
	public String UserID =  "UserID";
	
	//�û���
	public String UserName = "UserName";
	
	//�û��ǳ�
	public String NickName = "NickName";
	
	//��¼token
	public String LoginToken = "LoginToken";
	
	//��è��
	public String ChangduCoins = "ChangduCoins";
	
	//���
	public String GiftCoins = "GiftCoins";
	
	//VIP URL
	public String UserVipLevelBitmap = "UserVipLevelBitmap";
	
	//ͷ��URL
	public String UserHeadImgBitmap = "UserHeadImgBitmap";

	//�����û���Ϣ��url
	public String RequestUserInfoUrl = "RequestUserInfoUrl";
	
	//������ɫ����
	public String ColorMatrix = "String";
	

	
	private PayOrderInfoManager()
	{
		if (mPayOrderInfo == null)
		{
			mPayOrderInfo = new iCDPayOrderInfo();
		}
	}
	
	public static PayOrderInfoManager getPayOrderInfoManager()
	{
		if (mPayOrderInfoManager == null)
		{
			mPayOrderInfoManager = new PayOrderInfoManager();
		}
		
		return mPayOrderInfoManager;
	}
	
	public iCDPayOrderInfo getPayOrderInfo()
	{
		if (mPayOrderInfo == null)
		{
			mPayOrderInfo = new iCDPayOrderInfo();
		}
		else
		{
			mPayOrderInfo.CooperatorOrderSerial = java.util.UUID.randomUUID().toString();
		}
		return mPayOrderInfo;
	}
	
	public void saveInstanceState(Bundle outState)
	{
		if (outState != null)
		{
			PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
			outState.putLong(MerchantID, mPayOrderInfo.MerchantID);
			outState.putLong(MerchandiseID, mPayOrderInfo.MerchandiseID);
			outState.putString(MerchandiseName, mPayOrderInfo.MerchandiseName);
			outState.putLong(UserID, mPayOrderInfo.UserID);
			outState.putString(UserName, mPayOrderInfo.UserName);
			outState.putString(NickName, mPayOrderInfo.NickName);
			outState.putString(LoginToken, mPayOrderInfo.LoginToken);
			outState.putLong(ChangduCoins, mPayOrderInfo.ChangduCoins);
			outState.putLong(GiftCoins, mPayOrderInfo.GiftCoins);
			outState.putString(RequestUserInfoUrl, mPayOrderInfo.RequestUserInfoUrl);
			outState.putParcelable(UserVipLevelBitmap, mPayOrderInfo.UserVipLevelBitmap);
			outState.putParcelable(UserHeadImgBitmap, mPayOrderInfo.UserHeadImgBitmap);
			outState.putFloatArray(ColorMatrix, mPayOrderInfo.ColorMatrix);
		}
	}
	
	public void restoreInstanceState(Bundle savedInstanceState)
	{
		if (savedInstanceState != null)
		{
			PayOrderInfoManager.getPayOrderInfoManager().getPayOrderInfo();
			mPayOrderInfo.MerchantID = savedInstanceState.getLong(MerchantID);
			mPayOrderInfo.MerchandiseID = savedInstanceState.getLong(MerchandiseID);
			mPayOrderInfo.MerchandiseName = savedInstanceState.getString(MerchandiseName);
			mPayOrderInfo.UserID = savedInstanceState.getLong(UserID);
			mPayOrderInfo.UserName = savedInstanceState.getString(UserName);
			mPayOrderInfo.NickName = savedInstanceState.getString(NickName);
			mPayOrderInfo.LoginToken = savedInstanceState.getString(LoginToken);
			mPayOrderInfo.ChangduCoins = savedInstanceState.getLong(ChangduCoins);
			mPayOrderInfo.GiftCoins = savedInstanceState.getLong(GiftCoins);
			mPayOrderInfo.RequestUserInfoUrl = savedInstanceState.getString(RequestUserInfoUrl);
			mPayOrderInfo.UserVipLevelBitmap = savedInstanceState.getParcelable(UserVipLevelBitmap);
			mPayOrderInfo.UserHeadImgBitmap = savedInstanceState.getParcelable(UserHeadImgBitmap);
			mPayOrderInfo.ColorMatrix = savedInstanceState.getFloatArray(ColorMatrix);
		}
	}

	/**
	 * 刷新最新畅读币数据后保存
	 * <br>Created 2014年8月6日 上午11:50:05
	 * @param changduCoid
	 * @param giftCoin
	 * @author       daiyf
	*/
	public void setPayOrderCoinInfo(int changduCoid, int giftCoin) {
		if (mPayOrderInfo != null)
		{
			mPayOrderInfo.ChangduCoins = changduCoid;
			mPayOrderInfo.GiftCoins = giftCoin;
		}
	}
}
