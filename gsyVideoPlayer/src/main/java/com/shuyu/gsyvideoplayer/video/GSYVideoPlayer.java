package com.shuyu.gsyvideoplayer.video;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.StorageUtils;

import java.io.File;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * @author yinhui
 * @classname: GSYVideoPlayer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/4/25 10:04
 */
public abstract class GSYVideoPlayer extends GSYBaseVideoPlayer {

    public static final String TAG = "GSYVideoPlayer";


    public GSYVideoPlayer(Context context) {
        super(context);
    }

    public GSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GSYVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GSYVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        View.inflate(mAppContext, getLayoutId(), this);
        if (isInEditMode()){
            return;
        }
    }

    /**
     * 当前UI
     */
    public abstract int getLayoutId();

    /**
     * 开始播放
     */
    public abstract void startPlayLogic();

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects       object[0]目前为title
     * @return
     */
    public boolean setUp(String url, boolean cacheWithPlay, Object[] objects) {
        return setUp(url, cacheWithPlay, null, objects);
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param mapHeadData   头部信息
     * @param objects       object[0]目前为title
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object[] objects) {
        if (setUp(url, cacheWithPlay, cachePath, objects)) {
            this.mMapHeadData.clear();
            if (mapHeadData != null)
                this.mMapHeadData.putAll(mapHeadData);
            return true;
        }
        return false;
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
        mCache = cacheWithPlay;
        mCachePath = cachePath;
        mOriginUrl = url;

        if (cacheWithPlay && url.startsWith("http") && !url.contains("127.0.0.1")) {
            HttpProxyCacheServer proxy = GSYVideoManager.getProxy(getContext().getApplicationContext(), cachePath);
            //此处转换了url，然后再赋值给mUrl。
            url = proxy.getProxyUrl(url);
            mCacheFile = (!url.startsWith("http"));
            //注册上缓冲监听
            if (!mCacheFile && GSYVideoManager.instance() != null) {
                proxy.registerCacheListener(GSYVideoManager.instance(), mOriginUrl);
            }
        }
        this.mUrl = url;
        this.mObjects = objects;
        setStateAndUi(GSYVideoManager.STATE_IDLE);
        return true;
    }

    /**
     * 设置播放显示状态
     *
     * @param state
     */
    public abstract void setStateAndUi(int state);

    protected abstract void showWifiDialog();

    /**
     * 添加播放的view
     */
    public abstract void addTextureView();

    /**
     * 设置界面选择
     */
    public void setRotationView(int rotate) {
        this.mRotate = rotate;
        mTextureView.setRotation(rotate);
    }

    public void refreshVideo() {
        if (mTextureView != null) {
            mTextureView.requestLayout();
        }
    }



    /**
     * 显示暂停切换显示的bitmap
     */
    protected abstract void showPauseCover();

    /**
     * 销毁暂停切换显示的bitmap
     */
    protected abstract void releasePauseCover();

    /**
     * 销毁暂停切换显示的bitmap
     */
    protected abstract void releasePauseCoverAndBitmap();



    /**
     * 清除当前缓存
     */
    public void clearCurrentCache() {
        if (mCacheFile) {
            //是否为缓存文件
            Debuger.printfError(" mCacheFile Local Error " + mUrl);
            //可能是因为缓存文件除了问题
            CommonUtil.deleteFile(mUrl.replace("file://", ""));
            mUrl = mOriginUrl;
        } else if (mUrl.contains("127.0.0.1")) {
            //是否为缓存了未完成的文件
            Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
            String name = md5FileNameGenerator.generate(mOriginUrl);
            if (mCachePath != null) {
                String path = mCachePath.getAbsolutePath() + File.separator + name + ".download";
                CommonUtil.deleteFile(path);
            } else {
                String path = StorageUtils.getIndividualCacheDirectory
                        (getContext().getApplicationContext()).getAbsolutePath()
                        + File.separator + name + ".download";
                CommonUtil.deleteFile(path);
            }
        }

    }


    /**
     * 播放错误的时候，删除缓存文件
     */
    protected void deleteCacheFileWhenError() {
        clearCurrentCache();
        Debuger.printfError("Link Or mCache Error, Please Try Again" + mUrl);
        mUrl = mOriginUrl;
    }

    /**
     * 获取当前播放进度
     */
    public int getCurrentPositionWhenPlaying() {
        return GSYVideoManager.instance().getCurrentPosition();
    }

    /**
     * 获取当前总时长
     */
    public int getDuration() {
        return GSYVideoManager.instance().getDuration();
    }

    /**
     * if I am playing release me
     */
//    public void release() {
//        if (isCurrentMediaListener() &&
//                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
//            releaseAllVideos();
//        }
//        mHadPlay = false;
//    }


    /**
     * 播放tag防止错误，因为普通的url也可能重复
     */
    public String getPlayTag() {
        return mPlayTag;
    }

    /**
     * 播放tag防止错误，因为普通的url也可能重复
     *
     * @param playTag 保证不重复就好
     */
    public void setPlayTag(String playTag) {
        this.mPlayTag = playTag;
    }


    public int getPlayPosition() {
        return mPlayPosition;
    }

    /**
     * 设置播放位置防止错位
     */
    public void setPlayPosition(int playPosition) {
        this.mPlayPosition = playPosition;
    }

    /**
     * 网络速度
     * 注意，这里如果是开启了缓存，因为读取本地代理，缓存成功后还是存在速度的
     * 再打开已经缓存的本地文件，网络速度才会回0.因为是播放本地文件了
     */
    public long getNetSpeed() {
        return GSYVideoManager.instance().getNetSpeed();
    }

    /**
     * 网络速度
     * 注意，这里如果是开启了缓存，因为读取本地代理，缓存成功后还是存在速度的
     * 再打开已经缓存的本地文件，网络速度才会回0.因为是播放本地文件了
     */
    public String getNetSpeedText() {
        long speed = getNetSpeed();
        return CommonUtil.getTextSpeed(speed);
    }

    protected String getErrorMsg(int what) {
        String msg;
        switch (what) {
            case GSYVideoManager.PLAYER_INIT_ERROR:
                msg = "播放器初始化失败~";
                break;
            case IMediaPlayer.MEDIA_ERROR_UNKNOWN:
                msg = "未知错误,erorrCode:" + IMediaPlayer.MEDIA_ERROR_UNKNOWN;
                break;
            case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                msg = "播放器异常~";
                break;
            case IMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                msg = "暂不支持此类视频~";
                break;
            case IMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                msg = "连接超时，请检查网络连接";
                break;
            default:
                msg = "Fail to open file!";
                break;
        }
        return msg;
    }
}