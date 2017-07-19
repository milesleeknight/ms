package miles.lee.ms.ui.presenter;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.zip.DataFormatException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import miles.lee.ms.component.Config;
import miles.lee.ms.http.SmiClient;
import miles.lee.ms.http.response.BaseCPResponse;
import miles.lee.ms.model.BannerItem;
import miles.lee.ms.model.CategoryItem;
import miles.lee.ms.model.ChannelItem;
import miles.lee.ms.model.ContentItem;
import miles.lee.ms.model.PageBean;
import miles.lee.ms.model.SubTypeBean;
import miles.lee.ms.ui.adapter.section.HomeBannerSection;
import miles.lee.ms.ui.adapter.section.HomeMovieSection;
import miles.lee.ms.ui.presenter.contract.SectionContact;
import miles.lee.ms.utils.CategorHelper;

/**
 * Created by miles on 2017/6/21 0021.
 */

public class SectionPresenter extends RxPresenter<SectionContact.View> implements SectionContact
        .Presenter{
    private Context mContext;
    private boolean isFirstChannel = false;
    private ChannelItem item;

    public SectionPresenter(Context context){
        this.mContext = context;
    }

    @Override
    public void initParams(ChannelItem item){
        if(item == null){
            throw new NullPointerException("can not find object of ChannelItem in arguments !");
        }
        this.item = item;
        if(item.getSeq() == 1){
            isFirstChannel = true;
        }
    }

    @Override
    public void loadData(){
        Log.e("yinhui","  loadData ");
        if(isFirstChannel){
            addSubscribe(Flowable.zip(SmiClient.getVodApi().getBanner(32, 1),
                    SmiClient.getVodApi().getChannelSubType(item.getOperationTagId(), 1),
                    new BiFunction<BaseCPResponse<List<BannerItem>>,
                            BaseCPResponse<List<SubTypeBean>>, List<SubTypeBean>>(){

                        @Override
                        public List<SubTypeBean> apply(@NonNull BaseCPResponse<List<BannerItem>>
                                                               bannerList, @NonNull
                                                               BaseCPResponse<List<SubTypeBean>>
                                                               subTypeList) throws
                                Exception{
                            List<BannerItem> bannerItemList = bannerList.getData().getResult();
                            if(bannerItemList != null && !bannerItemList.isEmpty()){
                                view.addSection(new HomeBannerSection(bannerItemList));
                            }
                            List<SubTypeBean> result = subTypeList.getData().getResult();
                            if(result == null || result.isEmpty()){
                                throw new DataFormatException("there is nothing in this channel~");
                            }
                            return result;
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
//                    .compose(RxUtil.<List<SubTypeBean>>rxSchedulerHelper())
                    .subscribe(new Consumer<List<SubTypeBean>>(){
                        @Override
                        public void accept(@NonNull List<SubTypeBean> subTypeBeen) throws Exception{
                            getSubyType(subTypeBeen);
                        }
                    }, new Consumer<Throwable>(){
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception{
                            throwable.printStackTrace();
                            view.showError(throwable.getMessage());
                        }
                    }));
        }else{
            addSubscribe(SmiClient.getVodApi().getChannelSubType(item.getOperationTagId(), 1)
                    .map(new Function<BaseCPResponse<List<SubTypeBean>>, List<SubTypeBean>>(){
                        @Override
                        public List<SubTypeBean> apply(@NonNull BaseCPResponse<List<SubTypeBean>>
                                                               listBaseCPResponse) throws Exception{
                            List<SubTypeBean> result = listBaseCPResponse.getData().getResult();
                            if(result == null || result.isEmpty()){
                                throw new DataFormatException("there is nothing in this channel~");
                            }
                            return result;
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
//                    .compose(RxUtil.<List<SubTypeBean>>rxSchedulerHelper())
                    .subscribe(new Consumer<List<SubTypeBean>>(){
                        @Override
                        public void accept(@NonNull List<SubTypeBean> subTypeBeen) throws Exception{
                            getSubyType(subTypeBeen);
                        }
                    }, new Consumer<Throwable>(){
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception{
                            throwable.printStackTrace();
                            view.showError(throwable.getMessage());
                        }
                    }));
        }
    }

    private void getSubyType(List<SubTypeBean> list){
        addSubscribe(Flowable.fromIterable(list)
                .flatMap(new Function<SubTypeBean,
                Flowable<CategoryItem>>(){
                    @Override
                    public Flowable<CategoryItem> apply(@NonNull SubTypeBean subTypeBean) throws
                            Exception{
                        int posType = subTypeBean.position;
                        int operationTagId = subTypeBean.operationTagId;
                        int pageSize = CategorHelper.getCategorPageSize(posType, isFirstChannel);
                        return Flowable.zip(SmiClient.getVodApi().getChannelSubContent(1,
                                pageSize, String.valueOf(operationTagId), 1)
                                , Flowable.just(subTypeBean), new BiFunction<BaseCPResponse<PageBean<ContentItem>>,
                                                SubTypeBean, CategoryItem>(){
                                    @Override
                                    public CategoryItem apply(@NonNull BaseCPResponse<PageBean <ContentItem>> response, @NonNull SubTypeBean subTypeBean) throws Exception{
                                        List<ContentItem> contentItemList = response.getData().getResult().getPageContent();
                                        CategoryItem item = new CategoryItem();
                                        if(subTypeBean.position == Config.RecommendedType.POS_LOOP_IMG){
                                            //view.addSection(new HomeLoopImgSection
                                            //(contentItemList));
                                         }else{
                                             item.setCategoryName(subTypeBean.operationTagName);
                                             item.setCategorySubType(subTypeBean.operationTagId);
                                             item.setCategoryImg(subTypeBean.picUrl);
                                             item.setCategoryType(subTypeBean.position);
                                             if(contentItemList != null && !contentItemList.isEmpty()){
                                                 item.setKeynote(contentItemList.get(0));
                                                 contentItemList.remove(contentItemList.get(0));
                                             }
                                             item.setPage(contentItemList);

                                             view.addSection(new HomeMovieSection(item, mContext,isFirstChannel));
                                         }
                                        return item;
                                    }
                                }
                        );
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<CategoryItem>(){
                            @Override
                            public void accept(@NonNull CategoryItem categoryItem) throws Exception{
                            }
                        }, new Consumer<Throwable>(){
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception{
                                throwable.printStackTrace();
                                view.showError("出错啦，请稍后再试~");
                            }
                        }, new Action(){
                            @Override
                            public void run() throws Exception{
                                view.finishTask();
                            }
                        })
        );
    }

    @Override
    public void detachView(){
        item = null;
        super.detachView();
    }
}
