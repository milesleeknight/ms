package miles.lee.ms.ui.presenter.contract;

import miles.lee.ms.ui.BasePresenter;
import miles.lee.ms.ui.BaseView;

/**
 * Created by miles on 2017/7/19 0019.
 */

public interface VideoDetailContract{
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{
        void getDetailById(String albumId,String cpId,int index);
    }
}
