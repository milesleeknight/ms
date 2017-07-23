package miles.lee.ms.ui.widget;

import android.content.DialogInterface;

import miles.lee.ms.ui.BaseView;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public interface LoadingBaseView extends BaseView{
    void showDialog(String msg, DialogInterface.OnDismissListener onDismissListener);
    void dismissLoadingDialog();
}
