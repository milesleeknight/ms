package miles.lee.ms.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import miles.lee.ms.App;

/**
 * @author
 * @classname: Tips
 * @Description: Toast工具类
 * @date 2016/7/20 11:15
 */
public class Tips{
    /**
     * 显示Toast.
     */
    public static final int SHOW_TOAST = 0;

    /**
     * 主要Handler类，在线程中可用
     * what：0.提示文本信息
     */
    private static Handler baseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    showShortToast(msg.getData().getString("TEXT"));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     *
     * @param view
     * @param text
     */
    public static void showSnackbar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * @param text
     */
    public static void showShortToast(CharSequence text) {
        Toast toast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * @param text
     */
    public static void showLongToast(CharSequence text) {
        Toast toast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * @param resid
     */
    public static void showShortToast(int resid) {
        Toast.makeText(App.getInstance(), resid, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToastInThread(String text) {
        Message msg = baseHandler.obtainMessage(SHOW_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", text);
        msg.setData(bundle);
        baseHandler.sendMessage(msg);
    }

    public void showShortToastInThread(final int resid) {
        Message msg = baseHandler.obtainMessage(SHOW_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", App.getInstance().getResources().getString(resid));
        msg.setData(bundle);
        baseHandler.sendMessage(msg);
    }
}