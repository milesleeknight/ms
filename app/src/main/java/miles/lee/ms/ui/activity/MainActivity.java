package miles.lee.ms.ui.activity;

import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import miles.lee.ms.App;
import miles.lee.ms.R;
import miles.lee.ms.http.response.AppUpdateResponse;
import miles.lee.ms.ui.PresenterActivity;
import miles.lee.ms.ui.adapter.MainVpAdapter;
import miles.lee.ms.ui.fragment.ForshareFragment;
import miles.lee.ms.ui.fragment.MainDirectBroadcastFragment;
import miles.lee.ms.ui.fragment.MainMyselfFragment;
import miles.lee.ms.ui.fragment.MainRecommendFragment;
import miles.lee.ms.ui.presenter.MainPresenter;
import miles.lee.ms.ui.presenter.contract.MainContract;
import miles.lee.ms.ui.widget.PPfunsViewPager;
import miles.lee.ms.utils.LogUtil;
import miles.lee.ms.utils.Tips;

public class MainActivity extends PresenterActivity<MainPresenter> implements BottomNavigationBar.OnTabSelectedListener,MainContract.View{

    @BindView(R.id.top_bar)
    View topBarContainer;
    @BindView(R.id.location_tv)
    TextView location_tv;
    @BindView(R.id.connect_dev_tv_1)
    TextView connect_dev_tv_1;
    @BindView(R.id.search_tv)
    TextView search_tv;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    @BindView(R.id.view_pager)
    PPfunsViewPager mViewPager;
    private long firstMillis;

    @Override
    protected int getContentView(){
        return R.layout.activity_main;
    }

    @Override
    protected void initInject(){
        mPresenter = new MainPresenter();
    }

    @Override
    protected void initEventAndData(){
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.home_normal, R.string.recommend).setActiveColorResource(R.color.blue_sky))
                .addItem(new BottomNavigationItem(R.mipmap.live_normal, R.string.direct_broadcast).setActiveColorResource(R.color.blue_sky))
                .addItem(new BottomNavigationItem(R.mipmap.forshare_normal, R.string.forshare).setActiveColorResource(R.color.blue_sky))
                .addItem(new BottomNavigationItem(R.mipmap.mine_normal, R.string.myself).setActiveColorResource(R.color.blue_sky))
                .setFirstSelectedPosition(0)
                .setTabSelectedListener(this)
                .initialise();
        final String[] fnames = {
                MainRecommendFragment.class.getName(), MainDirectBroadcastFragment.class.getName(),
                ForshareFragment.class.getName(), MainMyselfFragment.class.getName()
        };
        MainVpAdapter adapter = new MainVpAdapter(getSupportFragmentManager(),this,fnames);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCanScroll(false);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void showError(String msg){
        Tips.showShortToast(msg);
    }

    @Override
    public void onTabSelected(int position){

    }

    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
        switch(level){
            case TRIM_MEMORY_UI_HIDDEN:
                LogUtil.d("onTrimMemory");
                break;
        }
        SparseBooleanArray array = new SparseBooleanArray();
    }

    @Override
    protected void onStop(){
        super.onStop();
        LogUtil.d("onStop");
    }

    @Override
    public void onTabUnselected(int position){
    }

    @Override
    public void onTabReselected(int position){
    }

    @Override
    public void showUpdateDialog(AppUpdateResponse appUpdateResponse){
    }

    @Override
    public void startUpdateService(AppUpdateResponse appUpdateResponse){
    }

    @Override
    public void setLocationCity(String city){
    }

    @Override
    public void showLocationDialog(String designatedCity){
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long currentMillis = System.currentTimeMillis();

            if (currentMillis - firstMillis > 2000) {
                firstMillis = currentMillis;
                Tips.showShortToast(R.string.exit_one_more_time);
            } else {
                //退出程序
                App.getInstance().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
