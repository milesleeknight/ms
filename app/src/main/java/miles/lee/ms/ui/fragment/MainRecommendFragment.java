package miles.lee.ms.ui.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.List;

import butterknife.BindView;
import miles.lee.ms.R;
import miles.lee.ms.model.ChannelItem;
import miles.lee.ms.ui.PresenterFragment;
import miles.lee.ms.ui.adapter.MultiPageAdapter;
import miles.lee.ms.ui.presenter.MainRecPresenter;
import miles.lee.ms.ui.presenter.contract.MainRecContract;
import miles.lee.ms.ui.widget.CustomerViewPager;

/**
 * Created by miles on 2017/5/27 0027.
 */

public class MainRecommendFragment extends PresenterFragment<MainRecContract.Presenter> implements MainRecContract.View{

    @BindView(R.id.rl_content)
    RelativeLayout relativeLayout;
    @BindView(R.id.tl)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.vp_recommend)
    CustomerViewPager viewPager;
    private MultiPageAdapter adapter;

    @Override
    protected void initEventAndData(){
        adapter = new MultiPageAdapter(getChildFragmentManager());
        mPresenter.getTabsData();
    }

    @Override
    protected void initInject(){
        mPresenter = new MainRecPresenter();
    }

    @Override
    protected int getLayoutRes(){
        return R.layout.frg_recommend;
    }

    @Override
    public void showLoading(){
        mVaryViewHelperController.showLoadingView(null);
    }

    @Override
    public void initPages(List<ChannelItem> list){
        mVaryViewHelperController.showContentView();
        if(list != null){
            for(ChannelItem item :list){
                String operationTagName = item.getOperationTagName();
                SectionFragment sectionFragment = SectionFragment.newInstance(item);
                adapter.addFrag(sectionFragment,operationTagName);
            }
        }
        viewPager.setAdapter(adapter);
        slidingTabLayout.setSnapOnTabClick(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void showError(String msg){
        mVaryViewHelperController.showErrorView(msg,retryListener);
    }

    private View.OnClickListener retryListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            mPresenter.getTabsData();
        }
    };

    @Override
    protected View getTargetLoadingView(){
        return relativeLayout;
    }
}
