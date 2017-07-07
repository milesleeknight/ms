package miles.lee.ms.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import miles.lee.ms.R;
import miles.lee.ms.model.ChannelItem;
import miles.lee.ms.ui.PresenterFragment;
import miles.lee.ms.ui.adapter.section.Section;
import miles.lee.ms.ui.adapter.section.SectionedRecyclerViewAdapter;
import miles.lee.ms.ui.presenter.SectionPresenter;
import miles.lee.ms.ui.presenter.contract.SectionContact;

/**
 * Created by miles on 2017/6/21 0021.
 */

public class SectionFragment extends PresenterFragment<SectionPresenter> implements SectionContact.View{
    @BindView(R.id.ll_content)
    LinearLayout linearLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter mAdapter;

    public static SectionFragment newInstance(ChannelItem item){
        Bundle bundle = new Bundle();
        SectionFragment fragment = new SectionFragment();
        bundle.putSerializable("Channel",item);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initEventAndData(){
        ChannelItem channelItem = (ChannelItem) getArguments().getSerializable("Channel");
        mPresenter.initParams(channelItem);
        initView();
        mPresenter.loadData();
    }

    private void initView(){
        mAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity,6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position){
                switch(position){
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_FOOTER:
                        return 6;
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_FILM:
                        return 2;
                    default:
                        return 3;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initInject(){
        mPresenter = new SectionPresenter(mActivity);
    }

    @Override
    protected int getLayoutRes(){
        return R.layout.frg_section;
    }

    @Override
    public void showError(String msg){
    }

    @Override
    public void addSection(Section section){
        mAdapter.addSection(section);
    }

    @Override
    protected View getTargetLoadingView(){
        return recyclerView;
    }

    @Override
    public void finishTask(){
        mAdapter.notifyDataSetChanged();
    }
}
