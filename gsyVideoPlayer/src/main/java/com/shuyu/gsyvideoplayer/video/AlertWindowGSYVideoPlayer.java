package com.shuyu.gsyvideoplayer.video;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ijk.player.R;
import com.shuyu.gsyvideoplayer.GSYImageCover;
import com.shuyu.gsyvideoplayer.GSYTextureView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.SwitchVideoModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author yinhui
 * @classname: AlertWindowVideoPlayer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/4/28 13:56
 */
public abstract class AlertWindowGSYVideoPlayer extends GSYVideoPlayer implements View.OnTouchListener,View.OnClickListener {

    private static final int OVERLAY_TIMEOUT = 4000;
    private static final int FADE_OUT = 1;

    // 暂停时从后台切换至前台是显示最后一帧图像的控件
    GSYImageCover cover;
    RelativeLayout rl_overlay;
    ImageButton btn_close;
    ImageButton btn_back;
    TextView tv_title;

    // 双指touch
    private boolean isDouble = false;
    private float lastX;
    private float lastY;
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private int screenWidth = 0;
    private int screenHeight = 0;

    public AlertWindowGSYVideoPlayer(Context context) {
        super(context);
    }

    public AlertWindowGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlertWindowGSYVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlertWindowGSYVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        super.init();
        surface_container = (FrameLayout) findViewById(R.id.surface_container);
        cover = (GSYImageCover) findViewById(R.id.cover);// cover
        rl_overlay = (RelativeLayout) findViewById(R.id.rl_overlay);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_close = (ImageButton) findViewById(R.id.btn_close);
        tv_title = (TextView) findViewById(R.id.tv_title);

        btn_back.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        this.setOnTouchListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.gsy_alert_window_video_player;
    }

    public void addToWindow(){
        wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wmParams.gravity = Gravity.CENTER;// 设置屏幕中心点为坐标原点

        Display currentDisplay = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        currentDisplay.getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        screenWidth = Math.min(w,h);
        screenHeight = Math.max(w,h);
        int mWindowWidth = screenWidth / 3 * 2;
        int mWindowHeight = mWindowWidth / 16 * 9;
//      minWindowWidth = (screenWidth < screenHeight ? screenWidth : screenHeight) / 2;
        wmParams.width = mWindowWidth;
        wmParams.height = mWindowHeight;
        wmParams.x = screenWidth/2 - mWindowWidth/2;
        wmParams.y = 0/*(wmParams.height - ScreenHeight) / 2*//*(ScreenHeight - mWindowHeight) / 2*/;

        wm.addView(this, wmParams);
    }

    public void removeFromWindow() {
        GSYVideoManager.instance().setAlertWindow(null);
        if(isCurrentMediaListener()){
            releaseAllVideos();
        }
        wm.removeView(this);
    }

    public void setTitle(CharSequence title){
        tv_title.setText(title);
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
        return setUp(urls,codingGradePosition, cacheWithPlay,null,objects);
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
        return setUp(urls,codingGradePosition,cacheWithPlay, cachePath,null,objects);
    }

    public boolean setUp(List<SwitchVideoModel> urls, int codingGradePosition, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData,Object[]objects) {
        if (urls == null || urls.isEmpty()) {
            return false;
        }
        this.mUrlList = urls;
        if (codingGradePosition < mUrlList.size()) {
            mCodingGradePosition = codingGradePosition;
        } else {
            mCodingGradePosition = 0;
        }
        SwitchVideoModel model = mUrlList.get(mCodingGradePosition);
        if(setUp(model.getUrl(), cacheWithPlay, cachePath,mapHeadData,objects)){
            initParams();
        }
        return true;
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
            initParams();
            return true;
        }
        return false;
    }

    protected void initParams(){

    }


    @Override
    public void startPlayLogic() {
        if (TextUtils.isEmpty(mUrl)) {
            return;
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
//        mBackUpPlayingBufferState = -1;
        GSYVideoManager.instance().prepare(mUrl, mMapHeadData, mLooping, mSpeed);
    }

    @Override
    public void setStateAndUi(int state) {
        switch (state) {
            case GSYVideoManager.STATE_IDLE:
                if (isCurrentMediaListener()) {
                    GSYVideoManager.instance().releaseMediaPlayer();
                    releasePauseCoverAndBitmap();
                }
                break;
            case GSYVideoManager.STATE_ERROR:
                if (isCurrentMediaListener()) {
                    GSYVideoManager.instance().releaseMediaPlayer();
                }
                break;
        }
    }

    @Override
    protected void showWifiDialog() {

    }

    @Override
    public void addTextureView() {
        if (surface_container.getChildCount() > 0) {
            surface_container.removeAllViews();
        }
        mTextureView = null;
        mTextureView = new GSYTextureView(getContext());
        mTextureView.setAspectRatio(mCurrentAspectRatio);
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setVideoRotation(mRotate);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        surface_container.addView(mTextureView, layoutParams);
    }

    @Override
    protected void updateFullScreenButton(int currentOrientation) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case FADE_OUT:
                hideOverlay();
                break;
        }
        return false;
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
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //如果播放的是暂停全屏了
        releasePauseCover();
    }

    @Override
    protected void showPauseCover() {
        try {
            if (getCurrentState() == GSYVideoManager.STATE_PAUSED && mFullPauseBitmap != null
                    && !mFullPauseBitmap.isRecycled()) {
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

    @Override
    public void onPrepared() {
        mIsPrepared = true;
    }

    @Override
    public void onAutoCompletion() {
        removeFromWindow();
    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {
        removeFromWindow();
        Toast.makeText(mAppContext, getErrorMsg(what), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfo(int what, int extra) {

    }

    @Override
    public void onVideoSizeChanged() {

    }

    @Override
    public void onVideoPause() {
        GSYVideoManager.instance().pause();
    }

    @Override
    public void onVideoResume() {
        if(isLive()){
            GSYVideoManager.instance().releaseMediaPlayer();
            startPlayLogic();
        } else {
            GSYVideoManager.instance().start();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    isDouble = false;
                }
                if (isDouble == false) {
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                }
                showOverlay();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    isDouble = true;
                }
                if (isDouble == false) {
                    int deltaX = (int) (event.getRawX() - lastX);
                    lastX = event.getRawX();
                    int deltaY = (int) (event.getRawY() - lastY);
                    lastY = event.getRawY();
                    updateViewPosition(deltaX, deltaY);
                }
                break;

            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    public void showOverlay() {
        rl_overlay.setVisibility(View.VISIBLE);
        mHandler.removeMessages(FADE_OUT);
        Message msg = mHandler.obtainMessage(FADE_OUT);
        mHandler.sendMessageDelayed(msg, OVERLAY_TIMEOUT);
    }

    private void hideOverlay() {
        rl_overlay.setVisibility(View.GONE);
    }

    private void updateViewPosition(int deltaX, int deltaY) {// 待优化
        int newX = wmParams.x + deltaX;
        int newY = wmParams.y + deltaY;
        int leftX = newX + (screenWidth - wmParams.width) / 2;
        int rightX = newX + (screenWidth + wmParams.width) / 2;
        int topY = newY + (screenHeight - wmParams.height) / 2;
        int bottomY = newY + (screenHeight + wmParams.height) / 2;
        if (leftX < 0) {
            newX = (wmParams.width - screenWidth) / 2;
            leftX = newX + (screenWidth - wmParams.width) / 2;
            rightX = newX + (screenWidth + wmParams.width) / 2;
        }
        if (rightX > screenWidth + 2) {
            newX = (screenWidth - wmParams.width) / 2;
            leftX = newX + (screenWidth - wmParams.width) / 2;
            rightX = newX + (screenWidth + wmParams.width) / 2;
        }
        if (topY < 0) {
            newY = (wmParams.height - screenHeight) / 2;
            topY = newY + (screenHeight - wmParams.height) / 2;
            bottomY = newY + (screenHeight + wmParams.height) / 2;
        }
        if (bottomY > screenHeight) {
            newY = (screenHeight - wmParams.height) / 2;
            topY = newY + (screenHeight - wmParams.height) / 2;
            bottomY = newY + (screenHeight + wmParams.height) / 2;
        }
        boolean xmove = false;
        boolean ymove = false;
        if ((leftX >= 0 || deltaX > 0) && (rightX <= screenWidth || deltaX < 0)) {
            xmove = true;
        }
        if ((topY >= 0 || deltaY > 0) && (bottomY <= screenHeight || deltaY < 0)) {
            ymove = true;
        }
        if (!xmove && !ymove) {// 不移动
            return;
        }
        // 移动
        if (xmove) {
            wmParams.x = newX;
        }
        if (ymove) {
            wmParams.y = newY;
        }
        wm.updateViewLayout(this, wmParams);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_back){
            onBackClick();
        } else if (id == R.id.btn_close){
            onCloseClick();
        }
    }

    protected abstract void onBackClick();

    protected abstract void onCloseClick();

    public abstract StandardGSYVideoPlayer windowToNormal(StandardGSYVideoPlayer standardVideoPlayer);
}
