package miles.lee.ms.ui.activity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import miles.lee.ms.R;
import miles.lee.ms.ui.PresenterActivity;
import miles.lee.ms.ui.presenter.LoginPresenter;
import miles.lee.ms.ui.presenter.contract.LoginContract;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class LoginActivity extends PresenterActivity<LoginPresenter> implements LoginContract.View{

    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.btn_authcode)
    Button btn_authcode;
    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_mobile_num)
    EditText et_mobile_num;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_pw_login)
    TextView tv_pw_login;
    @BindView(R.id.tv_code_login)
    TextView tv_code_login;
    @BindView(R.id.tv_forget_pw)
    TextView tv_forget_pw;
    @BindView(R.id.tv_regist)
    TextView tv_regist;
    @BindView(R.id.iv_cancel)
    ImageView iv_cancel;
    @BindView(R.id.iv_pw_line)
    ImageView iv_pw_line;
    @BindView(R.id.iv_code_line)
    ImageView iv_code_line;
    @BindView(R.id.iv_weibo)
    ImageView iv_weibo;
    @BindView(R.id.iv_wechat)
    ImageView iv_wechat;
    @BindView(R.id.iv_qq)
    ImageView iv_qq;
    @BindView(R.id.iv_show_pw)
    ImageView iv_show_pw;
    @BindView(R.id.rl_code_login)
    RelativeLayout rl_code_login;
    @BindView(R.id.rl_password_login)
    RelativeLayout rl_password_login;
    @Override

    public void showError(String msg){
    }

    @Override
    protected void initInject(){
        mPresenter = new LoginPresenter(this);
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

    @OnClick({R.id.tv_pw_login,R.id.tv_code_login,R.id.iv_show_pw,R.id.btn_authcode,R.id.tv_regist
            ,R.id.btn_login,R.id.iv_cancel,R.id.iv_weibo,R.id.iv_wechat,R.id.iv_qq,R.id.tv_forget_pw})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.iv_cancel:
                dismissLoadingDialog();
                finish();
                break;
            case R.id.tv_regist:
                // TODO: 2017/7/28 0028
                break;
            case R.id.btn_login:
                String account = et_account.getText().toString();
                String pwd = et_password.getText().toString();
                mPresenter.loginFromAccunt(account,pwd);
                break;
        }
    }
}

