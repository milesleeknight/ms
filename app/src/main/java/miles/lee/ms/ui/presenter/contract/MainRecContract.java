package miles.lee.ms.ui.presenter.contract;

import java.util.List;

import miles.lee.ms.model.ChannelItem;
import miles.lee.ms.ui.BasePresenter;
import miles.lee.ms.ui.BaseView;

/**
 * Created by miles on 2017/5/31 0031.
 */

public interface MainRecContract{
    interface View extends BaseView{
        void showLoading();
        void initPages(List<ChannelItem> list);
    }
    interface Presenter extends BasePresenter<View>{
        void getTabsData();
    }
}
