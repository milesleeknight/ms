package miles.lee.ms.ui;

/**
 * Created by miles on 2017/5/31 0031.
 */

public abstract class PresenterFragment<T extends BasePresenter> extends BaseFragment implements BaseView{
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
    public void onDestroyView(){
        super.onDestroyView();
        if(mPresenter != null)
            mPresenter.detachView();
    }

    protected abstract void initEventAndData();

    protected abstract void initInject();
}
