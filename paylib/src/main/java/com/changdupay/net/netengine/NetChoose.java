package com.changdupay.net.netengine;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

/**
 * 网络类型选择
 * @author Administrator
 *
 */
public class NetChoose {

	private static Uri uri = Uri.parse("content://telephony/carriers");
	private static final String PREFERRED_APN_URI ="content://telephony/carriers/preferapn"; 
    private static final Uri PREFERAPN_URI = Uri.parse(PREFERRED_APN_URI); 
    
    private static String getSelectedApnKey(Context ctx) { 
        String key = null; 
        Cursor cursor = ctx.getContentResolver().query(PREFERAPN_URI, new String[] {"_id"},
                null, null,null);  
        int row = cursor.getCount(); 
        if (row > 0) {
            cursor.moveToFirst();
            key = cursor.getString(0);
        }
        cursor.close();
        return key;
    }
    
    private static APN getAPN(Context ctx){ 
    	String key = getSelectedApnKey(ctx); 
    	
    	
        //current不为空表示可以使用的APN 
        String  projection[] = {"_id,apn,type,proxy,port,current"}; 
        if(key==null){
        	key = "";
        	return null;
        }else{
        	key = "_id ="+key +" and ";
        }
        Cursor cr = ctx.getContentResolver().query(uri, projection, key + "  current<>'' and proxy<>'' and port<>'' and  current is not null  and proxy is not null and port is not null", null, null);  
        APN a = new APN();  
        if(cr!=null && cr.moveToNext()){ 
	         a.id = cr.getString(cr.getColumnIndex("_id")); 
	         a.apn = cr.getString(cr.getColumnIndex("apn")); 
	         a.type = cr.getString(cr.getColumnIndex("type"));  
	         a.proxy = cr.getString(cr.getColumnIndex("proxy"));
	         a.port = cr.getInt(cr.getColumnIndex("port"));
	         cr.close(); 
        }  
        return a;
    }
    
    private static class APN{ 
        String id; 
        String apn; 
        String type; 
        String proxy;
        int    port;
    }
     
    
    /**
     * 从APN获取代理
     * @param ctx
     * @return
     */
    private static Proxy getProxyFormAPN(Context ctx){
    	Proxy proxy = null;
    	APN apn = getAPN(ctx);
    	if(apn != null && apn.apn!=null&&apn.proxy!=null&&apn.port>0){
    		proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress(apn.proxy,apn.port));
    	} 
    	return proxy;
    }
    
    /**
     * 获取当前有效的网络连接
     * @param ctx 
     * @param url 目标地址
     * @return
     */
    public static HttpURLConnection getAvailableNetwork(Context ctx,final URL url) { 
    	HttpURLConnection conn = null; 
		try {  
			ConnectivityManager mConnMgra= (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfowifi = mConnMgra.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
			if(networkInfowifi.isAvailable() && networkInfowifi.isConnected()){
				Log.d("net", "connect by wifi");
				conn = (HttpURLConnection)url.openConnection();
				return conn;
			} 
			NetworkInfo networkInfo = mConnMgra.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
			/*if(isFastMobileNetwork(ctx)){
				Log.d("net", "current is 3G");
			}else{
				Log.d("net", "current is 2G");
			}*/
			if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()){
				Log.d("net", "connect by mobile");
				String proxyHost = android.net.Proxy.getDefaultHost();   
				if (proxyHost != null) {
					Log.d("proxyHost", "connect by proxy");  
					/*URL url1 = new URL(url.getProtocol(),proxyHost,android.net.Proxy.getDefaultPort(),url.getFile());;   
					HttpURLConnection conn_c = (HttpURLConnection) url1.openConnection();   
					conn_c.setRequestProperty("X-Online-Host", url.getHost()); 
					conn_c.setDoInput(true); 
					return conn_c;*/
					
					Proxy p = new Proxy(
							Proxy.Type.HTTP, new InetSocketAddress(
									android.net.Proxy.getDefaultHost(),
									android.net.Proxy.getDefaultPort())); 
					HttpURLConnection conn_c = (HttpURLConnection) url.openConnection(p);
					 
					return conn_c;

				} else {
					return (HttpURLConnection) url.openConnection();
				} 
				
				
				
				/*Proxy proxy = getProxyFormAPN(ctx);
				if(proxy != null){
					conn = (HttpURLConnection)url.openConnection(proxy);
					return conn;
				}else{
					conn = (HttpURLConnection)url.openConnection(); 
					return conn;
				}*/
			}else{
				Log.d("net", "connect by other");
				return (HttpURLConnection) url.openConnection();
			}
			
		} catch (Exception e) {
//			Log.e("Exception", e.getMessage());
			e.printStackTrace();
			Log.d("net", "connect errpr");
			conn = null;
		}
		return conn;
	}
    
    
    
    /**
     * 判断当前是否需要代理
     * @param ctx
     * @return
     */
    public static boolean isProxy(Context ctx){
    	Log.d("net", " isProxy ");
    	ConnectivityManager mConnMgra= (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfowifi = mConnMgra.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		if(networkInfowifi.isAvailable() && networkInfowifi.isConnected()){
			return false;
		}
    	
    	NetworkInfo networkInfo = mConnMgra.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()){
			String proxyHost = android.net.Proxy.getDefaultHost();   
			if (proxyHost != null) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
    }
    
    
	/*private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	} */

    
}