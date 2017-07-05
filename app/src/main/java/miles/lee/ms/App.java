package miles.lee.ms;

import android.app.Activity;
import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import java.util.HashSet;
import java.util.Set;

import miles.lee.ms.component.CrashHandler;

/**
 * Created by miles on 2017/5/17 0017.
 */

public class App extends Application{
    public static int SCREEN_WIDTH = -1;
    private static App app;
    public static App getInstance(){
        return app;
    }

    private Set<Activity> allActivities;
    @Override
    public void onCreate(){
        super.onCreate();
        app = this;
        if (!LeakCanary.isInAnalyzerProcess(App.getInstance())) {
            LeakCanary.install(App.getInstance());
        }
        // TODO: 2017/5/17 0017 初始化屏幕宽高
        //初始化错误收集
        CrashHandler.init(this);

    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act){
        if(allActivities != null){
            allActivities.remove(act);
        }
    }

    public void exitApp(){
        if(allActivities != null){
            synchronized(allActivities){
                for(Activity activity : allActivities){
                    activity.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
