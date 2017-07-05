package miles.lee.ms.ui.presenter;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import miles.lee.ms.http.SmiClient;
import miles.lee.ms.http.exception.EmptyDataException;
import miles.lee.ms.http.response.BaseCPResponse;
import miles.lee.ms.model.ChannelItem;
import miles.lee.ms.ui.presenter.contract.MainRecContract;
import miles.lee.ms.utils.RxUtil;

/**
 * Created by miles on 2017/5/31 0031.
 */

public class MainRecPresenter extends RxPresenter<MainRecContract.View> implements
        MainRecContract.Presenter{

    @Override
    public void getTabsData() {
        addSubscribe(SmiClient.getVodApi().getChannelList(32,1)
                .map(new Function<BaseCPResponse<List<ChannelItem>>, List<ChannelItem>>(){

            @Override
            public List<ChannelItem> apply(@NonNull BaseCPResponse<List<ChannelItem>>
                                                   listBaseCPResponse) throws Exception{
                List<ChannelItem> result = listBaseCPResponse.getData().getResult();
                if(result.isEmpty()){
                    throw new EmptyDataException("服务器没有数据");
                }
                return result;
            }
        }).compose(RxUtil.<List<ChannelItem>>rxSchedulerHelper())
                .doOnSubscribe(new Consumer<Subscription>(){
                    @Override
                    public void accept(@NonNull Subscription subscription) throws Exception{
                        view.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ChannelItem>>(){
                    @Override
                    public void accept(@NonNull List<ChannelItem> channelItems) throws Exception{
                        view.initPages(channelItems);
                    }
                }, new Consumer<Throwable>(){
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception{
                        if(throwable instanceof EmptyDataException){
                            view.showError(throwable.getMessage());
                        }else{
                            view.showError("出错啦，请稍候再试");
                        }
                    }
                }));
    }
}
