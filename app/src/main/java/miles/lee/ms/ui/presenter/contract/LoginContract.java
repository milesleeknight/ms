package miles.lee.ms.ui.presenter.contract;

import miles.lee.ms.ui.BasePresenter;
import miles.lee.ms.ui.widget.LoadingBaseView;
import okhttp3.internal.platform.Platform;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public interface LoginContract{
    interface View extends LoadingBaseView{
        void toFinish();
    }
    interface Presenter extends BasePresenter<View>{
        void loginFromAccunt(String account,String pwd);
        void loginFromAuth(String phone,String code);
        void loginFromThird(Platform platform, String phone, String tokenId);
        void countdown();
    }
}
