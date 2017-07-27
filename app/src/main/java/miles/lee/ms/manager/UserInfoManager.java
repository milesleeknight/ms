package miles.lee.ms.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import miles.lee.ms.component.ACache;
import miles.lee.ms.component.Config;
import miles.lee.ms.component.RxBus;
import miles.lee.ms.http.SmiClient;
import miles.lee.ms.model.StbDevice;
import miles.lee.ms.model.UserInfo;
import miles.lee.ms.model.eventType.DeviceInfoEvent;
import miles.lee.ms.model.eventType.UserInfoEvent;
import miles.lee.ms.push.PushAgent;
import miles.lee.ms.utils.RxUtil;

/**
 * Created by miles on 2017/7/25 0025.
 */

public class UserInfoManager{
    private volatile static UserInfoManager instance;
    public static int USERINFO_UPDATE = 0;
    public static int USERINFO_LOGIN = 1;
    private UserInfo userInfo;
    private Context mContext;
    private List<StbDevice> mStbDevices;

    public static UserInfoManager getInstance(){
        if (instance == null) {                         //Single Checked
            synchronized (UserInfoManager.class) {
                if (instance == null) {                 //Double Checked
                    instance = new UserInfoManager();
                }
            }
        }
        return instance ;
    }
    public static void install(Context context){
        getInstance().init(context);
    }

    private void init(Context context){
        mContext = context;
        mStbDevices = new ArrayList<>();
        ACache aCache = ACache.get(context);
        userInfo = (UserInfo) aCache.getAsObject(Config.ACacheType.USER_INFO);
        if(userInfo != null){
            //获取设备，发出登入消息
            fetchDevice();
            RxBus.getDefault().post(new UserInfoEvent(UserInfoEvent.LOGIN));
        }
        initAlias();
    }

    private void initAlias(){
        if(isEmpty()){
            return;
        }
        PushAgent.setAlias(mContext,"ppfuns_"+userInfo.getUserPhone());
    }

    private void cancleAlias(){
        PushAgent.setAlias(mContext,null);
    }

    //设置用户信息
    public void setUserInfo(UserInfo userInfo,int type){
        if(userInfo == null){
            return;
        }
        ACache cache = ACache.get(mContext,1000 * 1000,1);
        cache.remove(Config.ACacheType.USER_INFO);
        cache.put(Config.ACacheType.USER_INFO, userInfo);
        this.userInfo = userInfo;
        if(type == USERINFO_UPDATE){
            RxBus.getDefault().post(new UserInfoEvent(UserInfoEvent.UPDATE_USERINFO));
        }else{
            RxBus.getDefault().post(new UserInfoEvent(UserInfoEvent.LOGIN));
        }
    }

    private void fetchDevice(){
        SmiClient.getPersonApi().getDeviceList(userInfo.getUserId())
                .compose(RxUtil.<List<StbDevice>>handleResult())
                .compose(RxUtil.<List<StbDevice>>rxSchedulerHelper())
                .subscribe(new Consumer<List<StbDevice>>(){
                    @Override
                    public void accept(@NonNull List<StbDevice> stbDevices) throws Exception{
                        setBindDevice(stbDevices);
                    }
                }, new Consumer<Throwable>(){
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception{
                    }
                });
    }

    private void setBindDevice(List<StbDevice> stbDevices){
        if(stbDevices == null){
            return;
        }
        mStbDevices.clear();
        mStbDevices.addAll(stbDevices);
        RxBus.getDefault().post(new DeviceInfoEvent(DeviceInfoEvent.UPDATE_LIST));
    }

    public boolean isEmpty(){
        return userInfo == null;
    }
    public void release(){
        cancleAlias();
        mStbDevices =null;
        mContext = null;
        instance =null;
    }
}
