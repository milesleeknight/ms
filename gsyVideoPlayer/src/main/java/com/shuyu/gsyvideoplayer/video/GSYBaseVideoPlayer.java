package com.shuyu.gsyvideoplayer.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.GSYTextureView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.interfaces.GSYCallBack;
import com.shuyu.gsyvideoplayer.interfaces.GSYMediaPlayerListener;
import com.shuyu.gsyvideoplayer.model.SwitchVideoModel;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author yinhui
 * @classname: GSYBaseVideoPlayer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/4/25 10:08
 */
public abstract class GSYBaseVideoPlayer extends FrameLayout implements TextureView.SurfaceTextureListener,GSYMediaPlayerListener,Handler.Callback {

    /**
     * 针对列表类的播放(ListView)
     */
    protected String mPlayTag = ""; //播放的tag，防止错误，因为普通的url也可能重复
    protected int mPlayPosition = -22; //播放的tag，防止错误，因为普通的url也可能重复
    //
//    protected static int mBackUpPlayingBufferState = -1;
    protected long mPauseTime; //保存暂停时的时间
    protected long mCurrentPosition; //当前的播放位置

    protected WeakReference<Context> mContext;
    protected Context mAppContext;
    // 音量
    protected AudioManager mAudioManager;
    protected int mMaxVolume;
    //旋转工具类
    protected OrientationUtils mOrientationUtils;
    // 屏幕大小
    protected int screenWidthPixels;
    // 横竖屏标识
    protected boolean portrait;
    private boolean fullScreenOnly;// 只横屏显示
    // handler
    protected Handler mHandler;
    // 控件未全屏时的初始高度
    private int initHeight;
    // 播放过程中回调
    protected GSYCallBack mGSYCallBack;
    protected Map<String, String> mMapHeadData = new HashMap<>();
    // 是否播边边缓冲
    protected boolean mCache = false;
    protected File mCachePath;
    // 当前清晰度对象集合与下标
    protected List<SwitchVideoModel> mUrlList;
    public int mCodingGradePosition = 0;
    // 原来的url与经过缓存转换过的url
    protected String mOriginUrl;
    protected String mUrl;
    protected Object[] mObjects;
    //是否是缓存的文件
    protected boolean mCacheFile = false;
    //循环播放，默认关闭
    protected boolean mLooping = false;
    //倍速播放，只支持6.0以上
    protected float mSpeed = 1;
    //针对某些视频的旋转信息做了旋转处理
    protected int mRotate = 0;
    //是否播放过
    public boolean mIsPrepared = false;
    // 是否是直播
    protected boolean mIsLive = false;
    // 是否是本地播放
    protected boolean mIsLocal = false;
    protected Bitmap mFullPauseBitmap = null;//暂停时的全屏图片；
    // 纹理画布
    protected ViewGroup surface_container;
    protected GSYTextureView mTextureView;
    protected Surface mSurface;

    private static final int[] s_allAspectRatio = {
            GSYTextureView.AR_ASPECT_FIT_PARENT,
            GSYTextureView.AR_ASPECT_FILL_PARENT,
            GSYTextureView.AR_ASPECT_WRAP_CONTENT,
            GSYTextureView.AR_MATCH_PARENT,
            GSYTextureView.AR_16_9_FIT_PARENT,
            GSYTextureView.AR_4_3_FIT_PARENT
    };
    private int mCurrentAspectRatioIndex = 4;
    protected int mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];

    public GSYBaseVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public GSYBaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GSYBaseVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GSYBaseVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = new WeakReference<>(context);
        mAppContext = context.getApplicationContext();
        // 初始化音量
        mAudioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //
        screenWidthPixels = mAppContext.getResources().getDisplayMetrics().widthPixels;
        //
        mHandler = new Handler(this);
        ViewTreeObserver vto2 = this.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initHeight = getHeight();
                if (Build.VERSION.SDK_INT < 16) {
                    removeLayoutListenerPre16(getViewTreeObserver(), this);
                } else {
                    removeLayoutListenerPost16(getViewTreeObserver(), this);
                }
            }
        });
        init();
    }

    @SuppressWarnings("deprecation")
    private void removeLayoutListenerPre16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeGlobalOnLayoutListener(listener);
    }

    @TargetApi(16)
    private void removeLayoutListenerPost16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    public int toggleAspectRatio() {
        mCurrentAspectRatioIndex++;
        mCurrentAspectRatioIndex %= s_allAspectRatio.length;

        mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];
        if (mTextureView != null)
            mTextureView.setAspectRatio(mCurrentAspectRatio);
        return mCurrentAspectRatio;
    }

    public void doOnConfigurationChanged(Configuration newConfig) {
        try {
            final Activity act = (Activity) mContext.get();
            if(mOrientationUtils == null){
                mOrientationUtils = new OrientationUtils(act, this);
            }
            portrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
            if (act != null && !fullScreenOnly) {
                tryFullScreen(act,!portrait);
//                    updateBoxPadding(portrait);
                if (portrait) {
                    size(false,initHeight,false);
                    updateFullScreenButton(Configuration.ORIENTATION_PORTRAIT);
                } else {
                    int heightPixels = mAppContext.getResources().getDisplayMetrics().heightPixels;
                    int widthPixels = mAppContext.getResources().getDisplayMetrics().widthPixels;
                    size(false,Math.min(heightPixels, widthPixels),false);
                    updateFullScreenButton(Configuration.ORIENTATION_LANDSCAPE);
                }
                mOrientationUtils.setEnable(true);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    protected void tryFullScreen(Activity act,boolean fullScreen) {
        if (act instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) act).getSupportActionBar();
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide();
                } else {
                    supportActionBar.show();
                }
            }
        }
        setFullScreen(act,fullScreen);
    }

    protected void setFullScreen(Activity act,boolean fullScreen) {
        if (act != null) {
            Window window = act.getWindow();
            WindowManager.LayoutParams attrs = window.getAttributes();
            if (fullScreen) {
//                showOpButton(true);
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                window.setAttributes(attrs);
                window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                // 虚拟按键
                CommonUtil.hideNavKey(act);
                if(mGSYCallBack != null){
                    mGSYCallBack.onEnterFullscreen(mUrl,mObjects);
                }
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } else {
//                showOpButton(false);
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                window.setAttributes(attrs);
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                CommonUtil.showNavKey(act,View.SYSTEM_UI_FLAG_VISIBLE);
                if(mGSYCallBack != null){
                    mGSYCallBack.onQuitFullscreen(mUrl,mObjects);
                }
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    private void size(boolean width, int n, boolean dip) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (n > 0 && dip) {
            n = CommonUtil.dip2pixel(mAppContext, n);
        }
        if (width) {
            lp.width = n;
        } else {
            lp.height = n;
        }
        setLayoutParams(lp);
    }

    /**
     * 设置播放过程中的回调
     *
     * @param mVideoAllCallBack
     */
    public void setGSYCallBack(GSYCallBack mVideoAllCallBack) {
        this.mGSYCallBack = mVideoAllCallBack;
    }

    protected boolean isCurrentMediaListener() {
        return GSYVideoManager.instance().listener() != null
                && GSYVideoManager.instance().listener() == this;
    }

    protected boolean checkVideoCallBackAndMediaListener(){
        return (mGSYCallBack != null && isCurrentMediaListener());
    }

    /**
     * 获取当前播放状态
     */
    protected int getCurrentState() {
        return GSYVideoManager.instance().getCurrentState();
    }

    public float getSpeed() {
        return mSpeed;
    }

    /**
     * 播放速度
     */
    public void setSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mSpeed = speed;
            if (GSYVideoManager.instance().getMediaPlayer() != null
                    && GSYVideoManager.instance().getMediaPlayer() instanceof IjkMediaPlayer) {
                if (speed != 1 && speed > 0) {
                    ((IjkMediaPlayer) GSYVideoManager.instance().getMediaPlayer()).setSpeed(speed);
                }
            }
        }
    }

    public void setLive(boolean isLive){
        this.mIsLive = isLive;
    }

    public boolean isLive(){
        return mIsLive;
    }

    public void setLocal(boolean isLocal){
        this.mIsLocal = isLocal;
    }

    public String getCurrentCodingGradeName() {
        if (mUrlList == null || mUrlList.isEmpty()) {
            return "";
        } else {
            SwitchVideoModel model = mUrlList.get(mCodingGradePosition);
            return model.getName();
        }
    }

    public int getCurrentCodingGrade() {
        return mCodingGradePosition;
    }

    /**
     *
     */
    protected abstract void init();

    protected abstract void updateFullScreenButton(int currentOrientation);

    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects
     * @return
     */
    public abstract boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object[] objects);

    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param mapHeadData
     * @param objects
     * @return
     */
    public abstract boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object[] objects);


    protected abstract void setStateAndUi(int state);

    protected abstract void addTextureView();


    public boolean onBackPressed(){
        try {
            Activity activtiy = (Activity) mContext.get();
            if (!fullScreenOnly && CommonUtil.getScreenOrientation(activtiy) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                activtiy.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 页面销毁了记得调用是否所有的video
     */
    public void releaseAllVideos() {
        if(GSYVideoManager.instance().getAlertWindow() != null){
            if (mOrientationUtils != null)
                mOrientationUtils.releaseListener();
            setGSYCallBack(null);
        }else {
            if(isCurrentMediaListener()){
                GSYVideoManager.instance().listener().onCompletion();
            }
            GSYVideoManager.instance().setListener(null);
            GSYVideoManager.instance().releaseMediaPlayer();
            if(mAudioManager != null){
                mAudioManager.abandonAudioFocus(null);
                mAudioManager = null;
            }
            if (mOrientationUtils != null)
                mOrientationUtils.releaseListener();
            if (mFullPauseBitmap != null && !mFullPauseBitmap.isRecycled()) {
                mFullPauseBitmap.recycle();
                mFullPauseBitmap = null;
            }
            setGSYCallBack(null);
        }
    }
}
