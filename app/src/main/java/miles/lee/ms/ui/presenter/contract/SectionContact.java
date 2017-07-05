package miles.lee.ms.ui.presenter.contract;

import miles.lee.ms.model.ChannelItem;
import miles.lee.ms.ui.BasePresenter;
import miles.lee.ms.ui.BaseView;
import miles.lee.ms.ui.adapter.section.Section;

/**
 * Created by miles on 2017/6/21 0021.
 */

public interface SectionContact {
    interface View extends BaseView{
        void addSection(Section section);
        void finishTask();
    }
    interface Presenter extends BasePresenter<View>{
        void initParams(ChannelItem item);
        void loadData();
    }
}
