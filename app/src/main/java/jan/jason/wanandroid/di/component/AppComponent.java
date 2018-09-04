package jan.jason.wanandroid.di.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.di.module.AbstractAllActivityModule;
import jan.jason.wanandroid.di.module.AbstractAllFragmentModule;
import jan.jason.wanandroid.di.module.AppModule;
import jan.jason.wanandroid.di.module.HttpModule;

/**
 * @Description: App级别的组件
 *          component类似于Activity,module类似于Activity中想使用的对象
 *          这里更像是Application
 *
 * @Author: jasonjan
 * @Date: 2018/8/30 20:56
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AbstractAllActivityModule.class,
        AbstractAllFragmentModule.class,
        AppModule.class,
        HttpModule.class})
public interface AppComponent {

    /**
     * 注入WanAndroidApp实例
     * @param wanAndroidApp
     */
    void inject(WanAndroidApp wanAndroidApp);

    /**
     * 提供App级别的Context
     * @return
     */
    WanAndroidApp getContext();

    /**
     * 数据中心
     * @return
     */
    DataManager getDataManager();
}
