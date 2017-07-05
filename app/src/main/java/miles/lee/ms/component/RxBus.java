package miles.lee.ms.component;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import miles.lee.ms.utils.RxUtil;

/**
 * Created by miles on 2017/5/26 0026.
 */

public class RxBus{
    //事件总线
    private final FlowableProcessor<Object> bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Flowable的数据发射给观察者
    private RxBus(){
        bus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getDefault(){
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder{
        private static final RxBus sInstance = new RxBus();
    }

    public void post(Object obj){
        bus.onNext(obj);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Flowable <T> toFlowable(Class<T> eventType){
        return bus.ofType(eventType);
    }

    //封装默认订阅
    public <T> Disposable toDefaultFlowable(Class<T> eventType,Consumer<T> act){
        return bus.ofType(eventType).compose(RxUtil.<T>rxSchedulerHelper()).subscribe(act);
    }
}
