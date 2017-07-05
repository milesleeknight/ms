package miles.lee.ms.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author yinhui
 * @classname: PPfunsViewPager
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/2/9 11:54
 */
public class PPfunsViewPager extends ViewPager{
    private boolean isCanScroll = true;

    public PPfunsViewPager(Context context) {
        super(context);
    }

    public PPfunsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && v instanceof LoopPlayImages){// 解决冲突
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isCanScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 修改多点触控放大缩小图片时的系统bug:java.lang.IllegalArgumentException: pointerIndex out of range
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (!isCanScroll) {
                return false;
            }
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
