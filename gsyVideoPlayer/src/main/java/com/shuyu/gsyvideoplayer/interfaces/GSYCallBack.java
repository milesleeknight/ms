package com.shuyu.gsyvideoplayer.interfaces;

/**
 * @author yinhui
 * @classname: GSYCallBack
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017/5/5 10:48
 */
public interface GSYCallBack {

    //加载成功
    void onPrepared(String url, Object[] objects);

    //点击了错误状态下的开始按键
    void onStartPlayLogic(String url,Object[] objects);

    //点击了播放状态下的开始按键--->暂停
    void onPlayingToPause(String url, Object[] objects);

    //点击了暂停状态下的开始按键--->播放
    void onPauseToPlaying(String url, Object[] objects);

    //播放完了
    void onAutoComplete(String url, Object[] objects);

    //进去全屏
    void onEnterFullscreen(String url, Object[] objects);

    //退出全屏
    void onQuitFullscreen(String url, Object[] objects);

    //退出小窗口
    void onEnterAlertWidget(String url, Object[] objects);
//
//    //触摸调整声音
//    void onTouchScreenSeekVolume(String url, Object[] objects);
//
//    //触摸调整进度
//    void onTouchScreenSeekPosition(String url, Object[] objects);
//
//    //触摸调整亮度
//    void onTouchScreenSeekLight(String url, Object[] objects);

    //播放错误
    void onPlayError(String url, Object[] objects);

    void onClickBackView();

    void onSaveHistoryPoint(String mUrl, Object[] mObjects, int position);

    void onAddHistory(String url, int state, int position,String codingGradeName, Object[] objects);
}
