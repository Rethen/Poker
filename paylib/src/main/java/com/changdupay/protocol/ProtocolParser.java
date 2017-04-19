package com.changdupay.protocol;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.changdupay.app.UserInfo;
import com.changdupay.net.netengine.NetReader;
import com.changdupay.util.PayConfigReader;

public class ProtocolParser {
		
	public static ProtocolData.BaseProtocalData getDefaultData(NetReader reader) {
		ProtocolData ProtocolData = new ProtocolData();
		ProtocolData.BaseProtocalData data = ProtocolData.getInstance().new BaseProtocalData();
		data.result = reader.getResult();
		data.errorMsg = reader.getErrorMsg();
		data.ActionID = "" + reader.getActionId();
		return data;
	}

	public static ProtocolData.BaseProtocalData parseProtocal_ChangduReader(NetReader reader) {
		if (reader.headCheck_ChangduReader()) {
			switch (reader.getActionId()) {
			case 1017:
				return parseProtocol_1017(reader);
			default:
				break;
			}
		}
	
		return getDefaultData(reader);
	}	

	/*
	*获取用户信息
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_1017(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }				
	    
	    if (reader.readInt() != 0) {
			reader.recordBegin();
			
			reader.readString();//ActionId
			reader.readString();//applicationId
			reader.readInt();//nextupdatetimespan
	
	        ProtocolData.ChangduReaderUserInfo m_UserInfo = ProtocolData.getInstance().new ChangduReaderUserInfo();
	        m_UserInfo.userImgSrc = reader.readString();
	        m_UserInfo.userLevelSrc = reader.readString();							
	        m_UserInfo.userChangduCoin = reader.readInt64();
	        m_UserInfo.userChangduGiftCoin = reader.readInt();
			
			reader.recordEnd();
	        return m_UserInfo;
		}
	    
	    return null;
	}
	
	
	public static ProtocolData.BaseProtocalData parseProtocal(NetReader reader) {
		if (reader.headCheck()) {
			switch (reader.getActionId()) {
			case 110001:
				return parseProtocol_110001(reader);
			case 110002:
				return parseProtocol_110002(reader);
			case 110010:
				return parseProtocol_110010(reader);
			case 120001:
				return parseProtocol_120001(reader);
			case 120002:
				return parseProtocol_120002(reader);
			case 120005:
				return parseProtocol_120005(reader);
			case 120006:
				return parseProtocol_120006(reader);
			case 120007:
				return parseProtocol_120007(reader);				
			case 120008:
				return parseProtocol_120008(reader);		
			case 120009:
				return parseProtocol_120009(reader);	
			case 120010:
				return parseProtocol_120010(reader);
			case 120011:
				return parseProtocol_120011(reader);
			case 120012:
				return parseProtocol_120012(reader);
			case 120013:
				return parseProtocol_120013(reader);
			case 120014:
				return parseProtocol_120014(reader);
			case 120015:
				return parseProtocol_120015(reader);		
			case 130001:
				return parseProtocol_130001(reader);
			default:
				break;
			}
		}
	
		return getDefaultData(reader);
	}
	
	/*
	*登录
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_110001(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.UserAccountEntity m_UserAccountEntity = ProtocolData.getInstance().new UserAccountEntity();
	        reader.recordBegin();
	        m_UserAccountEntity.UserID = reader.readInt64();
	        m_UserAccountEntity.UserName = reader.readString();
	        m_UserAccountEntity.LoginToken = reader.readString();
	        m_UserAccountEntity.CurrentVipLevel = reader.readInt();
	        m_UserAccountEntity.AccountTotalMoney = reader.readDouble();
	        m_UserAccountEntity.AvoidPasswordStatus = reader.readInt();
	        m_UserAccountEntity.AvoidPasswordMoney = reader.readDouble();
	        m_UserAccountEntity.PayPasswordStatus = reader.readInt();
	        m_UserAccountEntity.OpenAccountStatus = reader.readInt();
	        m_UserAccountEntity.PayInfo = reader.readString();
	        try {
	        	JSONArray  jSONContent = new JSONArray(m_UserAccountEntity.PayInfo);
	        	if (jSONContent != null){
	        		for (int i = 0; i < jSONContent.length(); i++){
		        		JSONObject object = jSONContent.getJSONObject(i);
		        		int paytype = object.getInt("PayType");
		        		int payid = object.getInt("PayID");
		        		PayConfigReader.getInstance().tokenPayChannelItemPairs(paytype, payid);
		        	}
	        	}
	        	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        reader.recordEnd();
	        return m_UserAccountEntity;
	    }
	    return null;
	}
	
	/*
	*令牌登录110002
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_110002(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.UserAccountEntity m_UserAccountEntity = ProtocolData.getInstance().new UserAccountEntity();
	        reader.recordBegin();
	        m_UserAccountEntity.UserID = reader.readInt64();
	        m_UserAccountEntity.UserName = reader.readString();
	        m_UserAccountEntity.LoginToken = reader.readString();
	        m_UserAccountEntity.CurrentVipLevel = reader.readInt();
	        m_UserAccountEntity.AccountTotalMoney = reader.readDouble();
	        m_UserAccountEntity.AvoidPasswordStatus = reader.readInt();
	        m_UserAccountEntity.AvoidPasswordMoney = reader.readDouble();
	        m_UserAccountEntity.PayPasswordStatus = reader.readInt();
	        m_UserAccountEntity.OpenAccountStatus = reader.readInt();
	        m_UserAccountEntity.PayInfo = reader.readString();
	        try {
	        	JSONArray  jSONContent = new JSONArray(m_UserAccountEntity.PayInfo);
	        	if (jSONContent != null){
	        		for (int i = 0; i < jSONContent.length(); i++){
		        		JSONObject object = jSONContent.getJSONObject(i);
		        		int paytype = object.getInt("PayType");
		        		int payid = object.getInt("PayID");
		        		PayConfigReader.getInstance().tokenPayChannelItemPairs(paytype, payid);
		        	}
	        	}
	        	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        reader.recordEnd();
	        return m_UserAccountEntity;
	    }
	    return null;
	}
	
	/*
	*加载帐户信息
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_110010(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.UserAccountEntity m_UserAccountEntity = ProtocolData.getInstance().new UserAccountEntity();
	        reader.recordBegin();
	        m_UserAccountEntity.UserID = reader.readInt64();
	        m_UserAccountEntity.UserName = reader.readString();
	        m_UserAccountEntity.LoginToken = reader.readString();
	        m_UserAccountEntity.CurrentVipLevel = reader.readInt();
	        m_UserAccountEntity.AccountTotalMoney = reader.readDouble();
	        m_UserAccountEntity.AvoidPasswordStatus = reader.readInt();
	        m_UserAccountEntity.AvoidPasswordMoney = reader.readDouble();
	        m_UserAccountEntity.PayPasswordStatus = reader.readInt();
	        m_UserAccountEntity.OpenAccountStatus = reader.readInt();
	        reader.recordEnd();
	        return m_UserAccountEntity;
	    }
	    return null;
	}
	
	/*
	*获取用户星级
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120001(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.StoreEntity m_StoreEntity = ProtocolData.getInstance().new StoreEntity();
	        reader.recordBegin();
	        m_StoreEntity.UserID = reader.readInt64();
	        m_StoreEntity.StarLevel = reader.readInt();
	        m_StoreEntity.AccountBalance = reader.readDouble();
	        m_StoreEntity.Status = reader.readInt();
	        m_StoreEntity.Money = reader.readDouble();
	        reader.recordEnd();
	        return m_StoreEntity;
	    }
	    return null;
	}	
	
	/*
	*获取账号余额
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120002(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.StoreEntity m_StoreEntity = ProtocolData.getInstance().new StoreEntity();
	        reader.recordBegin();
	        m_StoreEntity.UserID = reader.readInt64();
	        m_StoreEntity.StarLevel = reader.readInt();
	        m_StoreEntity.AccountBalance = reader.readDouble();
	        m_StoreEntity.Status = reader.readInt();
	        m_StoreEntity.Money = reader.readDouble();
	        reader.recordEnd();
	        return m_StoreEntity;
	    }
	    return null;
	}
	
	/*
	*获取小额支付免密状态
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120005(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.StoreEntity m_StoreEntity = ProtocolData.getInstance().new StoreEntity();
	        reader.recordBegin();
	        m_StoreEntity.UserID = reader.readInt64();
	        m_StoreEntity.StarLevel = reader.readInt();
	        m_StoreEntity.AccountBalance = reader.readDouble();
	        m_StoreEntity.Status = reader.readInt();
	        m_StoreEntity.Money = reader.readDouble();
	        reader.recordEnd();
	        return m_StoreEntity;
	    }
	    return null;
	}
	
	/*
	*创建订单
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120006(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.PayEntity m_PayEntity = ProtocolData.getInstance().new PayEntity();
	        reader.recordBegin();
	        m_PayEntity.UserID = reader.readInt64();
	        m_PayEntity.ExecType = reader.readInt();
	        m_PayEntity.JumpUrl = reader.readString();
	        m_PayEntity.PackageName = reader.readString();
	        m_PayEntity.Parameter = reader.readString();
	        m_PayEntity.Receiver = reader.readString();
	        m_PayEntity.Message = reader.readString();
	        m_PayEntity.StarLevel = reader.readInt();
	        m_PayEntity.AccountBalance = reader.readDouble();
	        m_PayEntity.VerifyUrl = reader.readString();
	        m_PayEntity.OrderId = reader.readString();
	        m_PayEntity.OrderSerial = reader.readString();
	        m_PayEntity.StartDateTime = reader.readString();
	        
	        reader.recordEnd();
	        return m_PayEntity;
	    }
	    return null;
	}
	
	/*
	*充值
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120007(NetReader reader) {
	    if (reader == null) {
	        return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.PayEntity m_PayEntity = ProtocolData.getInstance().new PayEntity();
	        reader.recordBegin();
	        m_PayEntity.UserID = reader.readInt64();
	        m_PayEntity.ExecType = reader.readInt();
	        m_PayEntity.JumpUrl = reader.readString();
	        m_PayEntity.PackageName = reader.readString();
	        m_PayEntity.Parameter = reader.readString();
	        m_PayEntity.Receiver = reader.readString();
	        m_PayEntity.Message = reader.readString();
	        m_PayEntity.StarLevel = reader.readInt();
	        m_PayEntity.AccountBalance = reader.readDouble();
	        //记录星级
	        UserInfo.getInstance().mAccountTotalMoney = m_PayEntity.AccountBalance;
	        UserInfo.getInstance().mCurrentVipLevel = m_PayEntity.StarLevel;
	        reader.recordEnd();
	        return m_PayEntity;
	    }
	    return null;
	}
	
	/*
	*加载消费账单列表
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120008(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.OrderEntityList m_OrderEntityList = ProtocolData.getInstance().new OrderEntityList();
	        reader.recordBegin();
	        m_OrderEntityList.OrdersDate = reader.readString();
	        m_OrderEntityList.UserID = reader.readInt64();
	        m_OrderEntityList.TotalInMoney = reader.readDouble();
	        m_OrderEntityList.TotalOutMoney = reader.readDouble();
	        ArrayList<ProtocolData.OrderEntity> m_Orders = new ArrayList<ProtocolData.OrderEntity>();
	        m_OrderEntityList.Orders = m_Orders;
	        int nNum_m_Orders1 = reader.readInt();
	        for(int idx_m_Orders_0 = 0; idx_m_Orders_0< nNum_m_Orders1; idx_m_Orders_0 ++)
	        { 
	            ProtocolData.OrderEntity v_item_m_Orders_0 = ProtocolData.getInstance().new OrderEntity();
	            reader.recordBegin();
	            v_item_m_Orders_0.AppName = reader.readString();
	            v_item_m_Orders_0.MerchandiseName = reader.readString();
	            v_item_m_Orders_0.OrderSerial = reader.readString();
	            v_item_m_Orders_0.OrderMoney = reader.readDouble();
	            v_item_m_Orders_0.OrderStatus = reader.readInt();
	            v_item_m_Orders_0.StartDateTime = reader.readString();
	            v_item_m_Orders_0.CooperatorDateTime = reader.readString();
	            v_item_m_Orders_0.AppID = reader.readInt64();
	            reader.recordEnd();
	            m_Orders.add(idx_m_Orders_0, v_item_m_Orders_0);
	        }
	        m_OrderEntityList.RecordCount = reader.readInt();
	        reader.recordEnd();
	        return m_OrderEntityList;
	    }
	    return null;
	}
	
	/*
	*加载消费账单详细信息
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120009(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.OrderEntity m_OrderEntity = ProtocolData.getInstance().new OrderEntity();
	        reader.recordBegin();
	        m_OrderEntity.AppName = reader.readString();
	        m_OrderEntity.MerchandiseName = reader.readString();
	        m_OrderEntity.OrderSerial = reader.readString();
	        m_OrderEntity.OrderMoney = reader.readDouble();
	        m_OrderEntity.OrderStatus = reader.readInt();
	        m_OrderEntity.StartDateTime = reader.readString();
	        m_OrderEntity.CooperatorDateTime = reader.readString();
	        m_OrderEntity.AppID = reader.readInt64();
	        reader.recordEnd();
	        return m_OrderEntity;
	    }
	    return null;
	}
	
	/*
	*加载收入账单列表
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120010(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.OrderEntityList m_OrderEntityList = ProtocolData.getInstance().new OrderEntityList();
	        reader.recordBegin();
	        m_OrderEntityList.OrdersDate = reader.readString();
	        m_OrderEntityList.UserID = reader.readInt64();
	        m_OrderEntityList.TotalInMoney = reader.readDouble();
	        m_OrderEntityList.TotalOutMoney = reader.readDouble();
	        ArrayList<ProtocolData.OrderEntity> m_Orders = new ArrayList<ProtocolData.OrderEntity>();
	        m_OrderEntityList.Orders = m_Orders;
	        int nNum_m_Orders1 = reader.readInt();
	        for(int idx_m_Orders_0 = 0; idx_m_Orders_0< nNum_m_Orders1; idx_m_Orders_0 ++)
	        { 
	            ProtocolData.OrderEntity v_item_m_Orders_0 = ProtocolData.getInstance().new OrderEntity();
	            reader.recordBegin();
	            v_item_m_Orders_0.AppName = reader.readString();
	            v_item_m_Orders_0.MerchandiseName = reader.readString();
	            v_item_m_Orders_0.OrderSerial = reader.readString();
	            v_item_m_Orders_0.OrderMoney = reader.readDouble();
	            v_item_m_Orders_0.OrderStatus = reader.readInt();
	            v_item_m_Orders_0.StartDateTime = reader.readString();
	            v_item_m_Orders_0.CooperatorDateTime = reader.readString();
	            v_item_m_Orders_0.AppID = reader.readInt64();
	            reader.recordEnd();
	            m_Orders.add(idx_m_Orders_0, v_item_m_Orders_0);
	        }
	        m_OrderEntityList.RecordCount = reader.readInt();
	        reader.recordEnd();
	        return m_OrderEntityList;
	    }
	    return null;
	}
	
	/*
	*加载收入账单详情
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120011(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.OrderEntity m_OrderEntity = ProtocolData.getInstance().new OrderEntity();
	        reader.recordBegin();
	        m_OrderEntity.AppName = reader.readString();
	        m_OrderEntity.MerchandiseName = reader.readString();
	        m_OrderEntity.OrderSerial = reader.readString();
	        m_OrderEntity.OrderMoney = reader.readDouble();
	        m_OrderEntity.OrderStatus = reader.readInt();
	        m_OrderEntity.StartDateTime = reader.readString();
	        m_OrderEntity.CooperatorDateTime = reader.readString();
	        m_OrderEntity.AppID = reader.readInt64();
	        reader.recordEnd();
	        return m_OrderEntity;
	    }
	    return null;
	}
	
	/*
	*加载最近数月的前N条消费账单
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120012(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.MonthsOrderEntityList m_MonthsOrderEntityList = ProtocolData.getInstance().new MonthsOrderEntityList();
	        reader.recordBegin();
	        m_MonthsOrderEntityList.UserID = reader.readInt64();
	        ArrayList<ProtocolData.OrderEntityList> m_OrderEntityLists = new ArrayList<ProtocolData.OrderEntityList>();
	        m_MonthsOrderEntityList.OrderEntityLists = m_OrderEntityLists;
	        int nNum_m_OrderEntityLists1 = reader.readInt();
	        for(int idx_m_OrderEntityLists_0 = 0; idx_m_OrderEntityLists_0< nNum_m_OrderEntityLists1; idx_m_OrderEntityLists_0 ++)
	        { 
	            ProtocolData.OrderEntityList v_item_m_OrderEntityLists_0 = ProtocolData.getInstance().new OrderEntityList();
	            reader.recordBegin();
	            v_item_m_OrderEntityLists_0.OrdersDate = reader.readString();
	            v_item_m_OrderEntityLists_0.UserID = reader.readInt64();
	            v_item_m_OrderEntityLists_0.TotalInMoney = reader.readDouble();
	            v_item_m_OrderEntityLists_0.TotalOutMoney = reader.readDouble();
	            ArrayList<ProtocolData.OrderEntity> m_Orders = new ArrayList<ProtocolData.OrderEntity>();
	            v_item_m_OrderEntityLists_0.Orders = m_Orders;
	            int nNum_m_Orders1 = reader.readInt();
	            for(int idx_m_Orders_0 = 0; idx_m_Orders_0< nNum_m_Orders1; idx_m_Orders_0 ++)
	            { 
	                ProtocolData.OrderEntity v_item_m_Orders_0 = ProtocolData.getInstance().new OrderEntity();
	                reader.recordBegin();
	                v_item_m_Orders_0.AppName = reader.readString();
	                v_item_m_Orders_0.MerchandiseName = reader.readString();
	                v_item_m_Orders_0.OrderSerial = reader.readString();
	                v_item_m_Orders_0.OrderMoney = reader.readDouble();
	                v_item_m_Orders_0.OrderStatus = reader.readInt();
	                v_item_m_Orders_0.StartDateTime = reader.readString();
	                v_item_m_Orders_0.CooperatorDateTime = reader.readString();
	                v_item_m_Orders_0.AppID = reader.readInt64();
	                reader.recordEnd();
	                m_Orders.add(idx_m_Orders_0, v_item_m_Orders_0);
	            }
	            v_item_m_OrderEntityLists_0.RecordCount = reader.readInt();
	            reader.recordEnd();
	            m_OrderEntityLists.add(idx_m_OrderEntityLists_0, v_item_m_OrderEntityLists_0);
	        }
	        reader.recordEnd();
	        return m_MonthsOrderEntityList;
	    }
	    return null;
	}
	
	/*
	*加载最近数月的前N条收入账单
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120013(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.MonthsOrderEntityList m_MonthsOrderEntityList = ProtocolData.getInstance().new MonthsOrderEntityList();
	        reader.recordBegin();
	        m_MonthsOrderEntityList.UserID = reader.readInt64();
	        ArrayList<ProtocolData.OrderEntityList> m_OrderEntityLists = new ArrayList<ProtocolData.OrderEntityList>();
	        m_MonthsOrderEntityList.OrderEntityLists = m_OrderEntityLists;
	        int nNum_m_OrderEntityLists1 = reader.readInt();
	        for(int idx_m_OrderEntityLists_0 = 0; idx_m_OrderEntityLists_0< nNum_m_OrderEntityLists1; idx_m_OrderEntityLists_0 ++)
	        { 
	            ProtocolData.OrderEntityList v_item_m_OrderEntityLists_0 = ProtocolData.getInstance().new OrderEntityList();
	            reader.recordBegin();
	            v_item_m_OrderEntityLists_0.OrdersDate = reader.readString();
	            v_item_m_OrderEntityLists_0.UserID = reader.readInt64();
	            v_item_m_OrderEntityLists_0.TotalInMoney = reader.readDouble();
	            v_item_m_OrderEntityLists_0.TotalOutMoney = reader.readDouble();
	            ArrayList<ProtocolData.OrderEntity> m_Orders = new ArrayList<ProtocolData.OrderEntity>();
	            v_item_m_OrderEntityLists_0.Orders = m_Orders;
	            int nNum_m_Orders1 = reader.readInt();
	            for(int idx_m_Orders_0 = 0; idx_m_Orders_0< nNum_m_Orders1; idx_m_Orders_0 ++)
	            { 
	                ProtocolData.OrderEntity v_item_m_Orders_0 = ProtocolData.getInstance().new OrderEntity();
	                reader.recordBegin();
	                v_item_m_Orders_0.AppName = reader.readString();
	                v_item_m_Orders_0.MerchandiseName = reader.readString();
	                v_item_m_Orders_0.OrderSerial = reader.readString();
	                v_item_m_Orders_0.OrderMoney = reader.readDouble();
	                v_item_m_Orders_0.OrderStatus = reader.readInt();
	                v_item_m_Orders_0.StartDateTime = reader.readString();
	                v_item_m_Orders_0.CooperatorDateTime = reader.readString();
	                v_item_m_Orders_0.AppID = reader.readInt64();
	                reader.recordEnd();
	                m_Orders.add(idx_m_Orders_0, v_item_m_Orders_0);
	            }
	            v_item_m_OrderEntityLists_0.RecordCount = reader.readInt();
	            reader.recordEnd();
	            m_OrderEntityLists.add(idx_m_OrderEntityLists_0, v_item_m_OrderEntityLists_0);
	        }
	        reader.recordEnd();
	        return m_MonthsOrderEntityList;
	    }
	    return null;
	}
	
	/*
	*加载应用账单
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120014(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.OrderEntityList m_OrderEntityList = ProtocolData.getInstance().new OrderEntityList();
	        reader.recordBegin();
	        m_OrderEntityList.OrdersDate = reader.readString();
	        m_OrderEntityList.UserID = reader.readInt64();
	        m_OrderEntityList.TotalInMoney = reader.readDouble();
	        m_OrderEntityList.TotalOutMoney = reader.readDouble();
	        ArrayList<ProtocolData.OrderEntity> m_Orders = new ArrayList<ProtocolData.OrderEntity>();
	        m_OrderEntityList.Orders = m_Orders;
	        int nNum_m_Orders1 = reader.readInt();
	        for(int idx_m_Orders_0 = 0; idx_m_Orders_0< nNum_m_Orders1; idx_m_Orders_0 ++)
	        { 
	            ProtocolData.OrderEntity v_item_m_Orders_0 = ProtocolData.getInstance().new OrderEntity();
	            reader.recordBegin();
	            v_item_m_Orders_0.AppName = reader.readString();
	            v_item_m_Orders_0.MerchandiseName = reader.readString();
	            v_item_m_Orders_0.OrderSerial = reader.readString();
	            v_item_m_Orders_0.OrderMoney = reader.readDouble();
	            v_item_m_Orders_0.OrderStatus = reader.readInt();
	            v_item_m_Orders_0.StartDateTime = reader.readString();
	            v_item_m_Orders_0.CooperatorDateTime = reader.readString();
	            v_item_m_Orders_0.AppID = reader.readInt64();
	            reader.recordEnd();
	            m_Orders.add(idx_m_Orders_0, v_item_m_Orders_0);
	        }
	        m_OrderEntityList.RecordCount = reader.readInt();
	        reader.recordEnd();
	        return m_OrderEntityList;
	    }
	    return null;
	}
	
	/*
	*加载最近数月的前N条应用账单
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_120015(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.MonthsOrderEntityList m_MonthsOrderEntityList = ProtocolData.getInstance().new MonthsOrderEntityList();
	        reader.recordBegin();
	        m_MonthsOrderEntityList.UserID = reader.readInt64();
	        ArrayList<ProtocolData.OrderEntityList> m_OrderEntityLists = new ArrayList<ProtocolData.OrderEntityList>();
	        m_MonthsOrderEntityList.OrderEntityLists = m_OrderEntityLists;
	        int nNum_m_OrderEntityLists1 = reader.readInt();
	        for(int idx_m_OrderEntityLists_0 = 0; idx_m_OrderEntityLists_0< nNum_m_OrderEntityLists1; idx_m_OrderEntityLists_0 ++)
	        { 
	            ProtocolData.OrderEntityList v_item_m_OrderEntityLists_0 = ProtocolData.getInstance().new OrderEntityList();
	            reader.recordBegin();
	            v_item_m_OrderEntityLists_0.OrdersDate = reader.readString();
	            v_item_m_OrderEntityLists_0.UserID = reader.readInt64();
	            v_item_m_OrderEntityLists_0.TotalInMoney = reader.readDouble();
	            v_item_m_OrderEntityLists_0.TotalOutMoney = reader.readDouble();
	            ArrayList<ProtocolData.OrderEntity> m_Orders = new ArrayList<ProtocolData.OrderEntity>();
	            v_item_m_OrderEntityLists_0.Orders = m_Orders;
	            int nNum_m_Orders1 = reader.readInt();
	            for(int idx_m_Orders_0 = 0; idx_m_Orders_0< nNum_m_Orders1; idx_m_Orders_0 ++)
	            { 
	                ProtocolData.OrderEntity v_item_m_Orders_0 = ProtocolData.getInstance().new OrderEntity();
	                reader.recordBegin();
	                v_item_m_Orders_0.AppName = reader.readString();
	                v_item_m_Orders_0.MerchandiseName = reader.readString();
	                v_item_m_Orders_0.OrderSerial = reader.readString();
	                v_item_m_Orders_0.OrderMoney = reader.readDouble();
	                v_item_m_Orders_0.OrderStatus = reader.readInt();
	                v_item_m_Orders_0.StartDateTime = reader.readString();
	                v_item_m_Orders_0.CooperatorDateTime = reader.readString();
	                v_item_m_Orders_0.AppID = reader.readInt64();
	                reader.recordEnd();
	                m_Orders.add(idx_m_Orders_0, v_item_m_Orders_0);
	            }
	            v_item_m_OrderEntityLists_0.RecordCount = reader.readInt();
	            reader.recordEnd();
	            m_OrderEntityLists.add(idx_m_OrderEntityLists_0, v_item_m_OrderEntityLists_0);
	        }
	        reader.recordEnd();
	        return m_MonthsOrderEntityList;
	    }
	    return null;
	}
	
	/*
	*加载推送服务
	*/
	private static ProtocolData.BaseProtocalData parseProtocol_130001(NetReader reader) {
	    if (reader == null) {
	            return null;
	    }
	    if(reader.readInt() > 0) {
	        ProtocolData.PushServiceListEntity m_PushServiceListEntity = ProtocolData.getInstance().new PushServiceListEntity();
	        reader.recordBegin();
	        ArrayList<ProtocolData.PushServiceEntity> m_PushServiceList = new ArrayList<ProtocolData.PushServiceEntity>();
	        m_PushServiceListEntity.PushServiceList = m_PushServiceList;
	        int nNum_m_PushServiceList1 = reader.readInt();
	        for(int idx_m_PushServiceList_0 = 0; idx_m_PushServiceList_0< nNum_m_PushServiceList1; idx_m_PushServiceList_0 ++)
	        { 
	            ProtocolData.PushServiceEntity v_item_m_PushServiceList_0 = ProtocolData.getInstance().new PushServiceEntity();
	            reader.recordBegin();
	            v_item_m_PushServiceList_0.ID = reader.readInt64();
	            v_item_m_PushServiceList_0.Title = reader.readString();
	            v_item_m_PushServiceList_0.Content = reader.readString();
	            v_item_m_PushServiceList_0.IcoLink = reader.readString();
	            v_item_m_PushServiceList_0.Href = reader.readString();
	            v_item_m_PushServiceList_0.Time = reader.readString();
	            v_item_m_PushServiceList_0.Action = reader.readString();
	            reader.recordEnd();
	            m_PushServiceList.add(idx_m_PushServiceList_0, v_item_m_PushServiceList_0);
	        }
	        m_PushServiceListEntity.RecordCount = reader.readInt();
	        reader.recordEnd();
	        return m_PushServiceListEntity;
	    }
	    return null;
	}
}