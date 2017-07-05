package miles.lee.ms.ui.presenter;

import com.tbruyelle.rxpermissions2.RxPermissions;

import miles.lee.ms.http.response.AppUpdateResponse;
import miles.lee.ms.ui.presenter.contract.MainContract;

/**
 * Created by miles on 2017/5/18 0018.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter{

    @Override
    public void bindDlna(){

    }

    @Override
    public void checkVersion(String currentVersion){
    }

    @Override
    public void checkUpdateState(RxPermissions rxPermissions, AppUpdateResponse appUpdateResponse){
    }

    @Override
    public void checkLocationPermission(RxPermissions rxPermissions){
    }

    @Override
    public void switchCity(String city){
    }

}
