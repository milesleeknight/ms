package miles.lee.ms.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import miles.lee.ms.App;

/**
 * Created by miles on 2017/5/17 0017.
 */

public abstract class BaseActivity extends AppCompatActivity{
    private static final String TAG = BaseActivity.class.getSimpleName();
    private Unbinder binder;
    private VaryViewHelperController mVaryViewHelperController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        App.getInstance().addActivity(this);
        init();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID){
        super.setContentView(layoutResID);
        //初始化黄油刀控件绑定框架
        binder = ButterKnife.bind(this);
        if (null != getLoadingTargetView())
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        binder.unbind();
        App.getInstance().removeActivity(this);
    }

    protected abstract int getContentView();
    protected abstract void init();
    protected View getLoadingTargetView(){
        return null;
    }
}
