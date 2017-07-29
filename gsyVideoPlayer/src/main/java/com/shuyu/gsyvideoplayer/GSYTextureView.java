package com.shuyu.gsyvideoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import com.shuyu.gsyvideoplayer.utils.MeasureHelper;

/**
 * @author yinhui
 * @classname: GSYTextureView
 * @Description: TODO(用于显示video的，做了横屏与竖屏的匹配，还有需要rotation需求的)
 * @date 2017/5/2 14:37
 */
public class GSYTextureView extends TextureView {

    public static final int AR_ASPECT_FIT_PARENT = 0; // without clip
    public static final int AR_ASPECT_FILL_PARENT = 1; // may clip
    public static final int AR_ASPECT_WRAP_CONTENT = 2;
    public static final int AR_MATCH_PARENT = 3;
    public static final int AR_16_9_FIT_PARENT = 4;
    public static final int AR_4_3_FIT_PARENT = 5;

    private MeasureHelper mMeasureHelper;

    public GSYTextureView(Context context) {
        super(context);
        initView(context);
    }

    public GSYTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mMeasureHelper = new MeasureHelper(this);
    }

    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        setRotation(degree);
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