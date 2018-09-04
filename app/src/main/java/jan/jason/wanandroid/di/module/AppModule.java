package jan.jason.wanandroid.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.db.DbHelper;
import jan.jason.wanandroid.core.db.DbHelperImpl;
import jan.jason.wanandroid.core.http.HttpHelper;
import jan.jason.wanandroid.core.http.HttpHelperImpl;
import jan.jason.wanandroid.core.prefs.PreferenceHelper;
import jan.jason.wanandroid.core.prefs.PreferenceHelperImpl;

/**
 * @Description: App级别需要使用的对象
 *       component类似于Activity,module类似于Activity中想使用的对象
 *
 * @Author: jasonjan
 * @Date: 2018/8/30 21:00
 */
@Module
public class AppModule {

    private final WanAndroidApp application;

    public AppModule(WanAndroidApp application){
        this.application=application;
    }

    /**
     * 提供一个Application级别的Context
     * @return
     */
    @Provides
    @Singleton
    WanAndroidApp provideApplicationContext(){
        return application;
    }

    /**
     * 提供一个网络帮助类，传入的是一个实现类，传出的是一个接口
     * @param httpHelperImpl
     * @return
     */
    @Provides
    @Singleton
    HttpHelper provideHttpHelper(HttpHelperImpl httpHelperImpl) {
        return httpHelperImpl;
    }

    /**
     * 提供一个数据库帮助类，传入一个实现类，传出的是一个接口
     * @param realmHelper
     * @return
     */
    @Provides
    @Singleton
    DbHelper provideDBHelper(DbHelperImpl realmHelper) {
        return realmHelper;
    }

    /**
     * 提供一个文件存储帮助类，传入一个实现类，传出的是一个接口
     * @param implPreferencesHelper
     * @return
     */
    @Provides
    @Singleton
    PreferenceHelper providePreferencesHelper(PreferenceHelperImpl implPreferencesHelper) {
        return implPreferencesHelper;
    }

    /**
     * 提供一个数据管理员，管理员下又包含了3个分部，一个网络部门，一个数据库部门，一个文件存储部分，3者合为一个组成了数据管理员
     * @param httpHelper
     * @param dbhelper
     * @param preferencesHelper
     * @return
     */
    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper, DbHelper dbhelper, PreferenceHelper preferencesHelper) {
        return new DataManager(httpHelper, dbhelper, preferencesHelper);
    }
}
