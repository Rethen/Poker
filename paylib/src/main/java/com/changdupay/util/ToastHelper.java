package com.changdupay.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastHelper{
	private static int MESSAGE_TOAST_TEXT						= -1000;
	private static int MESSAGE_TOAST_RESOURCE					= -1010;
	private static int MESSAGE_TOAST_VIEW						= -1020;
	private static int MESSAGE_TOAST_RESOURCE_GRAVITY			= -1030;
	private static int MESSAGE_TOAST_TEXT_GRAVITY				= -1040;
	
	private static InnerHandler handler;
	private static Context baseContext = null;
	
	public static void setContext(Context context){
		baseContext = context;
	}
	
	public static void shortToastText(String text){
		makeText(text, Toast.LENGTH_SHORT, Gravity.NO_GRAVITY);
	}
	
	public static void longToastText(String text){
		makeText(text, Toast.LENGTH_LONG, Gravity.NO_GRAVITY);
	}
	
	public static void shortToastTextCenter(String text){
		makeText(text, Toast.LENGTH_SHORT, Gravity.CENTER);
	}
	
	public static void longToastTextCenter(String text){
		makeText(text, Toast.LENGTH_LONG, Gravity.CENTER);
	}
	
	public static void shortToastFormatText(int resId, Object... args){
		makeText(Toast.LENGTH_SHORT, resId, args);
	}
	
	public static void longToastFormatText(int resId, Object... args){
		makeText(Toast.LENGTH_LONG, resId, args);
	}

	public static void shortToastTextCenter(int resId){
		makeText(resId, Toast.LENGTH_SHORT, Gravity.CENTER);
	}
	
	public static void longToastTextCenter(int resId){
		makeText(resId, Toast.LENGTH_LONG, Gravity.CENTER);
	}
	
	public static void shortToastText(int resId){
		makeText(resId, Toast.LENGTH_SHORT, Gravity.NO_GRAVITY);
	}
	
	public static void longToastText(int resId){
		makeText(resId, Toast.LENGTH_LONG, Gravity.NO_GRAVITY);
	}

	public static void shortToastViewCenter(int viewId){
		View view = View.inflate(ContextUtil.getContext(), viewId, null);
		toastView(view, Gravity.CENTER, 0, 0, 0, 0, Toast.LENGTH_SHORT);
	}
	
	public static void longToastViewCenter(int viewId){
		View view = View.inflate(ContextUtil.getContext(), viewId, null);
		toastView(view, Gravity.CENTER, 0, 0, 0, 0, Toast.LENGTH_LONG);
	}

	public static void shortToastViewCenter(View view){
		toastView(view, Gravity.CENTER, 0, 0, 0, 0, Toast.LENGTH_SHORT);
	}
	
	public static void longToastViewCenter(View view){
		toastView(view, Gravity.CENTER, 0, 0, 0, 0, Toast.LENGTH_LONG);
	}
	
	public static void shortToastView(View view){
		toastView(view, 0, 0, 0, 0, 0, Toast.LENGTH_SHORT);
	}
	
	public static void longToastView(View view){
		toastView(view, 0, 0, 0, 0, 0, Toast.LENGTH_LONG);
	}
	
	public static void defaultToast(String text, int duration) {
		TextView textView = new TextView(ContextUtil.getContext());
		textView.setText(text);
		textView.setTextSize(16);
		textView.setPadding(15, 15, 15, 15);
		textView.setBackgroundColor(Color.rgb(0x44, 0x44, 0x44));
		textView.setTextColor(Color.WHITE);
		ToastHelper.toastView(textView, Gravity.CENTER, 0, 0, 0, 0, duration);
	}
	
	public static void defaultToast(int textId, int duration) {
		try {
			defaultToast(ContextUtil.getContext().getString(textId), duration);			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void shortDefaultToast(int textId) {
		defaultToast(textId, Toast.LENGTH_SHORT);
	}
	
	public static void longDefaultToast(int textId) {
		defaultToast(textId, Toast.LENGTH_LONG);
	}
	
	public static void shortDefaultToast(String text) {
		defaultToast(text, Toast.LENGTH_SHORT);
	}
	
	public static void longDefaultToast(String text) {
		defaultToast(text, Toast.LENGTH_LONG);
	}
	
	public static void toastView(View view, int gravity, int duration){
		toastView(view, gravity, 0, 0, 0.0f, 0.0f, duration);
	}
	
	public static void toastView(View view, int gravity, int xOffset, int yOffset, 
			float horizontalMargin, float verticalMargin, int duration){
		getInnerHandler().removeMessages(MESSAGE_TOAST_VIEW, view);
		
		ToastEntity entity=new ToastEntity();
		entity.setView(view);
		entity.setGravity(gravity);
		entity.setxOffset(xOffset);
		entity.setyOffset(yOffset);
		entity.setHorizontalMargin(horizontalMargin);
		entity.setVerticalMargin(verticalMargin);
		entity.setDuration(duration);
		
		getInnerHandler().sendMessage(handler.obtainMessage(MESSAGE_TOAST_VIEW, entity));
	}
	
	public static void toastView(int resId, int gravity, int duration){
		toastView(resId, gravity, 0, 0, 0.0f, 0.0f, duration);
	}
	
	public static void toastView(int resId, int gravity, int xOffset, int yOffset, 
			float horizontalMargin, float verticalMargin, int duration){
		getInnerHandler().removeMessages(MESSAGE_TOAST_RESOURCE_GRAVITY,resId);
		
		ToastEntity entity=new ToastEntity();
		entity.setResource(resId);
		entity.setGravity(gravity);
		entity.setxOffset(xOffset);
		entity.setyOffset(yOffset);
		entity.setHorizontalMargin(horizontalMargin);
		entity.setVerticalMargin(verticalMargin);
		entity.setDuration(duration);
		
		getInnerHandler().sendMessage(handler.obtainMessage(MESSAGE_TOAST_RESOURCE_GRAVITY, entity));
	}
	
	public static void toastView(String text, int gravity, int duration){
		toastView(text, gravity, 0, 0, 0.0f, 0.0f, duration);
	}
	
	public static void toastView(String text, int gravity, int xOffset, int yOffset, 
			float horizontalMargin, float verticalMargin, int duration){
		getInnerHandler().removeMessages(MESSAGE_TOAST_TEXT_GRAVITY,text);
		
		ToastEntity entity=new ToastEntity();
		entity.setText(text);
		entity.setGravity(gravity);
		entity.setxOffset(xOffset);
		entity.setyOffset(yOffset);
		entity.setHorizontalMargin(horizontalMargin);
		entity.setVerticalMargin(verticalMargin);
		entity.setDuration(duration);
		
		getInnerHandler().sendMessage(handler.obtainMessage(MESSAGE_TOAST_TEXT_GRAVITY, entity));
	}
	
	public static void initHelper(){
		getInnerHandler();
	}
	
	private static void makeText(String text, int duration, int gravity){
		getInnerHandler().removeMessages(MESSAGE_TOAST_TEXT, text);
		getInnerHandler().sendMessage(handler.obtainMessage(MESSAGE_TOAST_TEXT, text), duration, gravity);
	}
	
	private static void makeText(int resId, int duration, int gravity){
		getInnerHandler().removeMessages(MESSAGE_TOAST_RESOURCE, resId);
		getInnerHandler().sendMessage(handler.obtainMessage(MESSAGE_TOAST_RESOURCE, resId), duration, gravity);
	}
	
	private static void makeText(int duration, int resId, Object... args){
		if (baseContext == null) {
			baseContext = ContextUtil.getContext();
		}
		String text=String.format(baseContext.getString(resId), args);
		getInnerHandler().removeMessages(MESSAGE_TOAST_TEXT, text);
		getInnerHandler().sendMessage(handler.obtainMessage(MESSAGE_TOAST_TEXT, text));
	}
	
	private static InnerHandler getInnerHandler(){
		if(handler==null){
			synchronized(InnerHandler.class){
				if(handler==null){
					handler=new InnerHandler(Looper.getMainLooper());
				}
			}
		}
		
		return handler;
	}
	
	private static final class InnerHandler extends Handler{
		
		public InnerHandler(Looper looper){
			super(looper);
		}
		
		public final boolean sendMessage(Message msg, int duration, int gravity){
			if (msg != null){
				msg.arg1 = duration;
				msg.arg2 = gravity;
			}
			return sendMessage(msg);
		}
		
		@Override
		public void handleMessage(Message msg){
			if (baseContext == null) {
				baseContext = ContextUtil.getContext();
			}
			super.handleMessage(msg);
			if(MESSAGE_TOAST_TEXT==msg.what && !TextUtils.isEmpty((String)msg.obj)){
				Toast toast = Toast.makeText(baseContext, (String)msg.obj, msg.arg1);
				toast.setGravity(msg.arg2, 0, 0);
				toast.show();
				
			}else if(MESSAGE_TOAST_RESOURCE==msg.what){
				Toast toast = Toast.makeText(baseContext, (Integer)msg.obj, msg.arg1);
				toast.setGravity(msg.arg2, 0, 0);
				toast.show();
				
			}else if(MESSAGE_TOAST_VIEW==msg.what && msg.obj!=null){
				ToastEntity tg=(ToastEntity)msg.obj;
				Toast toast=new Toast(baseContext);
				toast.setView(tg.getView());
				toast.setDuration(tg.getDuration());
				toast.setGravity(tg.getGravity(), tg.getxOffset(), tg.getyOffset());
				toast.setMargin(tg.horizontalMargin, tg.verticalMargin);
				toast.show();
			}else if(MESSAGE_TOAST_RESOURCE_GRAVITY==msg.what && msg.obj!=null){
				ToastEntity tg=(ToastEntity)msg.obj;
				Toast toast=Toast.makeText(baseContext, tg.getResource(), tg.getDuration());
				toast.setGravity(tg.getGravity(), tg.getxOffset(), tg.getyOffset());
				toast.setMargin(tg.horizontalMargin, tg.verticalMargin);
				toast.show();
			}else if(MESSAGE_TOAST_TEXT_GRAVITY==msg.what && msg.obj!=null){
				ToastEntity tg=(ToastEntity)msg.obj;
				Toast toast=Toast.makeText(baseContext, tg.getText(), tg.getDuration());
				toast.setGravity(tg.getGravity(), tg.getxOffset(), tg.getyOffset());
				toast.setMargin(tg.horizontalMargin, tg.verticalMargin);
				toast.show();
			}
		}
	}
	
	public static final class ToastEntity{
		private View view;
		private int resource;
		private String text;
		
		private int gravity;
		private int xOffset;
		private int yOffset;
		
		private float horizontalMargin;
		private float verticalMargin;
		
		private int duration;
		
		protected int getResource(){
			return resource;
		}
		protected void setResource(int resource){
			this.resource = resource;
		}
		protected String getText(){
			return text;
		}
		protected void setText(String text){
			this.text = text;
		}
		public View getView(){
			return view;
		}
		public void setView(View view){
			this.view = view;
		}
		public int getGravity(){
			return gravity;
		}
		public void setGravity(int gravity){
			this.gravity = gravity;
		}
		public int getxOffset(){
			return xOffset;
		}
		public void setxOffset(int xOffset){
			this.xOffset = xOffset;
		}
		public int getyOffset(){
			return yOffset;
		}
		public void setyOffset(int yOffset){
			this.yOffset = yOffset;
		}
		public float getHorizontalMargin(){
			return horizontalMargin;
		}
		public void setHorizontalMargin(float horizontalMargin){
			this.horizontalMargin = horizontalMargin;
		}
		public float getVerticalMargin(){
			return verticalMargin;
		}
		public void setVerticalMargin(float verticalMargin){
			this.verticalMargin = verticalMargin;
		}
		protected int getDuration(){
			return duration;
		}
		protected void setDuration(int duration){
			this.duration = duration;
		}
	}
}
