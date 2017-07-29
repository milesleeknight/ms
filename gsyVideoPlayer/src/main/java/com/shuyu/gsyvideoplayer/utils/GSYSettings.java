/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shuyu.gsyvideoplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GSYSettings {

    private static final String PREF_NAME = "player_settings";

//    public static final int PV_PLAYER__Auto = 0;
//    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public static int getPlayer(Context context) {
        int value = getSharedPreferences(context).getInt("pref.player_type", PV_PLAYER__IjkMediaPlayer);
        return value;
    }

    public static boolean isUsingMediaCodec(Context context) {
        return getSharedPreferences(context).getBoolean("pref.using_media_codec", false);
    }

    public static boolean isAutoPlayNext(Context context) {
        return getSharedPreferences(context).getBoolean("pref.auto_play_next",false);
    }

    /**
     * 关闭或开启硬解码，播放前设置
     * @param context
     * @param using true -> 开启，false -> 关闭
     */
    public static void usingMediaCodec(Context context, boolean using) {
        getSharedPreferences(context).edit().putBoolean("pref.using_media_codec",using).apply();
    }

    public static void autoPlayNext(Context context, boolean playNext) {
        getSharedPreferences(context).edit().putBoolean("pref.auto_play_next",playNext).apply();
    }
}
