package jan.jason.wanandroid.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jan.jason.wanandroid.di.component.BaseActivityComponent;
import jan.jason.wanandroid.ui.main.activity.SplashActivity;

/**
 * @Description: 所有活动的抽象Module
 *           component类似于Activity,module类似于Activity中想使用的对象
 *
 * @Author: jasonjan
 * @Date: 2018/8/30 20:52
 */
@Module(subcomponents = BaseActivityComponent.class)
public abstract class AbstractAllActivityModule {

    @ContributesAndroidInjector(modules = SplashActivityModule.class)
    abstract SplashActivity contributesSplashActivityInject();
}
