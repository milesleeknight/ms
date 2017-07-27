package miles.lee.ms.ui.activity;

import android.content.DialogInterface;
import android.widget.ImageView;

import butterknife.BindView;
import miles.lee.ms.R;
import miles.lee.ms.ui.PresenterActivity;
import miles.lee.ms.ui.presenter.LoginPresenter;
import miles.lee.ms.ui.presenter.contract.LoginContract;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class LoginActivity extends PresenterActivity<LoginPresenter> implements LoginContract.View{

    @BindView(R.id.iv_cate)
    ImageView imageView;
    @Override
    public void showError(String msg){
    }

    @Override
    protected void initInject(){
//        mPresenter = new LoginPresenter(this);

    }

    @Override
    protected void initEventAndData(){
    }

    @Override
    protected int getContentView(){
        return R.layout.activity_login;
    }

    @Override
    public void showDialog(String msg, DialogInterface.OnDismissListener onDismissListener){
    }

    @Override
    public void dismissLoadingDialog(){
    }
}
