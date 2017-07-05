package miles.lee.ms.ui.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import miles.lee.ms.ui.BasePresenter;
import miles.lee.ms.ui.BaseView;

/**
 * Created by miles on 2017/5/26 0026.
 */

public class RxPresenter<T extends BaseView> implements BasePresenter<T>{
    protected T view;
    private CompositeDisposable mCompositeDisposable;

    protected void removeSubscribe(Disposable subscription){
        if(subscription == null){
            return;
        }
        if(mCompositeDisposable != null){
            mCompositeDisposable.remove(subscription);
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    protected <U> void addRxBusSubscribe(Class<U> eventType, Consumer<U> act){
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
//        mCompositeDisposable.add(RxBus.getDefault().toDefaultFlowable(eventType, act));
    }

    @Override
    public void attchView(T view){
        this.view = view;

    }

    @Override
    public void detachView(){
        if(mCompositeDisposable!=null){
            mCompositeDisposable.dispose();
        }
    }
}
