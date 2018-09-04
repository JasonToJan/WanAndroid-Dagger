package jan.jason.wanandroid.di.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jan.jason.wanandroid.BuildConfig;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.core.http.api.GeeksApis;
import jan.jason.wanandroid.di.qualifier.WanAndroidUrl;
import jan.jason.wanandroid.utils.CommonUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description: 设置baseUrl和网络拦截相关设置
 *                  Module指定了组件可能需要使用的对象，Module提供了实例
 * @Author: jasonjan
 * @Date: 2018/9/4 9:25
 */
@Module
public class HttpModule {

    /**
     * 提供一个GeeksApis，也就是网络API,但是前提要有一个Retrofit
     * @param retrofit
     * @return
     */
    @Singleton
    @Provides
    GeeksApis provideGeeksApi(@WanAndroidUrl Retrofit retrofit){
        return retrofit.create(GeeksApis.class);   //通过类名访问
    }

    /**
     * 提供一个Retrofit，因为上面provideGeeksApi中需要
     * @param builder
     * @param client
     * @return
     */
    @Singleton
    @Provides
    @WanAndroidUrl
    Retrofit provideGeeksRetrofit(Retrofit.Builder builder, OkHttpClient client){
        return createRetrofit(builder,client,GeeksApis.HOST);//创建Retrofit
    }

    /**
     * 生产出一个Retrofit.Builder
     * @return
     */
    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    /**
     * 生产出一个OkHttpClient.Builder
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    /**
     * 生产出一个OkHttpClient
     * @param builder
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder){
        if(BuildConfig.DEBUG){
           /* HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);//设置日志等级*/

            //打印网络请求日志
            LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("WanAndroid-Request")
                    .response("WanAndroid-Response")
                    .build();

            builder.addInterceptor(httpLoggingInterceptor);
            builder.addNetworkInterceptor(new StethoInterceptor());//设置网络拦截
        }
        File cacheFile=new File(Constants.PATH_CACHE);
        Cache cache=new Cache(cacheFile,1024*1024*50);
        Interceptor cacheInterceptor=chain -> {
            Request request=chain.request();
            if(!CommonUtils.isNetworkConnected()){
                //无网络
                request=request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response=chain.proceed(request);
            if(CommonUtils.isNetworkConnected()){
                int maxAge=0;
                //有网络时，不缓存，最大保存时长为0
                response.newBuilder().header("Cache-Control","public,max-age="+maxAge)
                        .removeHeader("pragma")
                        .build();
            }else{
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }

            return response;
        };
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //cookie认证
        builder.cookieJar(new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(WanAndroidApp.getInstance())));
        return builder.build();
    }

    /**
     * 生产出一个Retrofit 通过Retrofit.Builder和OkHttpClient实例
     * @param builder
     * @param client
     * @param url
     * @return
     */
    private Retrofit createRetrofit(Retrofit.Builder builder,OkHttpClient client,String url){
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
