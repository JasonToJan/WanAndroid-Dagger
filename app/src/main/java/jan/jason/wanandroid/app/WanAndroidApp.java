package jan.jason.wanandroid.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import jan.jason.wanandroid.BuildConfig;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.core.dao.DaoMaster;
import jan.jason.wanandroid.core.dao.DaoSession;
import jan.jason.wanandroid.di.component.AppComponent;
import jan.jason.wanandroid.di.component.DaggerAppComponent;
import jan.jason.wanandroid.di.module.AppModule;
import jan.jason.wanandroid.di.module.HttpModule;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.logger.TxtFormatStrategy;

/**
 * @Description: 定义了APP开始的地方
 * @Author: jasonjan
 * @Date: 2018/8/23 21:31
 */
public class WanAndroidApp extends Application implements HasActivityInjector{

    /**
     * Activity注入器
     */
    @Inject
    DispatchingAndroidInjector<Activity> mAndroidInjector;

    /**
     * Application级别单例
     */
    private static WanAndroidApp instance;

    /**
     * 是否是第一次运行
     */
    public static boolean isFirstRun=true;

    /**
     * 内存泄漏相关类
     */
    private RefWatcher refWatcher;

    /**
     * 数据库相关类
     */
    private DaoSession mDaoSession;

    /**
     * App级别的组件，dagger用到
     */
    private static volatile AppComponent appComponent;

    /**
     * 类加载的时候就执行，且执行一次，常用于初始化属性，比构造函数还先执行
     */
    static{
        //Android API自带的，需要新建一个values-night文件夹来配合
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //设置头部刷新方案
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, refreshLayout) -> {
            refreshLayout.setPrimaryColorsId(R.color.colorPrimary,android.R.color.white);
            return new DeliveryHeader(context);
        });

        //设置底部刷新方案
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            return new BallPulseFooter(context).setAnimatingColor(ContextCompat.getColor(context,R.color.colorPrimary));
        });
    }

    /**
     * 单例获取实例，利用static synchronized关键字
     * @return
     */
    public static synchronized WanAndroidApp getInstance(){
        return instance;
    }

    /**
     * 获取RefWatcher类
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context){
        WanAndroidApp application=(WanAndroidApp) context.getApplicationContext();
        return application.refWatcher;
    }

    /**
     * 率先被调用，Application构造函数->attachBaseContext->onCreate
     * 在调用super.attachBaseContext之后，才可以使用context
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate(){
        super.onCreate();

        //注入了一个AppModule和HttpModule的一个实例
        DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .httpModule(new HttpModule())
                .build().inject(this);

        instance=this;

        initGreenDao();

        initBugly();

        initLogger();

        if(BuildConfig.DEBUG){
            //谷歌数据库，sp文件调试
            Stetho.initializeWithDefaults(this);
        }

        //判断是否在后台进程
        if(LeakCanary.isInAnalyzerProcess(this)){
            return ;
        }

        refWatcher=LeakCanary.install(this);
    }

    /**
     * 指导应用程序在不同的情况下进行自身的内存释放
     * 以避免被系统直接杀掉，提高应用程序的用户体验.
     * @param level
     */
    @Override
    public void onTrimMemory(int level){
        super.onTrimMemory(level);
        //程序界面已经不可见了
        if(level==TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    /**
     * 系统内存不足时调用
     */
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    /**
     * 初始化数据库
     */
    private void initGreenDao(){
        DaoMaster.DevOpenHelper devOpenHelper=new DaoMaster.DevOpenHelper(this,Constants.DB_NAME);
        SQLiteDatabase database=devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(database);
        mDaoSession=daoMaster.newSession();
    }

    /**
     * 获取数据库相关类
     * @return
     */
    public DaoSession getDaoSession(){
        return mDaoSession;
    }

    /**
     * 初始化Logger配置
     */
    private void initLogger(){
        if(BuildConfig.DEBUG){
            //个人偏好设置
            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag(getString(R.string.app_name)).build()));
        }
        //将log保存到本地
        Logger.addLogAdapter(new DiskLogAdapter(TxtFormatStrategy.newBuilder().tag(getString(R.string.app_name)).build(getPackageName(),getString(R.string.app_name))));
    }

    /**
     * 初始化Bugly
     */
    private void initBugly(){
        String packageName=getApplicationContext().getPackageName();
        String processName= CommonUtils.getProcessName(android.os.Process.myPid());//获取当前进程名称
        CrashReport.UserStrategy strategy=new CrashReport.UserStrategy(getApplicationContext());
        strategy.setUploadProcess(processName==null||processName.equals(packageName));
        CrashReport.initCrashReport(getApplicationContext(),Constants.BUGLY_ID,false,strategy);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mAndroidInjector;
    }

}
