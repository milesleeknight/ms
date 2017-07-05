package miles.lee.ms.utils;

import android.content.Context;

/**
 * Created by miles on 2017/6/23 0023.
 */

public class DensityUtil{
    /**
     * 密度转化像素
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue *scale + 0.5f);
    }

    /**
     * 像素转化密度
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
