package miles.lee.ms.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by miles on 2017/5/27 0027.
 */

public class CustomerViewPager extends ViewPager{
    private boolean canScroll = false;

    public CustomerViewPager(Context context){
        super(context);
    }

    public CustomerViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        if(!canScroll){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setCanScroll(boolean canScroll){
        this.canScroll = canScroll;
    }

}
