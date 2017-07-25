package miles.lee.ms.manager;

import android.content.Context;

import miles.lee.ms.model.UserInfo;

/**
 * Created by miles on 2017/7/25 0025.
 */

public class UserInfoManager{
    private volatile static UserInfoManager instance;
    private UserInfo userInfo;

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

    }
    public boolean isEmpty(){
        return userInfo == null;
    }
}
