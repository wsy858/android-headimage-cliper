/*
 * DisplayUtil.java
 */
package evan.wang.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 显示相关工具类
 */
public class DisplayUtil {
    /**
     * convert px value to dip／dp value
     *
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert dip／dp value to dp value
     *
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * convert px value to sp value
     *
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * convert sp value to px value
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 获取文字高度
     *
     * @return
     */
    public static float getTextHeight(Paint textPaint) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top; //计算文字高度
        return fontHeight;
    }

    /**
     * 获取文字基线(baseline)高度
     */
    public static float getTextBaselineHeight(Paint textPaint) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        return Math.abs(fontMetrics.top);
    }

    /**
     * 获取文字的宽度
     */
    public static float getTextWidth(String text, Paint textPaint) {
        return textPaint.measureText(text);
    }

    /**
     * 获取多个文字的最大宽度
     *
     * @return
     */
    public static float getMaxTextWidth(Paint textPaint, String... text) {
        String[] strs = text;
        float max = 0;
        if (text.length > 0) {
            max = textPaint.measureText(text[0]);
            for (String str : strs) {
                float width = textPaint.measureText(str);
                if (width > max) {
                    max = width;
                }
            }
        }
        return max;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 获取底部navigation bar高度
     *
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    /**
     * 某些设备手机底部使用虚拟导航按键，某些设备使用物理导航按键
     * 判断是否含有底部navigation bar
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w("android", e);
        }

        return hasNavigationBar;

    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }


}