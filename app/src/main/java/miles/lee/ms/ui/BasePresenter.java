package miles.lee.ms.ui;

/**
 * Created by miles on 2017/5/18 0018.
 */

public interface BasePresenter<T extends BaseView>{
    void attchView(T view);
    void detachView();
}
