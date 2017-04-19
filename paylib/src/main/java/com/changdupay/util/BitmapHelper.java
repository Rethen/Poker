package com.changdupay.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;

public class BitmapHelper{	

	public static Shape getBitmapShape(int resId){
		Shape shape = new Shape(0, 0);
		
		if (resId != 0){
			Drawable drawable = ContextUtil.getContext().getResources().getDrawable(resId);

			shape = new Shape(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		}
		
		return shape;
	}
	
	public static Shape getBitmapShape(String absfile){
		Shape shape = new Shape(0, 0);
		
		if(!TextUtils.isEmpty(absfile) && new File(absfile).exists()){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(absfile, options);

			shape = new Shape(options.outWidth, options.outHeight);
		}
		
		return shape;
	}
	
	public static Drawable bitmap2Drawable(Bitmap bitmap){
		Drawable drawable=null;
		if(bitmap!=null){
			BitmapDrawable dp=new BitmapDrawable(bitmap);
			drawable=(Drawable)dp;
		}
		
		return drawable;
	}
	
	public static Bitmap drawable2Bitmap(Drawable drawable){
		Bitmap bitmap=null;
		if(drawable!=null && drawable instanceof BitmapDrawable){
			bitmap=((BitmapDrawable)drawable).getBitmap();
		}
		return bitmap;
	}
	
	public static Drawable bytes2Drawable(byte[] bitmapData){
		Drawable drawable=null;
		if(bitmapData!=null && bitmapData.length>0){
			BitmapDrawable dp=new BitmapDrawable(BitmapFactory.
					decodeByteArray(bitmapData, 0, bitmapData.length));
			drawable=(Drawable)dp;
		}
		
		return drawable;
	}
	
	public static Bitmap bytes2Bitmap(byte[] bitmapData){
		Bitmap bitmap=null;
		if(bitmapData!=null && bitmapData.length>0){
			BitmapDrawable dp=new BitmapDrawable(BitmapFactory.
					decodeByteArray(bitmapData, 0, bitmapData.length));
			bitmap=dp.getBitmap();
		}
		
		return bitmap;
	}
	
	public static byte[] bitmap2Bytes(Bitmap bitmap){
		byte[] bitmapData=null;
  
		if(bitmap!=null ){
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();  
			try{
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
				byteStream.flush();
				bitmapData = byteStream.toByteArray();
			}catch (Exception e) {
	        	e.printStackTrace();
			}finally{
				try {
					byteStream.close();
				} catch (Exception e) {
		        	e.printStackTrace();
				}
				byteStream=null;
			}
		}
		
		return bitmapData;
	}
	
	public static byte[] drawable2Bytes(Drawable drawable){
		byte[] bitmapData=null;
  
		Bitmap bitmap=drawable2Bitmap(drawable);
		if(bitmap!=null ){
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();  
			try{
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
				byteStream.flush();
				bitmapData = byteStream.toByteArray();
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					byteStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				byteStream=null;
			}
		}
		
		return bitmapData;
	}
	
	public static Drawable getStateDrawable(StateListDrawable skDraw, int[] state, int[] defState){
		Drawable stateDraw=null;
		if(skDraw!=null){
			if(state!=null && defState!=null){
				skDraw.setState(state);
				stateDraw=skDraw.getCurrent();
				if(stateDraw==null){
					skDraw.setState(defState);
					stateDraw=skDraw.getCurrent();
				}
			}else if(state!=null && defState==null){
				skDraw.setState(state);
				stateDraw=skDraw.getCurrent();
			}else if(state==null && defState!=null){
				skDraw.setState(defState);
				stateDraw=skDraw.getCurrent();
			}

			if(stateDraw==null){
				int[] empty={};
				skDraw.setState(empty);
				stateDraw=skDraw.getCurrent();
			}
		}
		
		return stateDraw;
	}
	
	public static Bitmap getStateBitmap(StateListDrawable skDraw, int[] state, int[] defState){
		
		return drawable2Bitmap(getStateDrawable(skDraw, state, defState));
	}
	
	public static Drawable roundedCorner(Drawable drawable, Rect rect, int roundPx, Config config){
		return bitmap2Drawable(roundedCorner(drawable2Bitmap(drawable), rect, roundPx, config));
	}
	
	public static Bitmap roundedCorner(Bitmap bitmap, Rect rect, int roundPx, Config config){
		Bitmap output=null;
		
		if(bitmap!=null && !bitmap.isRecycled()){
			Rect bitmapRect=new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			rect = rect==null ? bitmapRect : rect;
			
			int width=rect.right-rect.left;
			int height=rect.bottom-rect.top;
			
			output = Bitmap.createBitmap(width, height, config);
			Canvas canvas = new Canvas(output);

			Paint paint = new Paint();
			RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawColor(Color.TRANSPARENT);
			paint.setColor(0xff424242);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, bitmapRect, rect, paint);
		}

		return output;
	}
	
	public static void release(Drawable drawable){
		if(drawable!=null){
			if(drawable instanceof LayerDrawable){
				LayerDrawable lawable=(LayerDrawable)drawable;
				int count=lawable.getNumberOfLayers();
				for(int i=0; i<count; i++){
					release(lawable.getDrawable(i));
				}
			}
			else{
				drawable.setCallback(null);
			}
		}
	}
	
	public static void recycle(Drawable drawable){
		if(drawable!=null){
			if(drawable instanceof LayerDrawable){
				LayerDrawable lawable=(LayerDrawable)drawable;
				int count=lawable.getNumberOfLayers();
				for(int i=0; i<count; i++){
					recycle(lawable.getDrawable(i));
				}
			}else if(drawable instanceof StateListDrawable){
				drawable.setCallback(null);
			}else if(drawable instanceof BitmapDrawable){
				drawable.setCallback(null);
				
				Bitmap bitmap=drawable2Bitmap(drawable);
				if(bitmap!=null && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap=null;
				}
			}else{
				drawable.setCallback(null);
			}
		}
	}
	
	public static void recycle(Bitmap bitmap){
		if(bitmap!=null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap=null;
		}
	}
	
	public static boolean isBitmapRecycled(Bitmap bitmap){
		
		return bitmap!=null && bitmap.isRecycled();
	}
	
	public static boolean isBitmapRecycled(Drawable drawable){
		
		return isBitmapRecycled(drawable2Bitmap(drawable));
	}
	
	/**
	 * 判断bitmap是否无效
	 * @param bitmap
	 * @return bitmap为null或者已经被回收，返回true，否则返回false 
	 */
	public static boolean isBitmapInvalid(Bitmap bitmap){
		return bitmap == null || bitmap.isRecycled();
	}
	
	public static boolean isBitmapInvalid(Drawable drawable){
		return isBitmapInvalid(drawable2Bitmap(drawable));
	}
	
	public static boolean save(Bitmap bitmap, String absFile, Bitmap.CompressFormat format){
		boolean result=false;
		if(!TextUtils.isEmpty(absFile)){
			result=save(bitmap, new File(absFile), format);
		}
		
		return result;
	}
	
	public static boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat format){
		boolean result=false;
		if(file!=null && bitmap!=null && !bitmap.isRecycled()){
			BufferedOutputStream bos=null;
	        try{
	        	if(!file.exists()){
	        		file.getParentFile().mkdirs();
	        		file.createNewFile();
	        	}

	        	bos = new BufferedOutputStream(new FileOutputStream(file));  
	        	result=bitmap.compress(format==null ? Bitmap.CompressFormat.PNG : format, 100, bos);  
	        	bos.flush();  
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally{
				if(bos!=null){
					try{
						bos.close();
					}catch(Exception e){
			        	e.printStackTrace();
					}
				}
			}
		}
		
		return result;
	}
	
	/**
     * 获取bitmap的字节大小
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
    	int size = bitmap.getRowBytes() * bitmap.getHeight();
    	return size;
    }
    
	/**
	 * 画圆形图
	 */
	public static Bitmap createCircleBitmap(Bitmap bitmap, int dstWidth, int dstHeight) {
		Bitmap dstBitmap = null;
		try {
			if (!BitmapHelper.isBitmapInvalid(bitmap)) {
				dstBitmap = Bitmap.createBitmap(dstWidth, dstHeight, Config.ARGB_8888);

				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(0xff424242);

				Rect rect = new Rect(0, 0, dstWidth, dstHeight);
				RectF rectF = new RectF(rect);

				float r = dstHeight > dstWidth ? dstWidth / 2 : dstHeight / 2;// 半径

				Canvas canvas = new Canvas(dstBitmap);
				canvas.drawARGB(0, 0, 0, 0);
				canvas.drawRoundRect(rectF, r, r, paint);

				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				canvas.drawBitmap(bitmap, rect, rect, paint);

				canvas.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dstBitmap;
	}
	
	public static Bitmap createRoundAvaterBitmap(Bitmap bitmap) {  
		return createRoundBitmap(bitmap, Utils.dipDimensionInteger(7));
	}

	/**
	 * 画圆角
	 */
	public static Bitmap createRoundBitmap(Bitmap bitmap, int pixels) {  
		Bitmap dstBitmap = null;
		try {
			if (!BitmapHelper.isBitmapInvalid(bitmap)) {
				dstBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(0xff424242);
				
		        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
		        final RectF rectF = new RectF(rect);  
		        final float roundPx = pixels;  
		        
		        Canvas canvas = new Canvas(dstBitmap); 
		        canvas.drawARGB(0, 0, 0, 0);  
		        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
		  
		        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
		        canvas.drawBitmap(bitmap, rect, rect, paint);  
		  
		        canvas.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return dstBitmap;  
    }
}
