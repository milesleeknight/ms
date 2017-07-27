package miles.lee.ms.model.eventType;

/**
 * Created by miles on 2017/7/25 0025.
 */

public class UserInfoEvent{
    public static final int LOGIN = 1;
    public static final int LOGOUT = 2;
    public static final int UPDATE_USERINFO = 3;//更新用户信息

    private int type;

    public UserInfoEvent(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }
}
