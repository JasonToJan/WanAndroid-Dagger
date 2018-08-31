package jan.jason.wanandroid.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.prefs.PreferenceHelper;

/**
 * @Description: App级别需要使用的对象
 *       component类似于Activity,module类似于Activity中想使用的对象
 *
 * @Author: jasonjan
 * @Date: 2018/8/30 21:00
 */
//TODO 待完善
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

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new DataManager();
    }
}
