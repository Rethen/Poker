package com.changdupay.protocol;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProtocolData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static ProtocolData protocolData;
	
	public static ProtocolData getInstance() {
		if (protocolData == null) {
			protocolData = new ProtocolData();
		}
		return protocolData;
	}

	public class BaseProtocalData {
		public boolean result;
		public String errorMsg;
		public String ActionID;	
		public String ApplicationID;
		public long NextUpdateTimeSpan;
	}
	
	public class UserAccountEntity extends BaseProtocalData {
		// 用户ID 
		public long UserID;
		// 用户名 
		public String UserName;
		// 登录令牌 
		public String LoginToken;
		// 用户星级 
		public int CurrentVipLevel;
		// 用户帐户总余额 
		public double AccountTotalMoney;
		// 用户小额免密状态 
		public int AvoidPasswordStatus;
		// 用户小额免密额度 
		public double AvoidPasswordMoney;
		//支付密码设置状态
		public int PayPasswordStatus;
		//开户状态 
		public int OpenAccountStatus;
		//支付信息
		public String PayInfo;
	}

	public class StoreEntity extends BaseProtocalData {
		// 用户ID 
		public long UserID;
		// 用户星级 
		public int StarLevel;
		// 小额免密状态
		public int Status;
		// 小额免密金额
		public double Money;
		// 账户余额
		public double AccountBalance;
	}
	
	//支付
	public class PayEntity extends BaseProtocalData {
		// 用户ID 
		public long UserID;
		//执行方式 
		public int ExecType;
		// 跳转支付URL
		public String JumpUrl;
		// 启动包名 
		public String PackageName;
		// 启动参数
		public String Parameter;
		// 短信接收者
		public String Receiver;
		// 短信内容
		public String Message;
		//用户星级
		public int StarLevel = 0;
		//账户余额 
		public double AccountBalance = 0.00;
		//校验地址（悦读纪移动专用）
		public String VerifyUrl = "";
		//订单编号（悦读纪移动专用）
		public String OrderId = "";
		//订单号
		public String OrderSerial = "";
		//订单提交时间
		public String StartDateTime = "";
	}
	
	//充值
	public class RechargeEntity extends PayEntity {
		
	}
	
	//账单
	public class OrderEntityList extends BaseProtocalData {
		// 日期 
		public String OrdersDate;
		// 用户ID 
		public long UserID;
		// 总收入
		public double TotalInMoney;
		// 总支出
		public double TotalOutMoney;
		// 订单列表 
		public List<OrderEntity> Orders;
		// 订单总数
		public long RecordCount;
		
		public OrderEntityList() {
			
		}
	}
	
	//订单详情
	public class OrderEntity extends BaseProtocalData implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
		
		public OrderEntity() {
			
		}
	}
		
	//收入订单
	public class StoreOrderEntityList extends BaseProtocalData {
		// 日期 
		public String OrdersDate;
		// 用户ID 
		public long UserID;
		// 总收入
		public double TotalInMoney;
		// 总支出
		public double TotalOutMoney;
		// 订单列表 
		public List<StoreOrderEntity> Orders;
		// 订单总数
		public long RecordCount;
	}
	   	
	//订单详情
	public class StoreOrderEntity extends BaseProtocalData implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// 应用名称
		public String AppName;
		// 商品名称
		public String MerchandiseName;
		// 金额
		public double InMoney;
		// 订单状态 
		public int OrderStatus;
		// 订单号
		public String MainConsumptionSerial;
		// 创建时间
		public String ConsumeDate;
	}
	
	public class PushServiceEntity extends BaseProtocalData{
		//活动编号
		public long ID;
		//活动简介
		public String Title; 
		//内容
		public String Content; 
		//图标地址
		public String IcoLink; 
		//活动地址
		public String Href; 
		//活动时间
		public String Time; 
		//请求方式
		public String Action; 
	}
	
	public class PushServiceListEntity extends BaseProtocalData {
		//总数
		public int RecordCount;
		//推送列表
		public ArrayList<PushServiceEntity> PushServiceList = new ArrayList<PushServiceEntity>();
	}
	
	//数月订单列表
	public class MonthsOrderEntityList extends BaseProtocalData {
		// 数月 
		public int Months;
		// 用户ID 
		public long UserID;
		// 订单列表 
		public List<OrderEntityList> OrderEntityLists;
		// 订单总数
		public long RecordCount;
	} 
	
	//用户信息：用户头像+用户等级图片等用户信息
	public class ChangduReaderUserInfo extends BaseProtocalData {
		//用户头像url
		public String userImgSrc;
		//用户等级图片url
		public String userLevelSrc;
		//用户畅读币
		public long userChangduCoin;
		//用户礼券
		public int userChangduGiftCoin;
	}
}
