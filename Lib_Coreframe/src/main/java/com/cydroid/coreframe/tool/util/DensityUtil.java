package com.cydroid.coreframe.tool.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
  
public class DensityUtil {  
//	public static int LISTWIDTH=260;
//	public static int MHEADHEIGHT=60;
//	public static int MFOOTHEIGHT=50;
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
   public static int sp2px(Context context, float spValue) {  
       final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
       return (int) (spValue * fontScale + 0.5f);  
   }
   public static int getStatusBarHeight(Context context){
       Class<?> c = null;
       Object obj = null;
       Field field = null;
       int x = 0, statusBarHeight = 0;
       try {
           c = Class.forName("com.android.internal.R$dimen");
           obj = c.newInstance();
           field = c.getField("status_bar_height");
           x = Integer.parseInt(field.get(obj).toString());
           statusBarHeight = context.getResources().getDimensionPixelSize(x);
       } catch (Exception e1) {
           e1.printStackTrace();
       } 
       return statusBarHeight;
   }
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }
} 