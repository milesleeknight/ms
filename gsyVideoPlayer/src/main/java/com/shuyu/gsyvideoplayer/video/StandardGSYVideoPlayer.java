package com.shuyu.gsyvideoplayer.video;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ijk.player.R;
import com.shuyu.gsyvideoplayer.GSYImageCover;
import com.shuyu.gsyvideoplayer.GSYTextureView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.interfaces.LockClickListener;
import com.shuyu.gsyvideoplayer.model.SwitchVideoModel;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYSettings;
import com.shuyu.gsyvideoplayer.utils.MeasureHelper;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * @author yinhui
 * @classname: StandardGSYVideoPlayer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/4/25 9:57
 */
public class StandardGSYVideoPlayer extends GSYVideoPlayer implements SeekBar.OnSeekBarChangeListener {

    private static final int MESSAGE_SHOW_PROGRESS = 1;
    private static final int MESSAGE_FADE_OUT = 2;
    private static final int MESSAGE_SEEK_NEW_POSITION = 3;
    private static final int MESSAGE_HIDE_CENTER_BOX = 4;
    private static final int MESSAGE_RESTART_PLAY = 5;
    private static final int MESSAGE_SHOW_NET_SPEED = 6;
    private static final int MESSAGE_HIDE_HISTORY = 7;


    // 暂停时从后台切换至前台是显示最后一帧图像的控件
    protected GSYImageCover cover;
    // top controller
    protected RelativeLayout app_video_top_box;
    protected ImageView app_video_finish;
    protected TextView app_video_title;
    protected ImageView app_video_more;
    // bottom controller
    protected LinearLayout app_video_bottom_box;
    protected ImageView app_video_play;
    protected TextView app_video_currentTime;
    protected SeekBar app_video_seekBar;
    protected TextView app_video_endTime;
    protected TextView app_video_resolution;
    protected ImageView app_video_fullscreen;
    // 音量
    protected LinearLayout app_video_volume_box;
    protected ImageView app_video_volume_icon;
    protected TextView app_video_volume;
    // 亮度
    protected LinearLayout app_video_brightness_box;
    protected TextView app_video_brightness;
    // fast forward
    protected LinearLayout app_video_fastForward_box;
    protected TextView app_video_fastForward;
    protected TextView app_video_fastForward_target;
    protected TextView app_video_fastForward_all;
    // 续看
    protected LinearLayout app_video_history;
    protected ImageView app_video_history_close;
    protected TextView app_video_history_tip;
    protected TextView app_video_history_continue;
    // loading
    protected LinearLayout app_video_loading;
    protected ProgressBar loading_progressbar;
    protected TextView tv_net_speed;
    // error area
    protected LinearLayout app_video_status;
    protected TextView app_video_status_text;
    protected TextView app_video_status_refresh;

    protected LockClickListener mLockClickListener;//点击锁屏的回调
    protected boolean mLockCurScreen;//锁定屏幕点击
    protected boolean mNeedLockFull;//是否需要锁定屏幕

    // 只保存观看时长大于1分钟的续看点
    private final long LIMIT_POINT_TIME = 60*1000;

    /**
     * -----------------------------------------------------------------------
     */
    // 手势滑动标识
    protected  boolean isProgressSlide = false;
    // 手势seek的位置
    protected  long newPosition = -1;
    // 是否正在拖到进度条
    protected  boolean isDragging = false;
    // controller隐藏的延时时间
    protected  int defaultRetryTime = 4000;
    // 缓冲进度
    protected  int mBuffterPoint;
    // 当前音量
    protected  int mVolume = -1;
    // 当前亮度
    protected  float mBrightness = -1;
    // 续看点
    protected  long lastPosition = 0;
    // 多清晰切换或者其他需要从已观看位置继续的position
    protected long mSeekOnStart = -1; //从哪个开始播放
    // 当前播放器所在页面的可见状态
    protected boolean isPageVisible = false;
    // controller显示隐藏标识
    protected  boolean isShowing = false;
    // 是否只显示全屏
    protected  boolean fullScreenOnly = false;
    protected  GestureDetector gestureDetector;

    protected  MeasureHelper mMeasureHelper;
//    protected  PopupWindow mResolutionPopupWindow;

    private StandardOnClickListener mOnclickListener;

    public StandardGSYVideoPlayer(Context context) {
        super(context);
    }

    public StandardGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StandardGSYVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StandardGSYVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLayoutId() {
        return R.layout.gsy_standard_video_player;
    }

    @Override
    protected void init() {
        super.init();
        mMeasureHelper = new MeasureHelper(this);
        mMeasureHelper.setAspectRatio(mCurrentAspectRatio);
        mOnclickListener = new StandardOnClickListener();
        // 初始化控件
        surface_container = (FrameLayout) findViewById(R.id.surface_container);
        cover = (GSYImageCover) findViewById(R.id.cover);// cover
        cover.setAspectRatio(mCurrentAspectRatio);
        app_video_seekBar = (SeekBar) findViewById(R.id.app_video_seekBar);

        // top controller
        app_video_top_box = (RelativeLayout) findViewById(R.id.app_video_top_box);
        app_video_finish = (ImageView) findViewById(R.id.app_video_finish);
        app_video_title = (TextView) findViewById(R.id.app_video_title);

        app_video_more = (ImageView) findViewById(R.id.app_video_more);
        // bottom controller
        app_video_bottom_box = (LinearLayout) findViewById(R.id.app_video_bottom_box);
        app_video_play = (ImageView) findViewById(R.id.app_video_play);
        app_video_currentTime = (TextView) findViewById(R.id.app_video_currentTime);
        app_video_seekBar = (SeekBar) findViewById(R.id.app_video_seekBar);
        app_video_endTime = (TextView) findViewById(R.id.app_video_endTime);
        app_video_resolution = (TextView) findViewById(R.id.app_video_resolution);
        app_video_fullscreen = (ImageView) findViewById(R.id.app_video_fullscreen);
        // 音量
        app_video_volume_box = (LinearLayout) findViewById(R.id.app_video_volume_box);
        app_video_volume_icon = (ImageView) findViewById(R.id.app_video_volume_icon);
        app_video_volume = (TextView) findViewById(R.id.app_video_volume);
        // 亮度
        app_video_brightness_box = (LinearLayout) findViewById(R.id.app_video_brightness_box);
        app_video_brightness = (TextView) findViewById(R.id.app_video_brightness);
        // fast forward
        app_video_fastForward_box = (LinearLayout) findViewById(R.id.app_video_fastForward_box);
        app_video_fastForward = (TextView) findViewById(R.id.app_video_fastForward);
        app_video_fastForward_target = (TextView) findViewById(R.id.app_video_fastForward_target);
        app_video_fastForward_all = (TextView) findViewById(R.id.app_video_fastForward_all);
        // 续看
        app_video_history = (LinearLayout) findViewById(R.id.app_video_history);
        app_video_history_close = (ImageView) findViewById(R.id.app_video_history_close);
        app_video_history_tip = (TextView) findViewById(R.id.app_video_history_tip);
        app_video_history_continue = (TextView) findViewById(R.id.app_video_history_continue);

        // loading
        app_video_loading = (LinearLayout) findViewById(R.id.app_video_loading);
        loading_progressbar = (ProgressBar) findViewById(R.id.loading_progressbar);
        tv_net_speed = (TextView) findViewById(R.id.tv_net_speed);

        loading_progressbar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#fb7299"), PorterDuff.Mode.SRC_IN);
        // error area
        app_video_status = (LinearLayout) findViewById(R.id.app_video_status);
        app_video_status_text = (TextView) findViewById(R.id.app_video_status_text);
        app_video_status_refresh = (TextView) findViewById(R.id.app_video_status_refresh);
        /** 设置监听 */
        app_video_seekBar.setMax(1000);
        app_video_seekBar.setOnSeekBarChangeListener(this);
        //
        app_video_finish.setOnClickListener(mOnclickListener);
        app_video_more.setOnClickListener(mOnclickListener);
        //
        app_video_play.setOnClickListener(mOnclickListener);
        app_video_resolution.setOnClickListener(mOnclickListener);
        app_video_fullscreen.setOnClickListener(mOnclickListener);
        //
        app_video_history_close.setOnClickListener(mOnclickListener);
        app_video_history_continue.setOnClickListener(mOnclickListener);
        //
        app_video_status_refresh.setOnClickListener(mOnclickListener);

        /** 手势 */
        // 手势
        gestureDetector = new GestureDetector(mAppContext, new PlayerGestureListener());
        this.setClickable(true);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    return true;
                }
                // 处理手势结束
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                return false;
            }
        });

//        mLockScreen.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE ||
//                        mCurrentState == CURRENT_STATE_ERROR) {
//                    return;
//                }
//                lockTouchLogic();
//                if (mLockClickListener != null) {
//                    mLockClickListener.onClick(v, mLockCurScreen);
//                }
//            }
//        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
    }

    /**
     * 多清晰度播放
     *
     * @param urls          播放url集合
     * @param cacheWithPlay 是否边播边缓存
     * @param objects       object[0]目前为title
     * @return
     */
    public boolean setUp(List<SwitchVideoModel> urls, int codingGradePosition, boolean cacheWithPlay, Object[] objects) {
        return setUp(urls, codingGradePosition, cacheWithPlay, null, objects);
    }

    /**
     * 多清晰度播放
     *
     * @param urls          播放url集合
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param objects       object[0]目前为title
     * @return
     */
    public boolean setUp(List<SwitchVideoModel> urls, int codingGradePosition, boolean cacheWithPlay, File cachePath, Object[] objects) {
        return setUp(urls, codingGradePosition, cacheWithPlay, cachePath, null, objects);
    }

    public boolean setUp(List<SwitchVideoModel> urls, int codingGradePosition, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object[] objects) {
        if (urls == null || urls.isEmpty()) {
            app_video_resolution.setText("");
            return false;
        } else {
            this.mUrlList = urls;
            if (codingGradePosition < mUrlList.size()) {
                mCodingGradePosition = codingGradePosition;
            } else {
                mCodingGradePosition = 0;
            }
            SwitchVideoModel model = mUrlList.get(mCodingGradePosition);
            app_video_resolution.setText(model.getName());
            return setUp(model.getUrl(), cacheWithPlay, cachePath, mapHeadData, objects);
        }
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects       object[0]目前为title
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, Object[] objects) {
        return setUp(url, cacheWithPlay, null, objects);
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param objects       object[0]目前为title
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object[] objects) {
        if (super.setUp(url, cacheWithPlay, cachePath, objects)) {
            if (objects != null && objects.length > 0) {
                app_video_title.setText(objects[0].toString());
            }
            return true;
        }
        return false;
    }

    /**
     * 暂停状态
     */
    @Override
    public void onVideoPause() {
        if (GSYVideoManager.instance().getAlertWindow() != null) {// 切换悬浮窗是不暂停播放
            return;
        }
        if (GSYVideoManager.instance().pause()) {
            mPauseTime = System.currentTimeMillis();
            mCurrentPosition = getCurrentPositionWhenPlaying();
            if (checkVideoCallBackAndMediaListener()) {
                Debuger.printfLog("onPlayingToPause");
                mGSYCallBack.onPlayingToPause(mUrl, mObjects);
            }
            setStateAndUi(GSYVideoManager.STATE_PAUSED);
        }
    }

    /**
     * 恢复暂停状态
     */
    @Override
    public void onVideoResume() {
        if (!isPageVisible || getCurrentState() != GSYVideoManager.STATE_PAUSED) {// 如果当前播放器所在页面时不可见状态或者当前不是暂停状态，忽略恢复暂停操作
            return;
        }
        if(isLive()){
            GSYVideoManager.instance().releaseMediaPlayer();
            startPlayLogic();
        } else if(GSYVideoManager.instance().start()){
            mPauseTime = 0;
            if (checkVideoCallBackAndMediaListener()) {
                Debuger.printfLog("onPauseToPlaying");
                mGSYCallBack.onPauseToPlaying(mUrl, mObjects);
            }
            setStateAndUi(GSYVideoManager.STATE_PLAYING);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        GSYVideoManager.instance().setDisplay(mSurface);
        //显示暂停切换显示的图片
        showPauseCover();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        GSYVideoManager.instance().setDisplay(null);
        surface.release();
//        cancelProgressTimer();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //如果播放的是暂停全屏了
        releasePauseCover();
    }

    @Override
    public void onPrepared() {
        mIsPrepared = true;

        GSYVideoManager.instance().start();

        setStateAndUi(GSYVideoManager.STATE_PLAYING);

        if (checkVideoCallBackAndMediaListener()) {
            Debuger.printfLog("onPrepared");
            mGSYCallBack.onPrepared(mUrl, mObjects);
        }

        if (GSYVideoManager.instance().getMediaPlayer() != null && !mIsLive && mSeekOnStart > 0) {
            GSYVideoManager.instance().getMediaPlayer().seekTo(mSeekOnStart);
            mSeekOnStart = 0;
        }

    }

    /**
     * 从哪里开始播放
     * 目前有时候前几秒有跳动问题，毫秒
     * 需要在startPlayLogic之前，即播放开始之前
     */
    public void setSeekOnStart(long seekOnStart) {
        this.mSeekOnStart = seekOnStart;
    }

    @Override
    public void onError(int what, int extra) {
        showErrorArea(true, getErrorMsg(what));
        if (what != 38 && what != -38) {
            setStateAndUi(GSYVideoManager.STATE_ERROR);
            deleteCacheFileWhenError();
            if (checkVideoCallBackAndMediaListener()) {
                mGSYCallBack.onPlayError(mUrl, mObjects);
            }
        }
    }

    /**
     * app_video_status的显示与隐藏
     *
     * @param show
     * @param errorMsg
     */
    private void showErrorArea(boolean show, String errorMsg) {
        app_video_status_text.setText(errorMsg);
        app_video_status.setVisibility(show ? VISIBLE : GONE);
    }

    @Override
    public void onInfo(int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            setStateAndUi(GSYVideoManager.STATE_LOADING);
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            setStateAndUi(GSYVideoManager.STATE_LOADED);
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            setStateAndUi(GSYVideoManager.STATE_LOADED);
            showContinuePoint();
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            mRotate = extra;
            if (mTextureView != null)
                mTextureView.setVideoRotation(mRotate);
        }
    }

    private void showContinuePoint() {
        if (lastPosition >= 60000 && getCurrentState() != GSYVideoManager.STATE_PLAYBACK_COMPLETED && mSeekOnStart <= 0) {
            app_video_history_tip.setText(mAppContext.getString(R.string.last_view_point, CommonUtil.stringForTime(lastPosition)));
            app_video_history.setVisibility(VISIBLE);
            mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_HISTORY,10000);
        }
    }

    @Override
    public void onVideoSizeChanged() {
        int mVideoWidth = GSYVideoManager.instance().getCurrentVideoWidth();
        int mVideoHeight = GSYVideoManager.instance().getCurrentVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            mTextureView.requestLayout();
        }
    }

    @Override
    public void setStateAndUi(int state) {
        switch (state) {
            case GSYVideoManager.STATE_IDLE:
                if (isCurrentMediaListener()) {
                    GSYVideoManager.instance().listener().onCompletion();
                    GSYVideoManager.instance().releaseMediaPlayer();
                    releasePauseCoverAndBitmap();
                    mBuffterPoint = 0;
                }
                break;
            case GSYVideoManager.STATE_LOADING:
                showLoading(true);
                break;
            case GSYVideoManager.STATE_LOADED:
                showLoading(false);
                break;
            case GSYVideoManager.STATE_PREPARING:
                resetAll();
                break;
            case GSYVideoManager.STATE_PLAYING:
                updatePausePlay(true);
                break;
            case GSYVideoManager.STATE_PAUSED:
                updatePausePlay(false);
                break;
            case GSYVideoManager.STATE_PLAYBACK_COMPLETED:
                app_video_seekBar.setProgress(1000);
                break;
            case GSYVideoManager.STATE_ERROR:
                if (isCurrentMediaListener()) {
                    GSYVideoManager.instance().releaseMediaPlayer();
                }
                break;
        }
    }

    /**
     * app_video_loading的显示与隐藏
     *
     * @param show
     */
    private void showLoading(boolean show) {
        if (show) {
            mHandler.sendEmptyMessage(MESSAGE_SHOW_NET_SPEED);
            app_video_loading.setVisibility(VISIBLE);
        } else {
            tv_net_speed.setText("");
            mHandler.removeMessages(MESSAGE_SHOW_NET_SPEED);
            app_video_loading.setVisibility(GONE);
        }
    }

    /**
     * 一开始播放时，提示用户当前网络状态的提示框
     */
    protected void showWifiDialog() {
        if (!NetworkUtils.isAvailable(mAppContext)) {
            Toast.makeText(mAppContext, mAppContext.getString(R.string.no_net), Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startPlayLogic();
            }
        });
        builder.setNegativeButton(R.string.tips_not_wifi_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 播放过程中网络变化弹出的友好提示框
     */
    public void showFriendlyDialog() {
        if (!NetworkUtils.isAvailable(mAppContext)) {
            Toast.makeText(mAppContext, mAppContext.getString(R.string.no_net), Toast.LENGTH_LONG).show();
            return;
        }
        if (getCurrentState() != GSYVideoManager.STATE_PLAYING || getCurrentState() == GSYVideoManager.STATE_PAUSED) {
            return;
        }
        // 先暂停播放,等待用户选择
        onVideoPause();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onVideoResume();
            }
        });
        builder.setNegativeButton(R.string.tips_not_wifi_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void addTextureView() {
        if (surface_container.getChildCount() > 0) {
            surface_container.removeAllViews();
        }
        mTextureView = null;
        mTextureView = new GSYTextureView(getContext());
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setAspectRatio(mCurrentAspectRatio);
        mTextureView.setVideoRotation(mRotate);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        surface_container.addView(mTextureView, layoutParams);
    }

    /**
     * 显示暂停切换显示的bitmap
     */
    @Override
    protected void showPauseCover() {
        try {
            if (getCurrentState() == GSYVideoManager.STATE_PAUSED && mFullPauseBitmap != null && !mFullPauseBitmap.isRecycled()) {
                cover.setRotation(mTextureView.getRotation());
                cover.setImageBitmap(mFullPauseBitmap);
                cover.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void releasePauseCover() {
        try {
            if (getCurrentState() != GSYVideoManager.STATE_PAUSED && mFullPauseBitmap != null
                    && !mFullPauseBitmap.isRecycled()) {
                cover.setImageResource(R.drawable.empty_drawable);
                cover.setVisibility(GONE);
                //如果在这里销毁，可能会draw a recycler bitmap error
                mFullPauseBitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void releasePauseCoverAndBitmap() {
        try {
            if (mFullPauseBitmap != null && !mFullPauseBitmap.isRecycled()) {
                cover.setImageResource(R.drawable.empty_drawable);
                cover.setVisibility(GONE);
                mFullPauseBitmap.recycle();
                mFullPauseBitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetAll() {
        // 隐藏错误panel
        showErrorArea(false, "");
        // 隐藏续看
        mHandler.removeMessages(MESSAGE_HIDE_HISTORY);
        app_video_history.setVisibility(GONE);
        // 进度、时长等信息重置
        app_video_seekBar.setProgress(0);
        app_video_seekBar.setSecondaryProgress(0);
        app_video_currentTime.setText(CommonUtil.stringForTime(0));
        app_video_endTime.setText(CommonUtil.stringForTime(0));
        // 显示loading
        showLoading(true);
        // 延时隐藏controller
        show(defaultRetryTime);
    }

    public void startPlayLogicCheck() {
        if (TextUtils.isEmpty(mUrl)) {
            Toast.makeText(mAppContext, mAppContext.getString(R.string.no_url), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mUrl.startsWith("file") && !CommonUtil.isWifiConnected(getContext())) {
            showWifiDialog();
            return;
        }
        startPlayLogic();
    }

    @Override
    public void startPlayLogic() {
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }
        if (checkVideoCallBackAndMediaListener()) {
            Debuger.printfLog("startPlayLogic");
            mGSYCallBack.onStartPlayLogic(mUrl, mObjects);
        }
        prepareVideo();
    }

    /**
     * 开始状态视频播放
     */
    protected void prepareVideo() {
        GSYVideoManager.instance().setListener(this);
        GSYVideoManager.instance().setPlayTag(mPlayTag);
        GSYVideoManager.instance().setPlayPosition(mPlayPosition);
        addTextureView();
        mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        mBackUpPlayingBufferState = -1;
        GSYVideoManager.instance().prepare(mUrl, mMapHeadData, mLooping, mSpeed);
        setStateAndUi(GSYVideoManager.STATE_PREPARING);
    }

    @Override
    public void onAutoCompletion() {
        try {
            if (checkVideoCallBackAndMediaListener()) {
                Debuger.printfLog("onAutoComplete");
                mGSYCallBack.onAutoComplete(mUrl, mObjects);
            }
            setStateAndUi(GSYVideoManager.STATE_PLAYBACK_COMPLETED);
            if (surface_container.getChildCount() > 0) {
                surface_container.removeAllViews();
            }
            ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion() {
        if(checkVideoCallBackAndMediaListener()){
            int position = getCurrentPositionWhenPlaying();
            if(position == 0){// position大于0可表示之前播放过视频，此时是换源播放
                return;
            }
            mGSYCallBack.onAddHistory(mUrl,getCurrentState(),position,getCurrentCodingGradeName(),mObjects);
            if(position > LIMIT_POINT_TIME){
                mGSYCallBack.onSaveHistoryPoint(mUrl,mObjects,position);
            }
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        mBuffterPoint = percent;
    }

    @Override
    public void onSeekComplete() {

    }

    /**
     * 多分辨率选择popupwindow
     */
    private void createResolutionPopupwindow() {
        if (mUrlList == null || mUrlList.isEmpty()) {
            return;
        }
        View view = LayoutInflater.from(mAppContext).inflate(R.layout.player_resolution_popupwindow, null);
        final PopupWindow mResolutionPopupWindow = new PopupWindow(view, FrameLayout.LayoutParams.WRAP_CONTENT, getHeight(), true);
        mResolutionPopupWindow.setFocusable(true);
        mResolutionPopupWindow.setOutsideTouchable(true);
        mResolutionPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mAppContext, R.mipmap.shading));
        mResolutionPopupWindow.setAnimationStyle(R.style.resolution_pop_anim);
        ListView mResolutionListView = (ListView) view.findViewById(R.id.resolution_lv);
        ResolutionAdapter mResolutionAdapter = new ResolutionAdapter();
        mResolutionListView.setAdapter(mResolutionAdapter);
        mResolutionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mResolutionPopupWindow != null) {
                    mResolutionPopupWindow.dismiss();
                }
                if (position == mCodingGradePosition) {
                    return;
                }
                mCodingGradePosition = position;
                SwitchVideoModel model = mUrlList.get(position);
                app_video_resolution.setText(model.getName());
                int time = getCurrentPositionWhenPlaying();
                setUp(model.getUrl(), mCache, mCachePath, mObjects);
                setSeekOnStart(time);
                startPlayLogicCheck();
            }
        });
        mResolutionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (CommonUtil.isScreenLand((Activity) mContext.get()) && mOrientationUtils != null) {
                    mOrientationUtils.setEnable(true);
                }
            }
        });
        mResolutionPopupWindow.showAtLocation(this, Gravity.TOP | Gravity.END, 0, 0);
        if (CommonUtil.isScreenLand((Activity) mContext.get()) && mOrientationUtils != null) {
            mOrientationUtils.setEnable(false);
        }
    }

    private void selectDecoderTrack() {
        int listPosition = GSYSettings.isUsingMediaCodec(getContext()) ? 1 : 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.decoder_settings_title);
        builder.setSingleChoiceItems(R.array.decoder_array, listPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int listPosition) {
                boolean usingMediaCodec;
                if (listPosition == 0) {
                    usingMediaCodec = false;
                } else {
                    usingMediaCodec = true;
                }
                GSYSettings.usingMediaCodec(getContext(), usingMediaCodec);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void selectPlayTrack() {
        int listPosition = GSYSettings.isAutoPlayNext(getContext()) ? 1 : 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.play_settings);
        builder.setSingleChoiceItems(R.array.play_setting_array, listPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int listPosition) {
                boolean playNext;
                if (listPosition == 0) {
                    playNext = false;
                } else {
                    playNext = true;
                }
                GSYSettings.autoPlayNext(getContext(), playNext);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * seek续看点
     *
     * @param toContinue
     */
    private void doSeekHistoryPoint(boolean toContinue) {
        mHandler.removeMessages(MESSAGE_HIDE_HISTORY);
        if (!toContinue) {
            app_video_history.setVisibility(GONE);
            lastPosition = 0;
            return;
        }
        if (GSYVideoManager.instance().getMediaPlayer() != null && mIsPrepared) {
            GSYVideoManager.instance().getMediaPlayer().seekTo(lastPosition);
        }
        app_video_history.setVisibility(GONE);
        lastPosition = 0;
    }



    /**
     * 播放系统拍照声音
     */
    public void showShotEffect() {
        Animation animation = AnimationUtils.loadAnimation(mAppContext, R.anim.shot_fade_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cover.setBackgroundResource(android.R.color.white);
                cover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cover.setBackgroundResource(android.R.color.transparent);
                cover.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cover.startAnimation(animation);
        if (mAudioManager != null) {
            int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            if (volume != 0) {
                final MediaPlayer shootMP = MediaPlayer.create(mAppContext, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
                if (shootMP != null)
                    shootMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            shootMP.stop();
                            shootMP.release();
                        }
                    });
                shootMP.start();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;
        showErrorArea(false, "");
        if (GSYVideoManager.instance().getMediaPlayer() != null && mIsPrepared) {
            long duration = getDuration();
            long newPosition = (int) ((duration * progress * 1.0) / 1000);
            String time = CommonUtil.stringForTime(newPosition);
            app_video_currentTime.setText(time);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        show(3600000);
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (GSYVideoManager.instance().getMediaPlayer() != null && mIsPrepared) {
            long duration = getDuration();
            int time = (int) ((duration * seekBar.getProgress() * 1.0) / 1000);
            GSYVideoManager.instance().getMediaPlayer().seekTo(time);
        }
        show(1500);
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
        isDragging = false;
        mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
    }

    @Override
    public boolean handleMessage(Message msg) {
        boolean isAlreadyDeal = false;
        switch (msg.what) {
            case MESSAGE_FADE_OUT:
                hide(false);
                isAlreadyDeal = true;
                break;
            case MESSAGE_HIDE_CENTER_BOX:
                app_video_volume_box.setVisibility(GONE);
                app_video_brightness_box.setVisibility(GONE);
                app_video_fastForward_box.setVisibility(GONE);
                isAlreadyDeal = true;
                break;
            case MESSAGE_SEEK_NEW_POSITION:
                if (!mIsLive && newPosition >= 0 && GSYVideoManager.instance().getMediaPlayer() != null && mIsPrepared) {
                    GSYVideoManager.instance().getMediaPlayer().seekTo((int) newPosition);
                    newPosition = -1;
                }
                isAlreadyDeal = true;
                break;
            case MESSAGE_SHOW_PROGRESS:
                setProgress();
                if (!isDragging && isShowing && !isProgressSlide) {
                    mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
                }
                isAlreadyDeal = true;
                break;
            case MESSAGE_RESTART_PLAY:
                startPlayLogicCheck();
                isAlreadyDeal = true;
                break;
            case MESSAGE_SHOW_NET_SPEED:
                tv_net_speed.setText(getNetSpeedText());
                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_NET_SPEED, 1000);
                isAlreadyDeal = true;
                break;
            case MESSAGE_HIDE_HISTORY:
                app_video_history.setVisibility(GONE);
                lastPosition = 0;
                break;
        }
        return isAlreadyDeal;
    }

    /**
     * @param timeout
     */
    public void show(int timeout) {
        if (!isShowing) {
            app_video_top_box.setVisibility(VISIBLE);
//            if (!isLive) {
//                showBottomControl(true);
//            }
            showBottomControl(true);
            if (!fullScreenOnly) {
                app_video_fullscreen.setVisibility(VISIBLE);
            }
            isShowing = true;
        }
        updatePausePlay(getCurrentState() == GSYVideoManager.STATE_PLAYING);
        mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        mHandler.removeMessages(MESSAGE_FADE_OUT);
        if (timeout != 0) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_FADE_OUT, timeout);
        }
    }

    public void hide(boolean force) {
        if (force || isShowing) {
            mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
            showBottomControl(false);
            app_video_top_box.setVisibility(GONE);
            app_video_fullscreen.setVisibility(INVISIBLE);
            isShowing = false;
        }
    }

    private void showBottomControl(boolean show) {
        if (isProgressSlide && !show) {
            return;
        }
        if (mIsLive && show) {
            app_video_play.setVisibility(INVISIBLE);
            app_video_currentTime.setVisibility(INVISIBLE);
            app_video_endTime.setVisibility(INVISIBLE);
            app_video_seekBar.setVisibility(INVISIBLE);
            app_video_bottom_box.setVisibility(VISIBLE);
        } else {
            app_video_play.setVisibility(show ? VISIBLE : GONE);
            app_video_currentTime.setVisibility(show ? VISIBLE : GONE);
            app_video_endTime.setVisibility(show ? VISIBLE : GONE);
            app_video_seekBar.setVisibility(show ? VISIBLE : GONE);
            app_video_bottom_box.setVisibility(show ? VISIBLE : GONE);
        }
    }

    private void updatePausePlay(boolean isPlaying) {
        app_video_play.setImageResource(isPlaying ? R.mipmap.ic_stop : R.mipmap.ic_play_arrow);
    }

    public void setFullScreenOnly(boolean fullScreenOnly) {
        try {
            Activity activity = (Activity) mContext.get();
            this.fullScreenOnly = fullScreenOnly;
            app_video_fullscreen.setVisibility(INVISIBLE);
            if (fullScreenOnly) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;
        }
        hide(true);

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }
        // 显示
        app_video_volume_icon.setImageResource(i == 0 ? R.mipmap.ic_volume_off : R.mipmap.ic_volume);
        app_video_brightness_box.setVisibility(GONE);
        app_video_volume_box.setVisibility(VISIBLE);
        app_video_volume.setText(s);
        app_video_volume.setVisibility(VISIBLE);
    }

    private void onProgressSlide(float percent) {
        if (GSYVideoManager.instance().getMediaPlayer() == null || !mIsPrepared) {
            return;
        }
        long position = getCurrentPositionWhenPlaying();
        long duration = getDuration();
        if (position > duration) {
            position = duration * app_video_seekBar.getProgress() / 1000;
        }
        long deltaMax = Math.min(100 * 1000, duration - position);
        long delta = (long) (deltaMax * percent);


        newPosition = delta + position;
        if (newPosition > duration) {
            newPosition = duration;
        } else if (newPosition <= 0) {
            newPosition = 0;
            delta = -position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            app_video_fastForward_box.setVisibility(VISIBLE);
            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
            app_video_fastForward.setText(text + "s");
            String time = CommonUtil.stringForTime(newPosition);
            app_video_fastForward_target.setText(time + "/");
            app_video_fastForward_all.setText(CommonUtil.stringForTime(duration));
            app_video_currentTime.setText(time);
            int pos = (int) (1000L * newPosition / duration);
            app_video_seekBar.setProgress(pos);
        }
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        try {
            Activity activity = (Activity) mContext.get();
            if (mBrightness < 0) {
                mBrightness = activity.getWindow().getAttributes().screenBrightness;
                if (mBrightness <= 0.00f) {
                    mBrightness = 0.50f;
                } else if (mBrightness < 0.01f) {
                    mBrightness = 0.01f;
                }
            }
            Log.d(this.getClass().getSimpleName(), "brightness:" + mBrightness + ",percent:" + percent);
            app_video_brightness_box.setVisibility(VISIBLE);
            WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
            lpa.screenBrightness = mBrightness + percent;
            if (lpa.screenBrightness > 1.0f) {
                lpa.screenBrightness = 1.0f;
            } else if (lpa.screenBrightness < 0.01f) {
                lpa.screenBrightness = 0.01f;
            }
            app_video_brightness.setText(((int) (lpa.screenBrightness * 100)) + "%");
            activity.getWindow().setAttributes(lpa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long setProgress() {
        if (isDragging || GSYVideoManager.instance().getMediaPlayer() == null || !mIsPrepared) {
            return 0;
        }

        long position = getCurrentPositionWhenPlaying();
        long duration = getDuration();
        // 修正position大于duration的问题
        if (position > duration) {
            position = duration;
        }
        long pos = 0;
        if (app_video_seekBar != null) {
            if (duration > 0) {
                pos = 1000L * position / duration;
                app_video_seekBar.setProgress((int) pos);
            }
            app_video_seekBar.setSecondaryProgress(mBuffterPoint * 10);
        }
        app_video_currentTime.setText(CommonUtil.stringForTime(position));
        app_video_endTime.setText(CommonUtil.stringForTime(duration));
        return position;
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mHandler.removeMessages(MESSAGE_HIDE_CENTER_BOX);
        mHandler.sendEmptyMessage(MESSAGE_HIDE_CENTER_BOX);
        mVolume = -1;
        mBrightness = -1f;
        isProgressSlide = false;
        if (newPosition >= 0) {
            showBottomControl(false);
            hide(true);
            mHandler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            mHandler.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
        }
    }

    public void toggleFullScreen() {
        try {
            Activity activity = (Activity) mContext.get();
            if (CommonUtil.getScreenOrientation(activity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setFullScreen(Activity act, boolean fullScreen) {
        super.setFullScreen(act, fullScreen);
        boolean show = fullScreen && !mIsLocal;
        app_video_resolution.setVisibility(show ? VISIBLE : GONE);
    }

    @Override
    protected void updateFullScreenButton(int currentOrientation) {
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            app_video_fullscreen.setImageResource(R.mipmap.ic_fullscreen_exit);
        } else {
            app_video_fullscreen.setImageResource(R.mipmap.ic_fullscreen);
        }
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
    }

    @Override
    public void setLive(boolean isLive) {
        super.setLive(isLive);
        if (isLive) {
            app_video_play.setVisibility(View.INVISIBLE);
            app_video_currentTime.setVisibility(View.INVISIBLE);
            app_video_endTime.setVisibility(View.INVISIBLE);
            app_video_seekBar.setVisibility(View.INVISIBLE);
        } else {
            app_video_play.setVisibility(View.VISIBLE);
            app_video_currentTime.setVisibility(View.VISIBLE);
            app_video_endTime.setVisibility(View.VISIBLE);
            app_video_seekBar.setVisibility(View.VISIBLE);
        }
    }

    public void isPageVisible() {
        isPageVisible = true;
    }

    public void isPageInVisible() {
        isPageVisible = false;
    }

    class StandardOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.app_video_fullscreen) {
                toggleFullScreen();
            } else if (id == R.id.app_video_play) {
                if (getCurrentState() == GSYVideoManager.STATE_IDLE || getCurrentState() == GSYVideoManager.STATE_PLAYBACK_COMPLETED
                        || getCurrentState() == GSYVideoManager.STATE_ERROR) {
                    startPlayLogicCheck();
                } else if (getCurrentState() == GSYVideoManager.STATE_PLAYING) {
                    onVideoPause();
                } else if (getCurrentState() == GSYVideoManager.STATE_PAUSED) {
                    if (NetworkUtils.isWifiConnected(mAppContext)) {
                        onVideoResume();
                    } else {
                        showFriendlyDialog();
                    }
                }
            } else if (id == R.id.app_video_finish) {
                if (mGSYCallBack != null) {
                    mGSYCallBack.onClickBackView();
                }
            } else if (id == R.id.app_video_status_refresh) {// 视频播放错误后重新加载
                startPlayLogicCheck();
            } else if (id == R.id.app_video_history_continue) {
                doSeekHistoryPoint(true);
            } else if (id == R.id.app_video_history_close) {
                doSeekHistoryPoint(false);
            } else if (id == R.id.app_video_resolution) {
                createResolutionPopupwindow();
            } else if (id == R.id.app_video_more) {
                PopupMenu pop = new PopupMenu(getContext(), v, Gravity.RIGHT);
                pop.getMenuInflater().inflate(R.menu.settings, pop.getMenu());
                pop.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        if (CommonUtil.isScreenLand((Activity) mContext.get()) && mOrientationUtils != null) {
                            mOrientationUtils.setEnable(true);
                        }
                    }
                });
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_decoder_settings) {
                            selectDecoderTrack();
                        } else if (id == R.id.action_play_settings) {
                            selectPlayTrack();
                        }
                        return false;
                    }
                });
                pop.show();
                if (CommonUtil.isScreenLand((Activity) mContext.get()) && mOrientationUtils != null) {
                    mOrientationUtils.setEnable(false);
                }
            }
        }
    }

    class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean volumeControl;
        private boolean toSeek;

        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //TODO 暂时屏蔽
//            mTextureView.toggleAspectRatio();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            return super.onDown(e);
        }

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            try {
                float mOldX = e1.getX(), mOldY = e1.getY();
                float deltaY = mOldY - e2.getY();
                float deltaX = mOldX - e2.getX();
                if (firstTouch) {
                    toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                    volumeControl = mOldX > screenWidthPixels * 0.5f;
                    firstTouch = false;
                }

                if (toSeek) {
                    if (!mIsLive) {
                        isProgressSlide = true;
//                        if (!isShowing) {
//                            showBottomControl(true);
//                        }
                        onProgressSlide(-deltaX / getWidth());
                    }
                } else {
                    float percent = deltaY / getHeight();
                    if (volumeControl) {
                        onVolumeSlide(percent);
                    } else {
                        onBrightnessSlide(percent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
//            show(defaultTimeout);
            if (isShowing) {
                hide(true);
            } else {
                show(defaultRetryTime);
            }
            return true;
        }
    }

    class ResolutionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mUrlList == null ? 0 : mUrlList.size();
        }

        @Override
        public Object getItem(int position) {
            return mUrlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ResolutionAdapter.ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mAppContext, R.layout.player_resolution_item, null);
                holder = new ResolutionAdapter.ViewHolder();
                holder.rl_item_tv = (TextView) convertView.findViewById(R.id.resolution_item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ResolutionAdapter.ViewHolder) convertView.getTag();
            }
            if (mCodingGradePosition == position) {
                holder.rl_item_tv.setTextColor(Color.parseColor("#00ACDE"));
            } else {
                holder.rl_item_tv.setTextColor(ContextCompat.getColor(mAppContext, android.R.color.white));
            }
            SwitchVideoModel model = mUrlList.get(position);
            holder.rl_item_tv.setText(model.getName());
            return convertView;
        }

        class ViewHolder {
            TextView rl_item_tv;
        }
    }
}

