package miles.lee.ms;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.squareup.leakcanary.LeakCanary;

import java.util.HashSet;
import java.util.Set;

import miles.lee.ms.component.CrashHandler;

/**
 * Created by miles on 2017/5/17 0017.
 */

public class App extends Application{
    private static App app;
    public static int SCREEN_WIDTH = -1;
    private static int SCREEN_HIGHT = -1;
    private static float SCREEN_SCALE = -1.0F;
    private static int DIMEN_DPI = 0;

    public static App getInstance(){
        return app;
    }

    private Set<Activity> allActivities;
    @Override
    public void onCreate(){
        super.onCreate();
        app = this;
        //初始化屏幕宽高
        getScreenSize();
        if (!LeakCanary.isInAnalyzerProcess(App.getInstance())) {
            LeakCanary.install(App.getInstance());
        }
        // TODO: 2017/5/17 0017 初始化屏幕宽高
        //初始化错误收集
        CrashHandler.init(this);

    }

    private void getScreenSize(){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = manager.getDefaultDisplay();
        display.getMetrics(dm);
        SCREEN_SCALE = dm.scaledDensity;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HIGHT = dm.heightPixels;
        DIMEN_DPI = dm.densityDpi;
        if(SCREEN_WIDTH > SCREEN_HIGHT){
            int t = SCREEN_HIGHT;
            SCREEN_HIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
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
