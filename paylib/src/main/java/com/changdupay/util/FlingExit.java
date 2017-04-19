package com.changdupay.util;

import android.app.Activity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * 滑动退出界面的类 提供检测方法isFlingExit和退出方法onFling
 * 
 * @author Administrator
 * 
 */
public class FlingExit implements OnGestureListener{

	private static boolean isViewPageScroll;// viewPage 子控件的滑动，则不响应滑动事件
	
	private GestureDetector gestureDetector;
	private OnFlingExcuteCallBack callBack; 
	private int fling_min_distance;  // 距离控制
	private float lastEventX;
	private boolean isEnable;
	private boolean isFlingExcuted;

	public FlingExit(Activity activity, OnFlingExcuteCallBack callBack) {
		gestureDetector = new GestureDetector(activity, this);
		this.callBack = callBack;
		
		WindowManager manager = activity.getWindowManager();
		Display display = manager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		fling_min_distance = screenWidth / 5;
	}

	public interface OnFlingExcuteCallBack {
		/***
		* @Title: onFlingExcute 
		* @Description: TODO 
		* @param @return    
		* @return boolean 	true  onFlingExcute action has excuted  false hasn't excuted
		* @throws
		 */
		public boolean onFlingExcute();
	}
	
	public static void disableWhenViewPageScroll() {
		isViewPageScroll = true;
	}
	
	/**
	 * 检测是否滑动退出的方法
	 * 
	 * @param ev 动作事件
	 * 
	 * return  true flingEventExcute    false normal
	 */
	public boolean flingExitProcess(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		int action = ev.getAction()& MotionEvent.ACTION_MASK;
		if (action == MotionEvent.ACTION_DOWN) {
			isFlingExcuted = false;
			isEnable = true;
		} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
			isViewPageScroll = false;
		} else if (ev.getX() < lastEventX || isViewPageScroll) {
			isEnable = false;
		}
		lastEventX = ev.getX();
		if (isFlingExcuted) {
			isFlingExcuted = false;
			return true;
		}
		return false;
	}


	/**
	 * 滑动关闭activity的方法
	 * 
	 * @param e1
	 *            放下的动作
	 * @param e2
	 *            抬起的动作
	 * @param velocityX
	 *            速度
	 * @param velocityY
	 * @return
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int fling_min_velocity = 150;
		if (isEnable && e2.getX() - e1.getX() > fling_min_distance
				&& Math.abs(velocityX) > fling_min_velocity
				&& Math.abs(e1.getY() - e2.getY()) < Math.abs(e1.getX()
						- e2.getY()) * 0.5) {
			if (callBack != null) {
				isFlingExcuted = callBack.onFlingExcute();
			}
			isEnable = false;
			return false;
		}
		isEnable = false;
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		
	}
}
