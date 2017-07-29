package miles.lee.ms.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import miles.lee.ms.R;

/**
 * Created by miles on 2017/7/29 0029.
 */

public class LoadingDialog{
    public static Dialog createDialog(Context context, String msg, boolean cancelable){

        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading,null);

        dialog.setContentView(view);

        return dialog;
    }
}
