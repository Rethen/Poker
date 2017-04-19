package com.changdupay.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * 根据资源的名字获取其ID值
 * @author mining
 *
 */
public class MResource {
	public static int getIdByName(Context context, String defType, String name) {
		int resId = 0;
		if (context != null && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(defType)) {
			resId = context.getResources().getIdentifier(name, defType, context.getPackageName());
		}
		return resId;
	}
	
	public static int[] getIdsByName(Context context, String className, String name) {
	    String packageName = context.getPackageName();
	    Class r = null;
	    int[] ids = null;
	    try {
	      r = Class.forName(packageName + ".R");

	      Class[] classes = r.getClasses();
	      Class desireClass = null;

	      for (int i = 0; i < classes.length; ++i) {
	        if (classes[i].getName().split("\\$")[1].equals(className)) {
	          desireClass = classes[i];
	          break;
	        }
	      }

	      if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null) && (desireClass.getField(name).get(desireClass).getClass().isArray()))
	        ids = (int[])desireClass.getField(name).get(desireClass);
	    }
	    catch (ClassNotFoundException e) {
	      e.printStackTrace();
	    } catch (IllegalArgumentException e) {
	      e.printStackTrace();
	    } catch (SecurityException e) {
	      e.printStackTrace();
	    } catch (IllegalAccessException e) {
	      e.printStackTrace();
	    } catch (NoSuchFieldException e) {
	      e.printStackTrace();
	    }

	    return ids;
	  }
}
