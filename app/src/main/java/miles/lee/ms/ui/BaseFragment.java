package miles.lee.ms.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by miles on 2017/5/27 0027.
 */

public abstract class BaseFragment extends Fragment{
    protected BaseActivity mActivity;
    protected View mContentView;

    private boolean mIsPrepared = false;
    private boolean isFirstResume = true;
    private boolean mIsFirstVisible = true;
    private boolean mIsFirstInvisible = true;
    private Unbinder bind;
    protected VaryViewHelperController mVaryViewHelperController;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    // TODO: 2017/5/31 0031 同步方法？？
    private void initPrepare(){
        if(mIsPrepared){
            onFirstUserVisible();
        }else{
            mIsPrepared = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if(mIsFirstVisible){
                mIsFirstVisible = false;
                initPrepare();
            }else{
                onUserVisible();
            }
        } else {
            if(mIsFirstInvisible){
                mIsFirstInvisible = false;
                onFirstUserInvisible();
            }
            else{
                onUserInvisible();
            }
        }
    }

    /**
     * 对用户不可见，除第一次外
     */
    protected void onUserInvisible(){
    }

    /**
     * 第一次对用户不可见
     */
    protected void onFirstUserInvisible(){

    }

    /**
     * 对用户可见，除第一次外
     */
    protected void onUserVisible(){
    }

    /**
     * 第一次对用户可见
     */
    protected void onFirstUserVisible(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(getLayoutRes(),null);
    }

    protected abstract @LayoutRes int getLayoutRes();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.mContentView = view;
        bind = ButterKnife.bind(this, mContentView);
        if(null != getTargetLoadingView()){
            mVaryViewHelperController = new VaryViewHelperController(getTargetLoadingView());
        }
        init();
    }

    protected abstract void init();

    protected View getTargetLoadingView(){
        return null;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
         if (getUserVisibleHint()) {
             onUserVisible();
         }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mContentView = null;
        bind.unbind();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (BaseActivity)getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mActivity = null;
    }
}
