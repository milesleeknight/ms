package miles.lee.ms.ui.fragment;

import miles.lee.ms.R;
import miles.lee.ms.ui.PresenterFragment;
import miles.lee.ms.ui.presenter.MainRecPresenter;

/**
 * Created by miles on 2017/5/27 0027.
 */

public class MainDirectBroadcastFragment extends PresenterFragment<MainRecPresenter>{
    @Override
    protected void initEventAndData(){
    }

    @Override
    protected void initInject(){
    }

    @Override
    protected int getLayoutRes(){
        return R.layout.activity_splash;
    }

    @Override
    public void showError(String msg){
    }
}
