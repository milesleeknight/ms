package miles.lee.ms.ui;

/**
 * Created by miles on 2017/5/18 0018.
 */

public abstract class PresenterActivity<T extends BasePresenter> extends BaseActivity implements BaseView{
    protected T mPresenter;

    @Override
    protected void init(){
        initInject();
        if(mPresenter != null){
            mPresenter.attchView(this);
        }
        initEventAndData();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    protected abstract void initInject();
    protected abstract void initEventAndData();
}
