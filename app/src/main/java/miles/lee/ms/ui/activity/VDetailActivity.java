package miles.lee.ms.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import miles.lee.ms.R;
import miles.lee.ms.ui.PresenterActivity;
import miles.lee.ms.ui.presenter.VideoDetailPresenter;
import miles.lee.ms.ui.presenter.contract.VideoDetailContract;

/**
 * Created by miles on 2017/6/23 0023.
 */

public class VDetailActivity extends PresenterActivity<VideoDetailPresenter> implements VideoDetailContract.View{
    @Override
    protected int getContentView(){
        return R.layout.activity_vdtail;
    }

    @Override
    protected void init(){
    }

    @Override
    protected void initInject(){
    }

    @Override
    protected void initEventAndData(){
    }

    public static void launch(Context context,String contentId,int i){
        context.startActivity(new Intent(context,VDetailActivity.class));
    }

    @Override
    public void showError(String msg){
    }
    public void jump(View v){

    }
}
