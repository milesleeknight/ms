package miles.lee.ms.utils;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import miles.lee.ms.component.Config;
import miles.lee.ms.http.exception.ApiException;
import miles.lee.ms.http.exception.EmptyDataException;
import miles.lee.ms.http.response.BaseResponse;

/**
 * Created by miles on 2017/5/27 0027.
 */

public class RxUtil{
    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一处理返回结果为BaseResponse的请求
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<BaseResponse<T>, T> handleResult() {   //compose判断结果
        return new FlowableTransformer<BaseResponse<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(@NonNull BaseResponse<T> tBaseResponse) throws Exception {
                        if(Config.HttpType.HTTP_SUCCESS.equals(tBaseResponse.getCode())) {
                            T t = tBaseResponse.getResult();
                            if(t == null){
                                return Flowable.error(new EmptyDataException("there is nothing"));
                            }
                            return createData(tBaseResponse.getResult());
                        } else {
                            return Flowable.error(new ApiException(tBaseResponse.getMessage()));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一处理返回结果为BaseResponse的请求
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<BaseResponse,Boolean> handleBaseResponse(){
        return new FlowableTransformer<BaseResponse, Boolean>(){
            @Override
            public Publisher<Boolean> apply(Flowable<BaseResponse> upstream){
                return upstream.flatMap(new Function<BaseResponse, Publisher<Boolean>>(){
                    @Override
                    public Publisher<Boolean> apply(@NonNull BaseResponse baseResponse) throws Exception{
                        if(Config.HttpType.HTTP_SUCCESS.equals(baseResponse.getCode())) {
                            return createData(true);
                        } else {
                            return Flowable.error(new ApiException(baseResponse.getMessage()));
                        }
                    }
                });
            }
        };
    }

    static <T> Flowable createData(final T t){
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
