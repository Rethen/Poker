package com.changdupay.util;

import java.io.Serializable;
import java.util.ArrayList;

import com.changdupay.protocol.base.PayConst;

@SuppressWarnings("serial")
public class PayConfigs implements Serializable {
	//新版配置
	//通用配置开始
	//配置文件解析器版本号
	public double ParserVer = 2.0;
	
	//会话有效时长,单位小时
	public int SessionTime = 24;
	
	//小额免密[是否启用,最大金额
	public class AvoidPasswrod 
	{
		public int Enable= 1;
		public double UpperLimit = 200;
	}
	
	//活动页相关配置
	public class ActionConfig
	{
		public int PageSize = 20;
		public int LatelyPageSize = 5;
	}
	
	//客户端版本号,下载地址
	public class ClientConfig
	{
		public double Ver = 1.0;
		public String DownloadUrl = "";
	}
	
	//充值金额配置[默认金额,最小金额,最大金额
	public class RechargeAmount
	{
		public double DefaultAmount = 50;
		public double LowerLimit = 1;
		public double UpperLimit = 50000;
	}
	
	//支付金额配置[默认金额,最小金额,最大金额
	public class PayAmount
	{
		public double DefaultAmount = 50;
		public double LowerLimit = 1;
		public double UpperLimit = 50000;
	}
	
	public class DefaultAmountList
	{
		public ArrayList<AmountLimit> AmountLimits = new ArrayList<AmountLimit>();
	}
	
	//<!--  商品渠道配置开始  -->
	//<!--  服务端会根据AppID下发该应用对应的商品,及商品支付的支付渠道，支付金额  -->
	//<!--  金额配置中的Premium为溢价的真实金额  -->
	//<!--  渠道的顺序服务端会根据后台配置的排序号按顺序下发,客户端按顺序展现  -->
	public class MerchandiseList
	{
		public ArrayList<Merchandise> mlMerchandises = new ArrayList<Merchandise>();
	}
	
	public class Merchandise
	{
		public long Id = PayConst.MERCHANDISEID;
		public String Name= PayConst.MERCHANDISENAME;
		public int Rate= 100;
		
		public ArrayList<ChannelList> mlChannelList = new ArrayList<ChannelList>();
	}
	
	//PayChannel相当于Category
	public class ChannelList 
	{
		public ArrayList<Category> mlCategoryList = new ArrayList<Category>();
	}
	
	public class Category
	{
		public String Name = "";
		public int Code = 1;
		public int ViewType = 1;
		public int IconType = 0;
		public ArrayList<Channel> Channels = new ArrayList<Channel>();
		
		public String ResKey = "";
		public int    SortId = Const.SORT_ID;
	}
	
	public class Channel implements Serializable 
	{
		public String Name = "";
		public String Descript = "";
		public int PayType = -1;
		public int PayId = -1;
		public int ViewType = 1;
		public int AmountLimit = 0;
		public ArrayList<AmountLimit> AmountLimits = new ArrayList<AmountLimit>();
		public ArrayList<String> MobileTypeList = new ArrayList<String>();
		public Boolean Tokened = false;
		public String ResKey = "";
	}
	
	public class AmountLimit implements Serializable
	{
		public int Value = 0;
		public int Premium = 1;
		public String chargeMessage;
		public int giftType = 0;
		public String Text = "";
		public String ShopItemId = "";
	}
	
//	//运营商号段描述
//	public class TMobileItem
//	{
//		public int SimOperator = Const.SimOperator.Mobile1;
//		public ArrayList<String> mlValue = new ArrayList<String>();
//	}
	
//	//运营商号段描述
//	public class TMobile
//	{
//		public ArrayList<TMobileItem> Items = new ArrayList<TMobileItem>();
//	}
	
	public String DynamicKey = "";
	
	public String LocalKey = "";
}
