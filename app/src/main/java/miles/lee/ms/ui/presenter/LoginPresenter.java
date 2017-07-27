package miles.lee.ms.ui.presenter;

import android.content.Context;
import android.content.DialogInterface;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.ResourceSubscriber;
import miles.lee.ms.component.Config;
import miles.lee.ms.http.SmiClient;
import miles.lee.ms.http.exception.ApiException;
import miles.lee.ms.http.exception.EmptyDataException;
import miles.lee.ms.http.response.BaseResponse;
import miles.lee.ms.manager.UserInfoManager;
import miles.lee.ms.model.UserInfo;
import miles.lee.ms.ui.presenter.contract.LoginContract;
import miles.lee.ms.utils.RxUtil;
import okhttp3.internal.platform.Platform;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract
        .Presenter{

    private Disposable mLoginDisposable;
    private Context mContext;
    public LoginPresenter(Context context){
        mContext = context;
    }

    @Override
    public void loginFromAccunt(String account, String pwd){
        if(account.isEmpty() || pwd.isEmpty()){
            view.showError("账号或密码不能为空");
            return;
        }
        mLoginDisposable = SmiClient.getPersonApi().getUserInfoByLogin(account, pwd, Config
                .LoginType.ACCOUNT)
                .flatMap(new Function<BaseResponse<UserInfo>, Publisher<UserInfo>>(){
                    @Override
                    public Publisher<UserInfo> apply(@NonNull final BaseResponse<UserInfo>
                                                             userInfoBaseResponse) throws Exception{
                        if(userInfoBaseResponse == null || userInfoBaseResponse.getResult() ==
                                null){
                            return Flowable.error(new EmptyDataException("userInfo is null"));
                        }
                        if(Config.HttpType.HTTP_SUCCESS.equals(userInfoBaseResponse.getResult())){
                            return Flowable.create(new FlowableOnSubscribe<UserInfo>(){
                                @Override
                                public void subscribe(FlowableEmitter<UserInfo> e) throws Exception{
                                    try{
                                        e.onNext(userInfoBaseResponse.getResult());
                                        e.onComplete();
                                    }catch(Exception ex){
                                        e.onError(ex);
                                    }
                                }
                            }, BackpressureStrategy.BUFFER);
                        }else{
                            return Flowable.error(new ApiException(userInfoBaseResponse
                                    .getMessage()));
                        }
                    }
                }).compose(RxUtil.<UserInfo>rxSchedulerHelper())
                .doOnSubscribe(new Consumer<Subscription>(){
                    @Override
                    public void accept(@NonNull Subscription subscription) throws Exception{
                        view.showDialog("正在登录。。", new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface dialog){
                                removeSubscribe(mLoginDisposable);
                            }
                        });
                    }
                })
                .subscribeWith(new ResourceSubscriber<UserInfo>(){
                    @Override
                    public void onNext(UserInfo userInfo){
                        view.dismissLoadingDialog();
                        UserInfoManager.getInstance().setUserInfo(userInfo,UserInfoManager.USERINFO_LOGIN);
                    }

                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        if (view != null){
                           if (e instanceof ApiException) {
                                view.showError(e.getMessage());
                            } else if (e instanceof EmptyDataException) {
                                view.showError(e.getMessage());
                            } else if (e instanceof HttpException) {
                                view.showError("数据加载失败ヽ(≧Д≦)ノ");
                            } else {
                                view.showError("出错啦!ヽ(≧Д≦)ノ");
                            }
                        }
                    }

                    @Override
                    public void onComplete(){
                    }
                });
        addSubscribe(mLoginDisposable);
    }

    @Override
    public void loginFromAuth(String phone, String code){
    }

    @Override
    public void loginFromThird(Platform platform, String phone, String tokenId){
    }

    @Override
    public void countdown(){
    }
}