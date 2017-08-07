package miles.lee.ms.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import miles.lee.ms.R;

/**
 * Created by miles on 2017/7/29 0029.
 */

public class LoadingDialog{
    public static Dialog createDialog(Context context, String msg, boolean cancelable){

        Dialog dialog = new Dialog(context,R.style.arlert);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading,null);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_message);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progresbar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                Color.parseColor("#fb2799"), PorterDuff.Mode.DST_IN);
        tvMsg.setText(msg);
        dialog.setCancelable(cancelable);
        dialog.setContentView(view,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                ,LinearLayout.LayoutParams.MATCH_PARENT));

        return dialog;
    }
}
