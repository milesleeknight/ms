package miles.lee.ms.push;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by miles on 2017/7/25 0025.
 */

public class PushAgent{
    private static final String TAG = "PushAgent";
    /**
     * 设置别名（需唯一），向指定别名推送消息时的标识
     * 设置别名为空字符串，可以取消别名
     * @param context
     * @param alias
     */
    public static void setAlias(Context context, String alias) {
        if (checkState(context)) {
            return;
        }
        if (alias == null) {
            alias = "";
        }
        YunBaManager.setAlias(context, alias, new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken arg) {
                Log.i(TAG,arg.toString());
            }

            @Override
            public void onFailure(IMqttToken arg0, Throwable arg) {
                Log.e(TAG,"error ", arg);
            }
        });
    }

    public static boolean checkState(Context context) {
        if (YunBaManager.isStopped(context)) {
            Log.w(TAG,"推送服务已经停止");
            YunBaManager.start(context);
            return true;
        }
        return false;
    }
}
