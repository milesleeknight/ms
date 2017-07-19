package miles.lee.ms;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
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
    private Activity mCurrentActivity;

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
        registerActivityLifecycleCallbacks(callbacks);
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

    private ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mCurrentActivity = activity;
            // 修复InputMethodManager内存泄漏
//            imInstance.fixFocusedViewLeak(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if(activity == mCurrentActivity){
                mCurrentActivity = null;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };
}
