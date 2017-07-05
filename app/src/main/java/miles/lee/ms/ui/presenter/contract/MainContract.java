package miles.lee.ms.ui.presenter.contract;

import com.tbruyelle.rxpermissions2.RxPermissions;

import miles.lee.ms.http.response.AppUpdateResponse;
import miles.lee.ms.ui.BasePresenter;
import miles.lee.ms.ui.BaseView;

/**
 * Created by miles on 2017/5/18 0018.
 */

public interface MainContract{
    interface View extends BaseView{

        void showUpdateDialog(AppUpdateResponse appUpdateResponse);

        void startUpdateService(AppUpdateResponse appUpdateResponse);

        void setLocationCity(String city);

        void showLocationDialog(String designatedCity);
    }
    interface Presenter extends BasePresenter<View>{
        void bindDlna();
        void checkVersion(String currentVersion);
        void checkUpdateState(RxPermissions rxPermissions,AppUpdateResponse appUpdateResponse);
        void checkLocationPermission(RxPermissions rxPermissions);
        void switchCity(String city);
    }
}
