package miles.lee.ms.http;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import miles.lee.ms.App;
import miles.lee.ms.constant.UrlConstant;
import miles.lee.ms.utils.NetworkUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author
 * @classname: SmiClient
 * @Description: retrofit实例获取类
 * @date 2017/2/8 16:18
 */
public final class SmiClient{
    private static OkHttpClient okHttpClient;
    private SmiClient(){}

    static {
        initOkHttpClient();
    }

    private static <T> T createApi(Class<T> clazz,String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

    public static VodService getVodApi(){
        return createApi(VodService.class, UrlConstant.VOD_URL);
    }

    private static void initOkHttpClient(){
        synchronized(SmiClient.class) {
            if(okHttpClient == null){
                //指定缓存目录
                File cacheFile = new File(App.getInstance().getCacheDir(), "HttpCache");
                Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);
                //云端响应头拦截器，用来配置缓存策略,离线可以缓存，在线就获取最新数据
                OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .cache(cache)
                        .addNetworkInterceptor(new CacheInterceptor())
                        .addNetworkInterceptor(new StethoInterceptor())
                        .retryOnConnectionFailure(true);
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("SmiClient",message);
                    }
                });
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); builder.addInterceptor(interceptor);
                okHttpClient = builder.build();
            }
        }
    }

    /**
     * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
     */
    private static class CacheInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException{

            // 有网络时 设置缓存超时时间1个小时
            int maxAge = 60 * 60;
            // 无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();
            if (NetworkUtils.isAvailable(App.getInstance())) {
                //有网络时只从网络获取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else {
                //无网络时只从缓存中读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (NetworkUtils.isAvailable(App.getInstance())) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }
}
