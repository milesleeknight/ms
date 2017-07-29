package com.shuyu.gsyvideoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.utils.MeasureHelper;

/**
 * @author yinhui
 * @classname: GSYImageCover
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/5/2 14:42
 */
public class    GSYImageCover extends ImageView {

    private MeasureHelper mMeasureHelper;
    
    public GSYImageCover(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GSYImageCover(Context context) {
        super(context);
        initView(context);
    }

    public GSYImageCover(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mMeasureHelper = new MeasureHelper(this);
    }

    public void setAspectRatio(int aspectRatio) {
        mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
    }

}
