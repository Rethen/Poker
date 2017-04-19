package com.changdupay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.changdupay.android.lib.R;
import com.changdupay.app.PayResultActivity;
import com.changdupay.open.iChangduPay;
import com.changdupay.protocol.base.PayConst;
import com.changdupay.protocol.pay.RequestContent;
import com.changdupay.util.PayConfigs.Category;
import com.changdupay.util.PayConfigs.ChannelList;

public class Utils {
	public static final String CONFIG_NAME = "CDPayConfig";

	public static void setHasLogined(Context context, boolean hasLogined) {
		SharedPreferences settingPreferences = context.getSharedPreferences(CONFIG_NAME, 0);
		SharedPreferences.Editor editor = settingPreferences.edit();
		editor.putBoolean("hasLogined", hasLogined);
		editor.commit();
	}

	public static boolean getHasLogined(Context context) {
		SharedPreferences settingPreferences = context.getSharedPreferences(CONFIG_NAME, 0);
		return settingPreferences.getBoolean("hasLogined", false);
	}
	
	public static String getStringValue(String strKey) {
		SharedPreferences settingPreferences = ContextUtil.getContext().getSharedPreferences(CONFIG_NAME, 0);
		return settingPreferences.getString(strKey, "");
	}
	
	public static void setStringValue(String strKey, String strValue) {
		SharedPreferences settingPreferences = ContextUtil.getContext().getSharedPreferences(CONFIG_NAME, 0);
		SharedPreferences.Editor editor = settingPreferences.edit();
		editor.putString(strKey, strValue);
		editor.commit();
	}
	
    public static int getIntValue(String strKey) {
    	SharedPreferences settingPreferences = ContextUtil.getContext().getSharedPreferences(CONFIG_NAME, 0);
    	return settingPreferences.getInt(strKey, Const.SORT_ID);
    }
	 
	public static int getIntegerValue(String strKey) {
		SharedPreferences settingPreferences = ContextUtil.getContext().getSharedPreferences(CONFIG_NAME, 0);
		return settingPreferences.getInt(strKey, 0);
	}
	
	public static void setIntegerValue(String strKey, int nValue) {
		SharedPreferences settingPreferences = ContextUtil.getContext().getSharedPreferences(CONFIG_NAME, 0);
		SharedPreferences.Editor editor = settingPreferences.edit();
		editor.putInt(strKey, nValue);
		editor.commit();
	}
	
	public static Boolean isApkInstalled(Context context, String appName)
	{
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> pkgList = manager.getInstalledPackages(0);
		for (int i = 0; i < pkgList.size(); i++) {
			PackageInfo pI = pkgList.get(i);
			if (pI.packageName.equalsIgnoreCase(appName))
				return true;
		}

		return false;
	}

    //�ж�app�Ƿ��Ѱ�װ��packageName--����
	public static boolean isAppInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		boolean bInstalled = false;
		try {
		   // 获取所有已安装程序的包信息
	        List<PackageInfo> pinfo = pm.getInstalledPackages(0);
	        for ( int i = 0; i < pinfo.size(); i++ )
	        {
	            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName)){
	            	bInstalled = true;
	            	break;
	            }	            
	        }

	        PackageInfo p2 = pm.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
	        if(p2 != null){
	        	bInstalled = true;
	        }
	        PackageInfo p3 = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
	        if(p3 != null){
	        	bInstalled = true;
	        }	        

		} catch(NameNotFoundException e) {
			bInstalled = false;
		}
		return bInstalled;
	}

    //����app��packageName--����
	public static void launchApp(Context context, String packageName, String params) {
		if (context != null) {
	    	Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
	    	if (!TextUtils.isEmpty(params)) {
		    	intent.putExtra("params", params);				
			}
	    	if(intent != null) 
	    	{
	    		context.startActivity(intent);
	    	}
		}
	}

	public static void launchApp(Context context, String packageName) {
		launchApp(context, packageName, null);
	}
		
	public static boolean runChangduPay(Context context, RequestContent content) {
		if (Utils.isAppInstalled(context, iChangduPay.PACKAGE_NAME)) {
			if (context != null) {
				Deviceinfo.setContext(context);
		    	Intent intent = context.getPackageManager().getLaunchIntentForPackage(iChangduPay.PACKAGE_NAME);
		    	if(intent != null) 
		    	{
		    		intent.putExtra(Const.ParamType.TypeRequestContent, content);
		    		if (context instanceof Activity) {
//			    		((Activity)context).startActivityForResult(intent, iChangduPay.REQUEST_CODE_RUNPAY);
					}
		    		else {
			    		context.startActivity(intent);
					}
		    	}
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * ��ת�ַ���
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * ��ȡȨ��
	 * 
	 * @param permission
	 *            Ȩ��
	 * @param path
	 *            ·��
	 */
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String filterQuotationmarks(String strData)
	{
		if (strData != null)
		{
			strData = strData.replaceAll("\"", "");
			
			return strData;
		}
		return null;
	}
	
	public static void gotoPaySuccessActivity(String strResultMsg, Activity activity) {
		Intent intent = new Intent(ContextUtil.getContext(), PayResultActivity.class);
		intent.putExtra(Const.ParamType.TypePayResult, true);
		intent.putExtra(Const.ParamType.TypePayResultMsg, strResultMsg);
		intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
		if (activity != null) {
			activity.startActivityForResult(intent, Const.RequestCode.PayResult);
		}
		else {
			ContextUtil.getContext().startActivity(intent);
		}
	}

	public static void gotoPayFailedActivity(String strResultMsg, Activity activity) {
		Intent intent = new Intent(ContextUtil.getContext(), PayResultActivity.class);
		intent.putExtra(Const.ParamType.TypePayResult, false);
		intent.putExtra(Const.ParamType.TypePayResultMsg, strResultMsg);
		intent.putExtra(Const.ParamType.TypeNeedQuitOrNot, true);
		if (activity != null) {
			activity.startActivityForResult(intent, Const.RequestCode.PayResult);
		}
		else {
			ContextUtil.getContext().startActivity(intent);
		}
	}

	public static String getMonth(String strSrcTime, Context ctx) {
		if (TextUtils.isEmpty(strSrcTime)) {
			return "";
		}
		
		if (strSrcTime.length() >= 7) {
			String strYear = strSrcTime.substring(0, 4);
			String strMonth = strSrcTime.substring(5, 7);
			Date date = new Date();//取时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
//			if (calendar.get(Calendar.YEAR) == Integer.parseInt(strYear) && (calendar.get(Calendar.MONTH) + 1) == Integer.parseInt(strMonth)) {
//				return ctx.getString(R.string.ipay_current_month);				
//			}
//			else 
			{
				return strMonth + ctx.getString(R.string.ipay_month);
			}
		}
		
		return "";
	}

	public static String getTime(String strSrcTime, Context ctx) {
		if (TextUtils.isEmpty(strSrcTime)) {
			return "";
		}
		
		if (strSrcTime.length() >= "2013-11-11 00:00:00".length()) {
			String strYear = strSrcTime.substring(0, 4);
			String strMonth = strSrcTime.substring(5, 7);
			String strDay = strSrcTime.substring(8, 10);
			Date date = new Date();//取时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String strTime = strSrcTime.substring(11, 19);
			try {
//				if (calendar.get(Calendar.YEAR) == Integer.parseInt(strYear) 
//						&& (calendar.get(Calendar.MONTH) + 1) == Integer.parseInt(strMonth)
//						&& calendar.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(strDay)) {
//					return String.format("%s %s", "今日", strTime);		
//				}
//				else 
				{
					return String.format("%s %s", strSrcTime.substring(5, 10), strTime);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return "";
	}

	public static boolean checkPhoneNumberValid(String strPhone) {
		if (strPhone.length() != 11) {
			return false;
		}
		byte[] bytes = strPhone.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] < '0' || bytes[i] > '9') {
				return false;
			}
		}
		
		return true;
	}
	
	public static void hideSoftKeyPanel(Activity activity) {
		((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE))
			.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	
    public static void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    }  
    
    public static void setGridViewHeightBasedOnChildren(Context context, GridView gridView, int nColumns) {  
        ListAdapter listAdapter = gridView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
        int totalHeight = 0;  
        int nItems = listAdapter.getCount();
        int nRows = nItems / nColumns;
        if (nItems > 0 && nItems % nColumns != 0)
        {
        	nRows++;
        }
        if (nRows > 0)
        {
        	View listItem = listAdapter.getView(0, null, null);  
            listItem.measure(0, 0);  
            totalHeight = listItem.getMeasuredHeight() * nRows + nRows * DensityUtil.dip2px(context, 10);  
        }
        
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);  
    } 
    
//    public static void startUpdateAccountInfoService(Context context)
//    {
//    	Intent i = new Intent(context, GetAccountInfoService.class); 
//    	context.startService(i); 
//    }
//    
//    public static void stopUpdateAccountInfoService(Context context)
//    {
//    	context.stopService(new Intent(context, GetAccountInfoService.class)); 
//    }
    
    /**
     * 获取软件版本号
     * 
     * @param context
     * @return
     */
    public static double getVersionCode(Context context)
    {
        double dVer = 0;
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
        	String versionName = context.getPackageManager().getPackageInfo("com.changdupay.android.app", 0).versionName;
        	dVer = Double.parseDouble(versionName);
        } 
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return dVer;
    }

	//安装apk包： apkFilePath--apk包路径
    public static void installPackage(Context mContext, String apkFilePath) {
		if (mContext != null) {
	    	Intent intent = new Intent(Intent.ACTION_VIEW);
	    	intent.setDataAndType(Uri.fromFile(new File(apkFilePath)), "application/vnd.android.package-archive");
	    	mContext.startActivity(intent);
		}
	}
    
    public static Boolean isConfigFileExist()
	{
		File file = ContextUtil.getContext().getFilesDir();
		if (file != null)
		{
			String absolutePath = file.getAbsolutePath();
			String fileName = absolutePath + "/" + Const.PAY_DEFAULT_CONFIG_FILENAME;
			File configFile = new File(fileName);
			if (configFile.exists()) 
			{
			   return true;
			}
			return false;
		}

		return false;
	}
    
    public static void resetPayOrder(String strKey, int nValue) {
		SharedPreferences settingPreferences = ContextUtil.getContext().getSharedPreferences(CONFIG_NAME, 0);
		SharedPreferences.Editor editor = settingPreferences.edit();
		
		ChannelList payChannelList = PayConfigReader.getInstance().getChannelListByMerchandiseId(PayConst.MERCHANDISEID);
		ArrayList<Category> channelList = payChannelList.mlCategoryList;
		int nOldOrder = Const.SORT_ID;
		for (int j = 0; j < channelList.size(); j++)
		{
			if (TextUtils.equals(strKey, channelList.get(j).Name))
			{
				nOldOrder = channelList.get(j).SortId;
				channelList.get(j).SortId = nValue;
				break;
			}
		}
		
		for (int i = 0; i < channelList.size(); i++)
		{		
			if (TextUtils.equals(strKey, channelList.get(i).Name) || channelList.get(i).SortId == Const.SORT_ID)
			{
				continue;
			}
			
			if (channelList.get(i).SortId > nOldOrder)
			{
				if (nOldOrder != Const.SORT_ID)
				{
					continue;
				}
			}
			
			channelList.get(i).SortId++;
			editor.putInt(channelList.get(i).Name, channelList.get(i).SortId);
		}
		editor.putInt(strKey, nValue);
		editor.commit();
	}
    
	public static int dipDimensionInteger(float value){
		return (int)(dipDimensionFloat(value)+0.5f);
	}
	
	public static float dipDimensionFloat(float value) {
		return ContextUtil.getContext() == null ? value : TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, value, ContextUtil.getContext().getResources()
						.getDisplayMetrics());
	}
 
	public static int getSoftInputMode(Activity activity) {
		int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
		if (activity != null) {
			Window window = activity.getWindow();
			if (window != null) {
				WindowManager.LayoutParams attrs = window.getAttributes();
				if (attrs != null) {
					mode = attrs.softInputMode;
				}
			}
		}
		return mode;
	}
	
	public static boolean isSoftInputModeAdjustmentOptions(Activity activity, int mode) {
		int softInputMode = getSoftInputMode(activity);
		return (softInputMode & WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST) == mode;
	}

	/**
	 * 关闭输入法
	 * 
	 * @param activity
	 */
	public static void hiddenKeyboard(Activity activity) {
		if (activity != null) {
			try {
				InputMethodManager inputMethodManager = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setNoTitle(Activity activity){
		if(activity != null){
			if(hasSmartBar()){
		      	final ActionBar bar = activity.getActionBar();
				if(bar != null){
		      		setActionBarViewCollapsable(bar, true);
		      		bar.setDisplayOptions(0);
		      	}
			}else{
				activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
			}
		}
	}
	
	private static boolean hasSmartBar() {
        try {
            // 新型号可用反射调用Build.hasSmartBar()
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        // 反射不到Build.hasSmartBar()，则用Build.DEVICE判断
        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        
        return false;
    }
	
	private static void setActionBarViewCollapsable(ActionBar actionbar, boolean collapsable) {
        try {
            Method method = Class.forName("android.app.ActionBar").getMethod(
                    "setActionBarViewCollapsable", new Class[] { boolean.class });
            try {
                method.invoke(actionbar, collapsable);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }	
	
	public static String replaceMd5Data(String data){
		data = data.replace("+", "~");
		data = data.replace("/", "@");
		data = data.replace("=", "$");
		return data;
	}
}
