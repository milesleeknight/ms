package miles.lee.ms.ui.presenter;

import miles.lee.ms.ui.presenter.contract.LoginContract;
import okhttp3.internal.platform.Platform;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void loginFromAccunt(String account, String pwd){

    }

    @Override
    public void loginFromAuth(String phone, String code){
    }

    @Override
    public void loginFromThird(Platform platform, String phone, String tokenId){
    }

    @Override
    public void countdown(){
    }
}
