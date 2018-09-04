package jan.jason.wanandroid.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jan.jason.wanandroid.di.component.BaseFragmentComponent;
import jan.jason.wanandroid.ui.mainpager.fragment.MainPagerFragment;

/**
 * @Description: 管理所有基类片段的Module
 * @Author: jasonjan
 * @Date: 2018/9/4 10:31
 */
@Module(subcomponents = BaseFragmentComponent.class)
public abstract class AbstractAllFragmentModule {

    /**
     * 注入MainPagerFragmentModule，使其具有生命力
     * @return
     */
    @ContributesAndroidInjector(modules = MainPagerFragmentModule.class)
    abstract MainPagerFragment contributesMainPagerFragmentInject();
}
