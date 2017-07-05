package miles.lee.ms.component;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import miles.lee.ms.utils.LogUtil;

/**
 * @author miles
 * @classname: CrashHandler
 * @Description: 缓存
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler defaultHandler;

    private Context context = null;

    private static CrashHandler crashHandler;

    private CrashHandler(Context context) {
        this.context = context;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    /**
     * 初始化,设置该CrashHandler为程序的默认处理器
     */
    public static void init(Context context) {
        if (crashHandler == null) {
            crashHandler = new CrashHandler(context);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex);
            ex.printStackTrace();
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        StringBuilder builder = new StringBuilder();
        String thoInfo = ex.toString();
        builder.append(thoInfo);
        builder.append("\n\n");
        LogUtil.e(thoInfo);
        // 收集设备信息
        String devInfo = collectCrashDeviceInfo();
        builder.append(thoInfo);
        builder.append("\n\n");
        LogUtil.e(devInfo);
        // 详细堆栈信息
        String crashInfo = getCrashInfo(ex);
        LogUtil.e(crashInfo);
        // 保存错误报告文件
        saveCrashInfoToFile(crashInfo);
        return true;
    }

    /**
     * 得到程序崩溃的详细信息
     */
    public String getCrashInfo(Throwable ex) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.setStackTrace(ex.getStackTrace());
        ex.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * 收集程序崩溃的设备信息
     */
    public String collectCrashDeviceInfo() {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = pi.versionName;
            String model = android.os.Build.MODEL;
            String androidVersion = android.os.Build.VERSION.RELEASE;
            String manufacturer = android.os.Build.MANUFACTURER;
            return versionName + "  " + model + "  " + androidVersion + "  " + manufacturer;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param exInfo
     */
    private void saveCrashInfoToFile(String exInfo) {
        try {
            long timestamp = System.currentTimeMillis();
            String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;

            File file = new File(context.getExternalFilesDir(null), fileName);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(exInfo.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
