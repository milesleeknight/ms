package miles.lee.ms.model.eventType;

/**
 * Created by miles on 2017/7/25 0025.
 */

public class DeviceInfoEvent{
    public static final int UPDATE_LIST = 1;
    public static final int CLEAR_LIST = 2;
    public static final int VARY_DEVICE = 3;//切换当前设备;
    public int what;

    public DeviceInfoEvent(int what){
        this.what = what;
    }
    public int getWhat(){
        return  what;
    }
}
